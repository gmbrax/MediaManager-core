package com.mediamanager.service.delegate.handler.albumhasgenre;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mediamanager.mapper.AlbumHasGenreMapper;
import com.mediamanager.model.AlbumHasGenre;
import com.mediamanager.protocol.TransportProtocol;
import com.mediamanager.protocol.messages.AlbumHasGenreMessages;
import com.mediamanager.service.delegate.ActionHandler;
import com.mediamanager.service.delegate.annotation.Action;
import com.mediamanager.service.albumhasgenre.AlbumHasGenreService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Action("albumhasgenre.create")
public class CreateAlbumHasGenreHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(CreateAlbumHasGenreHandler.class);
    private final AlbumHasGenreService albumHasGenreService;

    public CreateAlbumHasGenreHandler(AlbumHasGenreService albumHasGenreService) {
        this.albumHasGenreService = albumHasGenreService;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload) throws InvalidProtocolBufferException {
        try{
            AlbumHasGenreMessages.CreateAlbumHasGenreRequest createRequest =
                    AlbumHasGenreMessages.CreateAlbumHasGenreRequest.parseFrom(requestPayload);

            AlbumHasGenre albumHasGenre = albumHasGenreService.createAlbumHasGenre(
                    createRequest.getFkAlbumId() > 0 ? createRequest.getFkAlbumId() : null,
                    createRequest.getFkGenreId() > 0 ? createRequest.getFkGenreId() : null
            );

            AlbumHasGenreMessages.AlbumHasGenre albumHasGenreProto = AlbumHasGenreMapper.toProtobuf(albumHasGenre);
            AlbumHasGenreMessages.CreateAlbumHasGenreResponse createAlbumHasGenreResponse = AlbumHasGenreMessages.CreateAlbumHasGenreResponse.newBuilder()
                    .setAlbumhasgenre(albumHasGenreProto)
                    .build();
            return TransportProtocol.Response.newBuilder()
                    .setPayload(createAlbumHasGenreResponse.toByteString());
        } catch (IllegalArgumentException e) {
            logger.error("Validation error", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(400)
                    .setPayload(ByteString.copyFromUtf8("Validation error: " + e.getMessage()));

        } catch (Exception e) {
            logger.error("Error creating album has genre", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(ByteString.copyFromUtf8("Error: " + e.getMessage()));
        }
    }
}
