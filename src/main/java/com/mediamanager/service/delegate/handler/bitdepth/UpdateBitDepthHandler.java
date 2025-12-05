package com.mediamanager.service.delegate.handler.bitdepth;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mediamanager.mapper.BitDepthMapper;
import com.mediamanager.model.BitDepth;
import com.mediamanager.protocol.TransportProtocol;
import com.mediamanager.protocol.messages.BitDepthMessages;
import com.mediamanager.service.bitdepth.BitDepthService;
import com.mediamanager.service.delegate.ActionHandler;
import com.mediamanager.service.delegate.annotation.Action;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

@Action("bitdepth.update")
public class UpdateBitDepthHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(UpdateBitDepthHandler.class);
    private final BitDepthService bitDepthService;

    public UpdateBitDepthHandler(BitDepthService bitDepthService) {
        this.bitDepthService = bitDepthService;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload) throws InvalidProtocolBufferException {
        try{
            BitDepthMessages.UpdateBitDepthRequest updateRequest =
                    BitDepthMessages.UpdateBitDepthRequest.parseFrom(requestPayload);

            int id = updateRequest.getId();
            String newValue = updateRequest.getValue();

            Optional<BitDepth> bitDepthOpt = bitDepthService.updateBitDepth(id, newValue);

            if(bitDepthOpt.isEmpty()){
                logger.warn("BitDepth not found with ID: {}", id);
                return TransportProtocol.Response.newBuilder()
                        .setStatusCode(404)
                        .setPayload(ByteString.copyFromUtf8("BitDepth not found"));
            }

            BitDepthMessages.BitDepth bitDepthProto = BitDepthMapper.toProtobuf(bitDepthOpt.get());

            BitDepthMessages.UpdateBitDepthResponse updateResponse = BitDepthMessages.UpdateBitDepthResponse.newBuilder()
                    .setBitdepth(bitDepthProto)
                    .build();

            return TransportProtocol.Response.newBuilder()
                    .setPayload(updateResponse.toByteString());

        } catch (IllegalArgumentException e){
            logger.error("Validation error", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(400)
                    .setPayload(ByteString.copyFromUtf8("Validation error: " + e.getMessage()));
        } catch (Exception e) {
            logger.error("Error updating bit-depth", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(ByteString.copyFromUtf8("Error: " + e.getMessage()));
        }
    }
}
