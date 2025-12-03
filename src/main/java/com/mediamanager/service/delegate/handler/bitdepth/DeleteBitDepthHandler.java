package com.mediamanager.service.delegate.handler.bitdepth;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mediamanager.protocol.TransportProtocol;
import com.mediamanager.protocol.messages.BitDepthMessages;
import com.mediamanager.service.bitdepth.BitDepthService;
import com.mediamanager.service.delegate.ActionHandler;
import com.mediamanager.service.delegate.annotation.Action;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Action("bitdepth.delete")
public class DeleteBitDepthHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(DeleteBitDepthHandler.class);

    private final BitDepthService bitDepthService;

    public DeleteBitDepthHandler(BitDepthService bitDepthService) {
        this.bitDepthService = bitDepthService;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload)
            throws InvalidProtocolBufferException {

        try {
            BitDepthMessages.DeleteBitDepthRequest deleteRequest =
                    BitDepthMessages.DeleteBitDepthRequest.parseFrom(requestPayload);
            int id = deleteRequest.getId();
            boolean success = bitDepthService.deleteBitDepth(id);
            BitDepthMessages.DeleteBitDepthResponse deleteResponse;
            if (success) {
                deleteResponse = BitDepthMessages.DeleteBitDepthResponse.newBuilder()
                        .setSuccess(true)
                        .setMessage("Bit-Depth deleted successfully")
                        .build();
                return TransportProtocol.Response.newBuilder()
                        .setPayload(deleteResponse.toByteString());
            } else {
                deleteResponse = BitDepthMessages.DeleteBitDepthResponse.newBuilder()
                        .setSuccess(false)
                        .setMessage("Bit-Depth not found")
                        .build();

                return TransportProtocol.Response.newBuilder()
                        .setStatusCode(404)
                        .setPayload(deleteResponse.toByteString());
            }
        } catch (Exception e) {
            logger.error("Error deleting bit-depth", e);
            BitDepthMessages.DeleteBitDepthResponse deleteResponse =
                    BitDepthMessages.DeleteBitDepthResponse.newBuilder()
                            .setSuccess(false)
                            .setMessage("Error: " + e.getMessage())
                            .build();
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(deleteResponse.toByteString());
        }
    }
    }
