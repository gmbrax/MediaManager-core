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

import java.util.Optional;

@Action(value = "samplingrate.getById")
public class GetSamplingRateByIdHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(GetSamplingRateByIdHandler.class);
    private final SamplingRateService samplingRateService;

    public GetSamplingRateByIdHandler(SamplingRateService samplingRateService) {
        this.samplingRateService = samplingRateService;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload)
        throws InvalidProtocolBufferException{

        try{
            SamplingRateMessages.GetSamplingRateByIDRequest getByIdRequest =
                    SamplingRateMessages.GetSamplingRateByIDRequest.parseFrom(requestPayload);
            int id = getByIdRequest.getId();

            Optional<SamplingRate> samplingRateOpt = samplingRateService.getSamplingRateById(id);

            if (samplingRateOpt.isEmpty()){
                logger.warn("SamplingRate not found with ID: {}", id);
                return TransportProtocol.Response.newBuilder()
                        .setStatusCode(404)
                        .setPayload(ByteString.copyFromUtf8("SamplingRate not found"));
            }
            SamplingRateMessages.SamplingRate samplingRateProto = SamplingRateMapper.toProtobuf(samplingRateOpt.get());
            SamplingRateMessages.GetSamplingRateByIdResponse getByIdResponse = SamplingRateMessages.GetSamplingRateByIdResponse.newBuilder()
                    .setSamplingrate(samplingRateProto)
                    .build();
            return TransportProtocol.Response.newBuilder()
                    .setPayload(getByIdResponse.toByteString());
        } catch (Exception e) {
            logger.error("Error getting sampling rate by ID", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(ByteString.copyFromUtf8("Error: "+ e.getMessage()));
        }
    }
}
