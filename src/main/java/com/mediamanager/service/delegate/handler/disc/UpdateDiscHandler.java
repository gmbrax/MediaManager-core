package com.mediamanager.service.delegate.handler.disc;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mediamanager.mapper.DiscMapper;
import com.mediamanager.model.Disc;
import com.mediamanager.protocol.TransportProtocol;
import com.mediamanager.protocol.messages.DiscMessages;
import com.mediamanager.service.delegate.ActionHandler;
import com.mediamanager.service.delegate.annotation.Action;
import com.mediamanager.service.disc.DiscService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

@Action("disc.update")
public class UpdateDiscHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(UpdateDiscHandler.class);
    private final DiscService discService;

    public UpdateDiscHandler(DiscService discService) {
        this.discService = discService;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload)
            throws InvalidProtocolBufferException {
        try {
            DiscMessages.UpdateDiscRequest updateRequest =
                    DiscMessages.UpdateDiscRequest.parseFrom(requestPayload);

            int id = updateRequest.getId();
            Integer discNumber = updateRequest.getDiscNumber();
            Integer albumId = updateRequest.getFkAlbumId() > 0 ? updateRequest.getFkAlbumId() : null;

            Optional<Disc> discOpt = discService.updateDisc(
                    id,
                    discNumber,
                    albumId
            );

            if (discOpt.isEmpty()) {
                logger.warn("Disc not found with ID: {}", id);
                return TransportProtocol.Response.newBuilder()
                        .setStatusCode(404)
                        .setPayload(ByteString.copyFromUtf8("Disc not found"));
            }

            DiscMessages.Disc discProto = DiscMapper.toProtobuf(discOpt.get());

            DiscMessages.UpdateDiscResponse updateResponse =
                    DiscMessages.UpdateDiscResponse.newBuilder()
                            .setDisc(discProto)
                            .build();

            return TransportProtocol.Response.newBuilder()
                    .setPayload(updateResponse.toByteString());

        } catch (IllegalArgumentException e) {
            logger.error("Validation error", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(400)
                    .setPayload(ByteString.copyFromUtf8("Validation error: " + e.getMessage()));
        } catch (Exception e) {
            logger.error("Error updating disc", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(ByteString.copyFromUtf8("Error: " + e.getMessage()));
        }
    }
}
