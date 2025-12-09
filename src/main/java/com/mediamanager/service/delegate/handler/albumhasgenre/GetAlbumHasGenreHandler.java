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

import java.util.List;


@Action("albumhasgenre.getAll")
public class GetAlbumHasGenreHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(GetAlbumHasGenreHandler.class);

    private final AlbumHasGenreService albumHasGenreService;

    public GetAlbumHasGenreHandler(AlbumHasGenreService albumHasGenreService){this.albumHasGenreService = albumHasGenreService;}

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload) throws InvalidProtocolBufferException {
        try{
            List<AlbumHasGenre> albumHasGenres = albumHasGenreService.getAllAlbumHasGenres();
            AlbumHasGenreMessages.GetAlbumHasGenresResponse.Builder responseBuilder = AlbumHasGenreMessages.GetAlbumHasGenresResponse.newBuilder();

            for (AlbumHasGenre albumHasGenre : albumHasGenres) {
                AlbumHasGenreMessages.AlbumHasGenre albumHasGenreProto = AlbumHasGenreMapper.toProtobuf(albumHasGenre);
                responseBuilder.addAlbumhasgenre(albumHasGenreProto);
            }
            AlbumHasGenreMessages.GetAlbumHasGenresResponse getAlbumHasGenresResponse = responseBuilder.build();

            return TransportProtocol.Response.newBuilder()
                    .setPayload(getAlbumHasGenresResponse.toByteString());

        }catch (Exception e){
            logger.error("Error getting album has genres", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(ByteString.copyFromUtf8("Error: " + e.getMessage()));
        }
    }
}
