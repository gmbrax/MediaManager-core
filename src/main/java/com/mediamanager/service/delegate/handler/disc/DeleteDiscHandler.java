package com.mediamanager.service.delegate.handler.disc;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mediamanager.protocol.TransportProtocol;
import com.mediamanager.protocol.messages.DiscMessages;
import com.mediamanager.service.delegate.ActionHandler;
import com.mediamanager.service.delegate.annotation.Action;
import com.mediamanager.service.disc.DiscService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Action("disc.delete")
public class DeleteDiscHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(DeleteDiscHandler.class);

    private final DiscService discService;

    public DeleteDiscHandler(DiscService discService) {
        this.discService = discService;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload)
            throws InvalidProtocolBufferException {

        try {
            DiscMessages.DeleteDiscRequest deleteRequest =
                    DiscMessages.DeleteDiscRequest.parseFrom(requestPayload);
            int id = deleteRequest.getId();
            boolean success = discService.deleteDisc(id);
            DiscMessages.DeleteDiscResponse deleteResponse;
            if (success) {
                deleteResponse = DiscMessages.DeleteDiscResponse.newBuilder()
                        .setMessage("Disc deleted successfully")
                        .build();
                return TransportProtocol.Response.newBuilder()
                        .setPayload(deleteResponse.toByteString());
            } else {
                deleteResponse = DiscMessages.DeleteDiscResponse.newBuilder()
                        .setMessage("Disc not found")
                        .build();

                return TransportProtocol.Response.newBuilder()
                        .setStatusCode(404)
                        .setPayload(deleteResponse.toByteString());
            }
        } catch (Exception e) {
            logger.error("Error deleting disc", e);
            DiscMessages.DeleteDiscResponse deleteResponse =
                    DiscMessages.DeleteDiscResponse.newBuilder()
                            .setMessage("Error: " + e.getMessage())
                            .build();
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(deleteResponse.toByteString());
        }
    }
}
