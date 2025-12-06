package com.mediamanager.service.delegate.handler.bitrate;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mediamanager.protocol.TransportProtocol;
import com.mediamanager.protocol.messages.BitRateMessages;
import com.mediamanager.service.bitrate.BitRateService;
import com.mediamanager.service.delegate.ActionHandler;
import com.mediamanager.service.delegate.annotation.Action;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Action("bitrate.delete")
public class DeleteBitRateHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(DeleteBitRateHandler.class);

    private final BitRateService bitRateService;

    public DeleteBitRateHandler(BitRateService bitRateService) {
        this.bitRateService = bitRateService;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload)
            throws InvalidProtocolBufferException {

        try {
            BitRateMessages.DeleteBitRateRequest deleteRequest =
                    BitRateMessages.DeleteBitRateRequest.parseFrom(requestPayload);
            int id = deleteRequest.getId();
            boolean success = bitRateService.deleteBitRate(id);
            BitRateMessages.DeleteBitRateResponse deleteResponse;
            if (success) {
                deleteResponse = BitRateMessages.DeleteBitRateResponse.newBuilder()
                        .setSuccess(true)
                        .setMessage("Bit-Rate deleted successfully")
                        .build();
                return TransportProtocol.Response.newBuilder()
                        .setPayload(deleteResponse.toByteString());
            } else {
                deleteResponse = BitRateMessages.DeleteBitRateResponse.newBuilder()
                        .setSuccess(false)
                        .setMessage("Bit-Rate not found")
                        .build();

                return TransportProtocol.Response.newBuilder()
                        .setStatusCode(404)
                        .setPayload(deleteResponse.toByteString());
            }
        } catch (Exception e) {
            logger.error("Error deleting bit-rate", e);
            BitRateMessages.DeleteBitRateResponse deleteResponse =
                    BitRateMessages.DeleteBitRateResponse.newBuilder()
                            .setSuccess(false)
                            .setMessage("Error: " + e.getMessage())
                            .build();
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(deleteResponse.toByteString());
        }
    }
    }
