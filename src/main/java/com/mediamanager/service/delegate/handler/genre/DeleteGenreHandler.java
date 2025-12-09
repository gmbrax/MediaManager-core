package com.mediamanager.service.delegate.handler.genre;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mediamanager.protocol.TransportProtocol;
import com.mediamanager.protocol.messages.GenreMessages;
import com.mediamanager.service.delegate.ActionHandler;
import com.mediamanager.service.delegate.annotation.Action;
import com.mediamanager.service.genre.GenreService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Action("genre.delete")
public class DeleteGenreHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(DeleteGenreHandler.class);

    private final GenreService genreService;

    public DeleteGenreHandler(GenreService genreService) {
        this.genreService = genreService;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload)
            throws InvalidProtocolBufferException {
        try {
            // 1. Parse protobuf request
            GenreMessages.DeleteGenreRequest deleteRequest =
                    GenreMessages.DeleteGenreRequest.parseFrom(requestPayload);

            int id = deleteRequest.getId();

            // 2. Deleta via service
            boolean success = genreService.deleteGenre(id);

            // 3. Cria response
            GenreMessages.DeleteGenreResponse deleteResponse;

            if (success) {
                deleteResponse = GenreMessages.DeleteGenreResponse.newBuilder()
                        .setSuccess(true)
                        .setMessage("Genre deleted successfully")
                        .build();

                return TransportProtocol.Response.newBuilder()
                        .setPayload(deleteResponse.toByteString());
            } else {
                deleteResponse = GenreMessages.DeleteGenreResponse.newBuilder()
                        .setSuccess(false)
                        .setMessage("Genre not found")
                        .build();

                return TransportProtocol.Response.newBuilder()
                        .setStatusCode(404)
                        .setPayload(deleteResponse.toByteString());
            }

        } catch (Exception e) {
            logger.error("Error deleting genre", e);

            GenreMessages.DeleteGenreResponse deleteResponse =
                    GenreMessages.DeleteGenreResponse.newBuilder()
                            .setSuccess(false)
                            .setMessage("Error: " + e.getMessage())
                            .build();

            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(deleteResponse.toByteString());
        }
    }
}