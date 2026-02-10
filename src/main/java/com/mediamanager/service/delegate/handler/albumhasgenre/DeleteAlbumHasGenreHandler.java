package com.mediamanager.service.delegate.handler.albumhasgenre;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mediamanager.protocol.TransportProtocol;
import com.mediamanager.protocol.messages.AlbumHasGenreMessages;
import com.mediamanager.service.delegate.ActionHandler;
import com.mediamanager.service.delegate.annotation.Action;
import com.mediamanager.service.albumhasgenre.AlbumHasGenreService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Action("albumhasgenre.delete")
public class DeleteAlbumHasGenreHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(DeleteAlbumHasGenreHandler.class);

    private final AlbumHasGenreService albumHasGenreService;

    public DeleteAlbumHasGenreHandler(AlbumHasGenreService albumHasGenreService) {
        this.albumHasGenreService = albumHasGenreService;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload)
            throws InvalidProtocolBufferException {

        try {
            AlbumHasGenreMessages.DeleteAlbumHasGenreRequest deleteRequest =
                    AlbumHasGenreMessages.DeleteAlbumHasGenreRequest.parseFrom(requestPayload);
            int id = deleteRequest.getId();
            boolean success = albumHasGenreService.deleteAlbumHasGenre(id);
            AlbumHasGenreMessages.DeleteAlbumHasGenreResponse deleteResponse;
            if (success) {
                deleteResponse = AlbumHasGenreMessages.DeleteAlbumHasGenreResponse.newBuilder()
                        .setSuccess(true)
                        .setMessage("Album has genre deleted successfully")
                        .build();
                return TransportProtocol.Response.newBuilder()
                        .setPayload(deleteResponse.toByteString());
            } else {
                deleteResponse = AlbumHasGenreMessages.DeleteAlbumHasGenreResponse.newBuilder()
                        .setSuccess(false)
                        .setMessage("Album has genre not found")
                        .build();

                return TransportProtocol.Response.newBuilder()
                        .setStatusCode(404)
                        .setPayload(deleteResponse.toByteString());
            }
        } catch (Exception e) {
            logger.error("Error deleting album has genre", e);
            AlbumHasGenreMessages.DeleteAlbumHasGenreResponse deleteResponse =
                    AlbumHasGenreMessages.DeleteAlbumHasGenreResponse.newBuilder()
                            .setSuccess(false)
                            .setMessage("Error: " + e.getMessage())
                            .build();
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(deleteResponse.toByteString());
        }
    }
    }
