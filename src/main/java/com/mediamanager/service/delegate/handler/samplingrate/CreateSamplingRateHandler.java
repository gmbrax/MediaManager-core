package com.mediamanager.service.delegate.handler.samplingrate;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mediamanager.mapper.SamplingRateMapper;
import com.mediamanager.model.SamplingRate;
import com.mediamanager.protocol.TransportProtocol;
import com.mediamanager.protocol.messages.SamplingRateMessages;
import com.mediamanager.service.delegate.ActionHandler;
import com.mediamanager.service.delegate.annotation.Action;
import com.mediamanager.service.samplingrate.SamplingRateService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Action("samplingrate.create")
public class CreateSamplingRateHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(CreateSamplingRateHandler.class);
    private final SamplingRateService samplingRateService;

    public CreateSamplingRateHandler(SamplingRateService samplingRateService) {
        this.samplingRateService = samplingRateService;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload) throws InvalidProtocolBufferException {
        try{
            SamplingRateMessages.CreateSamplingRateRequest createRequest =
                    SamplingRateMessages.CreateSamplingRateRequest.parseFrom(requestPayload);
            SamplingRate samplingRate = samplingRateService.createSamplingRate(createRequest.getValue());
            SamplingRateMessages.SamplingRate samplingRateProto = SamplingRateMapper.toProtobuf(samplingRate);
            SamplingRateMessages.CreateSamplingRateResponse createSamplingRateResponse = SamplingRateMessages.CreateSamplingRateResponse.newBuilder()
                    .setSamplingrate(samplingRateProto)
                    .build();
            return TransportProtocol.Response.newBuilder()
                    .setPayload(createSamplingRateResponse.toByteString());
        } catch (IllegalArgumentException e) {
            logger.error("Validation error", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(400)
                    .setPayload(ByteString.copyFromUtf8("Validation error: " + e.getMessage()));

        } catch (Exception e) {
            logger.error("Error creating sampling rate", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(ByteString.copyFromUtf8("Error: " + e.getMessage()));
        }
    }
}

