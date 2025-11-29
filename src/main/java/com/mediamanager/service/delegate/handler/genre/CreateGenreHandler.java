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

public class CreateGenreHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(CreateGenreHandler.class);

    private final GenreService genreService;

    public CreateGenreHandler(GenreService genreService) {
        this.genreService = genreService;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload)
            throws InvalidProtocolBufferException {
        try {
            // 1. Parse protobuf request
            GenreMessages.CreateGenreRequest createRequest =
                    GenreMessages.CreateGenreRequest.parseFrom(requestPayload);

            // 2. Chama service (lógica de negócio)
            Genre genre = genreService.createGenre(createRequest.getName());

            // 3. Converte entity para protobuf
            GenreMessages.Genre genreProto = GenreMapper.toProtobuf(genre);

            // 4. Cria response protobuf
            GenreMessages.CreateGenreResponse createResponse =
                    GenreMessages.CreateGenreResponse.newBuilder()
                            .setGenre(genreProto)
                            .build();

            // 5. Retorna
            return TransportProtocol.Response.newBuilder()
                    .setPayload(createResponse.toByteString());

        } catch (IllegalArgumentException e) {
            logger.error("Validation error", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(400)
                    .setPayload(ByteString.copyFromUtf8("Validation error: " + e.getMessage()));

        } catch (Exception e) {
            logger.error("Error creating genre", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(ByteString.copyFromUtf8("Error: " + e.getMessage()));
        }
    }
}