package com.mediamanager.service.delegate.handler.samplingrate;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mediamanager.protocol.TransportProtocol;
import com.mediamanager.protocol.messages.SamplingRateMessages;
import com.mediamanager.service.delegate.ActionHandler;
import com.mediamanager.service.delegate.annotation.Action;
import com.mediamanager.service.samplingrate.SamplingRateService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Action("samplingrate.delete")
public class DeleteSamplingRateHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(DeleteSamplingRateHandler.class);

    private final SamplingRateService samplingRateService;

    public DeleteSamplingRateHandler(SamplingRateService samplingRateService) {
        this.samplingRateService = samplingRateService;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload)
            throws InvalidProtocolBufferException {

        try {
            SamplingRateMessages.DeleteSamplingRateRequest deleteRequest =
                    SamplingRateMessages.DeleteSamplingRateRequest.parseFrom(requestPayload);
            int id = deleteRequest.getId();
            boolean success = samplingRateService.deleteSamplingRate(id);
            SamplingRateMessages.DeleteSamplingRateResponse deleteResponse;
            if (success) {
                deleteResponse = SamplingRateMessages.DeleteSamplingRateResponse.newBuilder()
                        .setSuccess(true)
                        .setMessage("Sampling rate deleted successfully")
                        .build();
                return TransportProtocol.Response.newBuilder()
                        .setPayload(deleteResponse.toByteString());
            } else {
                deleteResponse = SamplingRateMessages.DeleteSamplingRateResponse.newBuilder()
                        .setSuccess(false)
                        .setMessage("Sampling rate not found")
                        .build();

                return TransportProtocol.Response.newBuilder()
                        .setStatusCode(404)
                        .setPayload(deleteResponse.toByteString());
            }
        } catch (Exception e) {
            logger.error("Error deleting sampling rate", e);
            SamplingRateMessages.DeleteSamplingRateResponse deleteResponse =
                    SamplingRateMessages.DeleteSamplingRateResponse.newBuilder()
                            .setSuccess(false)
                            .setMessage("Error: " + e.getMessage())
                            .build();
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(deleteResponse.toByteString());
        }
    }
    }
