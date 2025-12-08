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

import java.util.Optional;

@Action(value = "albumhasgenre.getById")
public class GetAlbumHasGenreByIdHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(GetAlbumHasGenreByIdHandler.class);
    private final AlbumHasGenreService albumHasGenreService;

    public GetAlbumHasGenreByIdHandler(AlbumHasGenreService albumHasGenreService) {
        this.albumHasGenreService = albumHasGenreService;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload)
        throws InvalidProtocolBufferException{

        try{
            AlbumHasGenreMessages.GetAlbumHasGenreByIdRequest getByIdRequest =
                    AlbumHasGenreMessages.GetAlbumHasGenreByIdRequest.parseFrom(requestPayload);
            int id = getByIdRequest.getId();

            Optional<AlbumHasGenre> albumHasGenreOpt = albumHasGenreService.getAlbumHasGenreById(id);

            if (albumHasGenreOpt.isEmpty()){
                logger.warn("AlbumHasGenre not found with ID: {}", id);
                return TransportProtocol.Response.newBuilder()
                        .setStatusCode(404)
                        .setPayload(ByteString.copyFromUtf8("AlbumHasGenre not found"));
            }
            AlbumHasGenreMessages.AlbumHasGenre albumHasGenreProto = AlbumHasGenreMapper.toProtobuf(albumHasGenreOpt.get());
            AlbumHasGenreMessages.GetAlbumHasGenreByIdResponse getByIdResponse = AlbumHasGenreMessages.GetAlbumHasGenreByIdResponse.newBuilder()
                    .setAlbumhasgenre(albumHasGenreProto)
                    .build();
            return TransportProtocol.Response.newBuilder()
                    .setPayload(getByIdResponse.toByteString());
        } catch (Exception e) {
            logger.error("Error getting album has genre by ID", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(ByteString.copyFromUtf8("Error: "+ e.getMessage()));
        }
    }
}
