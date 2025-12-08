package com.mediamanager.service.delegate.handler.albumhasartist;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mediamanager.protocol.TransportProtocol;
import com.mediamanager.protocol.messages.AlbumHasArtistMessages;
import com.mediamanager.service.delegate.ActionHandler;
import com.mediamanager.service.delegate.annotation.Action;
import com.mediamanager.service.albumhasartist.AlbumHasArtistService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Action("albumhasartist.delete")
public class DeleteAlbumHasArtistHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(DeleteAlbumHasArtistHandler.class);

    private final AlbumHasArtistService albumHasArtistService;

    public DeleteAlbumHasArtistHandler(AlbumHasArtistService albumHasArtistService) {
        this.albumHasArtistService = albumHasArtistService;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload)
            throws InvalidProtocolBufferException {

        try {
            AlbumHasArtistMessages.DeleteAlbumHasArtistRequest deleteRequest =
                    AlbumHasArtistMessages.DeleteAlbumHasArtistRequest.parseFrom(requestPayload);
            int id = deleteRequest.getId();
            boolean success = albumHasArtistService.deleteAlbumHasArtist(id);
            AlbumHasArtistMessages.DeleteAlbumHasArtistResponse deleteResponse;
            if (success) {
                deleteResponse = AlbumHasArtistMessages.DeleteAlbumHasArtistResponse.newBuilder()
                        .setSuccess(true)
                        .setMessage("Album has artist deleted successfully")
                        .build();
                return TransportProtocol.Response.newBuilder()
                        .setPayload(deleteResponse.toByteString());
            } else {
                deleteResponse = AlbumHasArtistMessages.DeleteAlbumHasArtistResponse.newBuilder()
                        .setSuccess(false)
                        .setMessage("Album has artist not found")
                        .build();

                return TransportProtocol.Response.newBuilder()
                        .setStatusCode(404)
                        .setPayload(deleteResponse.toByteString());
            }
        } catch (Exception e) {
            logger.error("Error deleting album has artist", e);
            AlbumHasArtistMessages.DeleteAlbumHasArtistResponse deleteResponse =
                    AlbumHasArtistMessages.DeleteAlbumHasArtistResponse.newBuilder()
                            .setSuccess(false)
                            .setMessage("Error: " + e.getMessage())
                            .build();
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(deleteResponse.toByteString());
        }
    }
    }
