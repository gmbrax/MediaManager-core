package com.mediamanager.service.delegate.handler.bitrate;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mediamanager.mapper.BitRateMapper;
import com.mediamanager.model.BitRate;
import com.mediamanager.protocol.TransportProtocol;
import com.mediamanager.protocol.messages.BitRateMessages;
import com.mediamanager.service.bitrate.BitRateService;
import com.mediamanager.service.delegate.ActionHandler;
import com.mediamanager.service.delegate.annotation.Action;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

@Action("bitrate.update")
public class UpdateBitRateHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(UpdateBitRateHandler.class);
    private final BitRateService bitRateService;

    public UpdateBitRateHandler(BitRateService bitRateService) {
        this.bitRateService = bitRateService;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload) throws InvalidProtocolBufferException {
        try{
            BitRateMessages.UpdateBitRateRequest updateRequest =
                    BitRateMessages.UpdateBitRateRequest.parseFrom(requestPayload);

            int id = updateRequest.getId();
            String newValue = updateRequest.getValue();

            Optional<BitRate> bitRateOpt = bitRateService.updateBitRate(id, newValue);

            if(bitRateOpt.isEmpty()){
                logger.warn("BitRate not found with ID: {}", id);
                return TransportProtocol.Response.newBuilder()
                        .setStatusCode(404)
                        .setPayload(ByteString.copyFromUtf8("BitRate not found"));
            }

            BitRateMessages.BitRate bitRateProto = BitRateMapper.toProtobuf(bitRateOpt.get());

            BitRateMessages.UpdateBitRateResponse updateResponse = BitRateMessages.UpdateBitRateResponse.newBuilder()
                    .setBitrate(bitRateProto)
                    .build();

            return TransportProtocol.Response.newBuilder()
                    .setPayload(updateResponse.toByteString());

        } catch (IllegalArgumentException e){
            logger.error("Validation error", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(400)
                    .setPayload(ByteString.copyFromUtf8("Validation error: " + e.getMessage()));
        } catch (Exception e) {
            logger.error("Error updating bit-rate", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(ByteString.copyFromUtf8("Error: " + e.getMessage()));
        }
    }
}
