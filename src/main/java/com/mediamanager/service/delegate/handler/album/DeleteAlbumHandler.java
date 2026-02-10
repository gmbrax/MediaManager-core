package com.mediamanager.service.delegate.handler.album;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mediamanager.protocol.TransportProtocol;
import com.mediamanager.protocol.messages.AlbumMessages;
import com.mediamanager.service.delegate.ActionHandler;
import com.mediamanager.service.delegate.annotation.Action;
import com.mediamanager.service.album.AlbumService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Action("album.delete")
public class DeleteAlbumHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(DeleteAlbumHandler.class);

    private final AlbumService albumService;

    public DeleteAlbumHandler(AlbumService albumService) {
        this.albumService = albumService;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload)
            throws InvalidProtocolBufferException {

        try {
            AlbumMessages.DeleteAlbumRequest deleteRequest =
                    AlbumMessages.DeleteAlbumRequest.parseFrom(requestPayload);
            int id = deleteRequest.getId();
            boolean success = albumService.deleteAlbum(id);
            AlbumMessages.DeleteAlbumResponse deleteResponse;
            if (success) {
                deleteResponse = AlbumMessages.DeleteAlbumResponse.newBuilder()
                        .setSuccess(true)
                        .setMessage("Album deleted successfully")
                        .build();
                return TransportProtocol.Response.newBuilder()
                        .setPayload(deleteResponse.toByteString());
            } else {
                deleteResponse = AlbumMessages.DeleteAlbumResponse.newBuilder()
                        .setSuccess(false)
                        .setMessage("Album not found")
                        .build();

                return TransportProtocol.Response.newBuilder()
                        .setStatusCode(404)
                        .setPayload(deleteResponse.toByteString());
            }
        } catch (Exception e) {
            logger.error("Error deleting album", e);
            AlbumMessages.DeleteAlbumResponse deleteResponse =
                    AlbumMessages.DeleteAlbumResponse.newBuilder()
                            .setSuccess(false)
                            .setMessage("Error: " + e.getMessage())
                            .build();
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(deleteResponse.toByteString());
        }
    }
    }
