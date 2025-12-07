package com.mediamanager.service.delegate.handler.albumart;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mediamanager.protocol.TransportProtocol;
import com.mediamanager.protocol.messages.AlbumArtMessages;
import com.mediamanager.service.delegate.ActionHandler;
import com.mediamanager.service.delegate.annotation.Action;
import com.mediamanager.service.albumart.AlbumArtService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Action("albumart.delete")
public class DeleteAlbumArtHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(DeleteAlbumArtHandler.class);

    private final AlbumArtService albumArtService;

    public DeleteAlbumArtHandler(AlbumArtService albumArtService) {
        this.albumArtService = albumArtService;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload)
            throws InvalidProtocolBufferException {

        try {
            AlbumArtMessages.DeleteAlbumArtRequest deleteRequest =
                    AlbumArtMessages.DeleteAlbumArtRequest.parseFrom(requestPayload);
            int id = deleteRequest.getId();
            boolean success = albumArtService.deleteAlbumArt(id);
            AlbumArtMessages.DeleteAlbumArtResponse deleteResponse;
            if (success) {
                deleteResponse = AlbumArtMessages.DeleteAlbumArtResponse.newBuilder()
                        .setSuccess(true)
                        .setMessage("Album art deleted successfully")
                        .build();
                return TransportProtocol.Response.newBuilder()
                        .setPayload(deleteResponse.toByteString());
            } else {
                deleteResponse = AlbumArtMessages.DeleteAlbumArtResponse.newBuilder()
                        .setSuccess(false)
                        .setMessage("Album art not found")
                        .build();

                return TransportProtocol.Response.newBuilder()
                        .setStatusCode(404)
                        .setPayload(deleteResponse.toByteString());
            }
        } catch (Exception e) {
            logger.error("Error deleting album art", e);
            AlbumArtMessages.DeleteAlbumArtResponse deleteResponse =
                    AlbumArtMessages.DeleteAlbumArtResponse.newBuilder()
                            .setSuccess(false)
                            .setMessage("Error: " + e.getMessage())
                            .build();
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(deleteResponse.toByteString());
        }
    }
    }
