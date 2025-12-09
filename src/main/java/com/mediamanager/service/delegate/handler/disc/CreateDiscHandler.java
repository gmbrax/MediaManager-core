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

@Action("disc.create")
public class CreateDiscHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(CreateDiscHandler.class);
    private final DiscService discService;

    public CreateDiscHandler(DiscService discService) {
        this.discService = discService;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload) throws InvalidProtocolBufferException {
        try {
            DiscMessages.CreateDiscRequest createRequest =
                    DiscMessages.CreateDiscRequest.parseFrom(requestPayload);

            Disc disc = discService.createDisc(
                    createRequest.getDiscNumber(),
                    createRequest.getFkAlbumId() > 0 ? createRequest.getFkAlbumId() : null
            );

            DiscMessages.Disc discProto = DiscMapper.toProtobuf(disc);
            DiscMessages.CreateDiscResponse createDiscResponse = DiscMessages.CreateDiscResponse.newBuilder()
                    .setDisc(discProto)
                    .build();
            return TransportProtocol.Response.newBuilder()
                    .setPayload(createDiscResponse.toByteString());
        } catch (IllegalArgumentException e) {
            logger.error("Validation error", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(400)
                    .setPayload(ByteString.copyFromUtf8("Invalid request"));

        } catch (InvalidProtocolBufferException e) {
            logger.error("Invalid CreateDiscRequest payload", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(400)
                    .setPayload(ByteString.copyFromUtf8("Invalid request payload"));

        } catch (Exception e) {
            logger.error("Error creating disc", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(ByteString.copyFromUtf8("Internal server error"));
        }
    }
    }

