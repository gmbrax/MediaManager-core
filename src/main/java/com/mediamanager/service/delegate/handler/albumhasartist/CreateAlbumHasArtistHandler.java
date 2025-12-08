package com.mediamanager.service.delegate.handler.albumhasartist;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mediamanager.mapper.AlbumHasArtistMapper;
import com.mediamanager.model.AlbumHasArtist;
import com.mediamanager.protocol.TransportProtocol;
import com.mediamanager.protocol.messages.AlbumHasArtistMessages;
import com.mediamanager.service.delegate.ActionHandler;
import com.mediamanager.service.delegate.annotation.Action;
import com.mediamanager.service.albumhasartist.AlbumHasArtistService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Action("albumhasartist.create")
public class CreateAlbumHasArtistHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(CreateAlbumHasArtistHandler.class);
    private final AlbumHasArtistService albumHasArtistService;

    public CreateAlbumHasArtistHandler(AlbumHasArtistService albumHasArtistService) {
        this.albumHasArtistService = albumHasArtistService;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload) throws InvalidProtocolBufferException {
        try{
            AlbumHasArtistMessages.CreateAlbumHasArtistRequest createRequest =
                    AlbumHasArtistMessages.CreateAlbumHasArtistRequest.parseFrom(requestPayload);

            AlbumHasArtist albumHasArtist = albumHasArtistService.createAlbumHasArtist(
                    createRequest.getFkAlbumId() > 0 ? createRequest.getFkAlbumId() : null,
                    createRequest.getFkArtistId() > 0 ? createRequest.getFkArtistId() : null
            );

            AlbumHasArtistMessages.AlbumHasArtist albumHasArtistProto = AlbumHasArtistMapper.toProtobuf(albumHasArtist);
            AlbumHasArtistMessages.CreateAlbumHasArtistResponse createAlbumHasArtistResponse = AlbumHasArtistMessages.CreateAlbumHasArtistResponse.newBuilder()
                    .setAlbumhasartist(albumHasArtistProto)
                    .build();
            return TransportProtocol.Response.newBuilder()
                    .setPayload(createAlbumHasArtistResponse.toByteString());
        } catch (IllegalArgumentException e) {
            logger.error("Validation error", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(400)
                    .setPayload(ByteString.copyFromUtf8("Validation error: " + e.getMessage()));

        } catch (Exception e) {
            logger.error("Error creating album has artist", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(ByteString.copyFromUtf8("Error: " + e.getMessage()));
        }
    }
}
