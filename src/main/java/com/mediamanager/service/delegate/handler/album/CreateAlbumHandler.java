package com.mediamanager.service.delegate.handler.album;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mediamanager.mapper.AlbumMapper;
import com.mediamanager.model.Album;
import com.mediamanager.protocol.TransportProtocol;
import com.mediamanager.protocol.messages.AlbumMessages;
import com.mediamanager.service.delegate.ActionHandler;
import com.mediamanager.service.delegate.annotation.Action;
import com.mediamanager.service.album.AlbumService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Action("album.create")
public class CreateAlbumHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(CreateAlbumHandler.class);
    private final AlbumService albumService;

    public CreateAlbumHandler(AlbumService albumService) {
        this.albumService = albumService;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload) throws InvalidProtocolBufferException {
        try{
            AlbumMessages.CreateAlbumRequest createRequest =
                    AlbumMessages.CreateAlbumRequest.parseFrom(requestPayload);

            Album album = albumService.createAlbum(
                    createRequest.getName(),
                    createRequest.getYear() > 0 ? createRequest.getYear() : null,
                    createRequest.getNumberOfDiscs() > 0 ? createRequest.getNumberOfDiscs() : null,
                    createRequest.getCode().isEmpty() ? null : createRequest.getCode(),
                    createRequest.getIsCompilation(),
                    createRequest.getFkAlbumtypeId() > 0 ? createRequest.getFkAlbumtypeId() : null,
                    createRequest.getFkAlbumartId() > 0 ? createRequest.getFkAlbumartId() : null
            );

            AlbumMessages.Album albumProto = AlbumMapper.toProtobuf(album);
            AlbumMessages.CreateAlbumResponse createAlbumResponse = AlbumMessages.CreateAlbumResponse.newBuilder()
                    .setAlbum(albumProto)
                    .build();
            return TransportProtocol.Response.newBuilder()
                    .setPayload(createAlbumResponse.toByteString());
        } catch (IllegalArgumentException e) {
            logger.error("Validation error", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(400)
                    .setPayload(ByteString.copyFromUtf8("Validation error: " + e.getMessage()));

        } catch (Exception e) {
            logger.error("Error creating album", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(ByteString.copyFromUtf8("Error: " + e.getMessage()));
        }
    }
}
