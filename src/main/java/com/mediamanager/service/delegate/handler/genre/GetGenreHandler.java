package com.mediamanager.service.delegate.handler.genre;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mediamanager.mapper.GenreMapper;
import com.mediamanager.model.Genre;
import com.mediamanager.protocol.TransportProtocol;
import com.mediamanager.protocol.messages.GenreMessages;
import com.mediamanager.service.delegate.ActionHandler;
import com.mediamanager.service.delegate.annotation.Action;
import com.mediamanager.service.genre.GenreService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@Action("genre.getAll")
public class GetGenreHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(GetGenreHandler.class);

    private final GenreService genreService;

    public GetGenreHandler(GenreService genreService) {
        this.genreService = genreService;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload)
            throws InvalidProtocolBufferException {
        try {
            // 1. Busca todos os genres via service
            List<Genre> genres = genreService.getAllGenres();

            // 2. Converte cada Genre para protobuf
            GenreMessages.GetGenresResponse.Builder responseBuilder =
                    GenreMessages.GetGenresResponse.newBuilder();

            for (Genre genre : genres) {
                GenreMessages.Genre genreProto = GenreMapper.toProtobuf(genre);
                responseBuilder.addGenres(genreProto);
            }

            GenreMessages.GetGenresResponse getGenresResponse = responseBuilder.build();

            // 3. Retorna
            return TransportProtocol.Response.newBuilder()
                    .setPayload(getGenresResponse.toByteString());

        } catch (Exception e) {
            logger.error("Error getting genres", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(ByteString.copyFromUtf8("Error: " + e.getMessage()));
        }
    }
}