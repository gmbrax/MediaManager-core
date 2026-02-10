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

@Action("samplingrate.update")
public class UpdateSamplingRateHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(UpdateSamplingRateHandler.class);
    private final SamplingRateService samplingRateService;

    public UpdateSamplingRateHandler(SamplingRateService samplingRateService) {
        this.samplingRateService = samplingRateService;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload) throws InvalidProtocolBufferException {
        try{
            SamplingRateMessages.UpdateSamplingRateRequest updateRequest =
                    SamplingRateMessages.UpdateSamplingRateRequest.parseFrom(requestPayload);

            int id = updateRequest.getId();
            String newValue = updateRequest.getValue();

            Optional<SamplingRate> samplingRateOpt = samplingRateService.updateSamplingRate(id, newValue);

            if(samplingRateOpt.isEmpty()){
                logger.warn("SamplingRate not found with ID: {}", id);
                return TransportProtocol.Response.newBuilder()
                        .setStatusCode(404)
                        .setPayload(ByteString.copyFromUtf8("SamplingRate not found"));
            }

            SamplingRateMessages.SamplingRate samplingRateProto = SamplingRateMapper.toProtobuf(samplingRateOpt.get());

            SamplingRateMessages.UpdateSamplingRateResponse updateResponse = SamplingRateMessages.UpdateSamplingRateResponse.newBuilder()
                    .setSamplingrate(samplingRateProto)
                    .build();

            return TransportProtocol.Response.newBuilder()
                    .setPayload(updateResponse.toByteString());

        } catch (IllegalArgumentException e){
            logger.error("Validation error", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(400)
                    .setPayload(ByteString.copyFromUtf8("Validation error: " + e.getMessage()));
        } catch (Exception e) {
            logger.error("Error updating sampling rate", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(ByteString.copyFromUtf8("Error: " + e.getMessage()));
        }
    }
}
