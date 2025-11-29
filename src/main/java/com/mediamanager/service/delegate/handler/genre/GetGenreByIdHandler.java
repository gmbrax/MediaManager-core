package com.mediamanager.service.delegate.handler.genre;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mediamanager.mapper.GenreMapper;
import com.mediamanager.model.Genre;
import com.mediamanager.protocol.TransportProtocol;
import com.mediamanager.protocol.messages.GenreMessages;
import com.mediamanager.service.delegate.ActionHandler;
import com.mediamanager.service.genre.GenreService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class GetGenreByIdHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(GetGenreByIdHandler.class);

    private final GenreService genreService;

    public GetGenreByIdHandler(GenreService genreService) {
        this.genreService = genreService;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload)
            throws InvalidProtocolBufferException {
        try {
            // 1. Parse protobuf request
            GenreMessages.GetGenreByIdRequest getByIdRequest =
                    GenreMessages.GetGenreByIdRequest.parseFrom(requestPayload);

            int id = getByIdRequest.getId();

            // 2. Busca via service
            Optional<Genre> genreOpt = genreService.getGenreById(id);

            if (genreOpt.isEmpty()) {
                logger.warn("Genre not found with ID: {}", id);
                return TransportProtocol.Response.newBuilder()
                        .setStatusCode(404)
                        .setPayload(ByteString.copyFromUtf8("Genre not found"));
            }

            // 3. Converte para protobuf
            GenreMessages.Genre genreProto = GenreMapper.toProtobuf(genreOpt.get());

            GenreMessages.GetGenreByIdResponse getByIdResponse =
                    GenreMessages.GetGenreByIdResponse.newBuilder()
                            .setGenre(genreProto)
                            .build();

            // 4. Retorna
            return TransportProtocol.Response.newBuilder()
                    .setPayload(getByIdResponse.toByteString());

        } catch (Exception e) {
            logger.error("Error getting genre by ID", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(ByteString.copyFromUtf8("Error: " + e.getMessage()));
        }
    }
}