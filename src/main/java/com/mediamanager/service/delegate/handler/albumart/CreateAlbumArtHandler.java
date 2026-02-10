package com.mediamanager.service.delegate.handler.albumart;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mediamanager.mapper.AlbumArtMapper;
import com.mediamanager.model.AlbumArt;
import com.mediamanager.protocol.TransportProtocol;
import com.mediamanager.protocol.messages.AlbumArtMessages;
import com.mediamanager.service.delegate.ActionHandler;
import com.mediamanager.service.delegate.annotation.Action;
import com.mediamanager.service.albumart.AlbumArtService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Action("albumart.create")
public class CreateAlbumArtHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(CreateAlbumArtHandler.class);
    private final AlbumArtService albumArtService;

    public CreateAlbumArtHandler(AlbumArtService albumArtService) {
        this.albumArtService = albumArtService;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload) throws InvalidProtocolBufferException {
        try{
            AlbumArtMessages.CreateAlbumArtRequest createRequest =
                    AlbumArtMessages.CreateAlbumArtRequest.parseFrom(requestPayload);
            AlbumArt albumArt = albumArtService.createAlbumArt(createRequest.getFilepath());
            AlbumArtMessages.AlbumArt albumArtProto = AlbumArtMapper.toProtobuf(albumArt);
            AlbumArtMessages.CreateAlbumArtResponse createAlbumArtResponse = AlbumArtMessages.CreateAlbumArtResponse.newBuilder()
                    .setAlbumart(albumArtProto)
                    .build();
            return TransportProtocol.Response.newBuilder()
                    .setPayload(createAlbumArtResponse.toByteString());
        } catch (IllegalArgumentException e) {
            logger.error("Validation error", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(400)
                    .setPayload(ByteString.copyFromUtf8("Validation error: " + e.getMessage()));

        } catch (Exception e) {
            logger.error("Error creating album art", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(ByteString.copyFromUtf8("Error: " + e.getMessage()));
        }
    }
}
