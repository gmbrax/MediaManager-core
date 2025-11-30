package com.mediamanager.service.delegate.handler.artist;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mediamanager.protocol.TransportProtocol;
import com.mediamanager.protocol.messages.ArtistMessages;
import com.mediamanager.service.artist.ArtistService;
import com.mediamanager.service.delegate.ActionHandler;
import com.mediamanager.service.delegate.annotation.Action;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Action("artist.delete")
public class DeleteArtistHandle implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(DeleteArtistHandle.class);

    private final ArtistService artistService;

    public DeleteArtistHandle(ArtistService artistService) {
        this.artistService = artistService;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload)
            throws InvalidProtocolBufferException {

        try {
            ArtistMessages.DeleteArtistRequest deleteRequest =
                    ArtistMessages.DeleteArtistRequest.parseFrom(requestPayload);
            int id = deleteRequest.getId();
            boolean success = artistService.deleteArtist(id);
            ArtistMessages.DeleteArtistResponse deleteResponse;
            if (success) {
                deleteResponse = ArtistMessages.DeleteArtistResponse.newBuilder()
                        .setSuccess(true)
                        .setMessage("Artist deleted successfully")
                        .build();
                return TransportProtocol.Response.newBuilder()
                        .setPayload(deleteResponse.toByteString());
            } else {
                deleteResponse = ArtistMessages.DeleteArtistResponse.newBuilder()
                        .setSuccess(false)
                        .setMessage("Artist not found")
                        .build();

                return TransportProtocol.Response.newBuilder()
                        .setStatusCode(404)
                        .setPayload(deleteResponse.toByteString());
            }
        } catch (Exception e) {
            logger.error("Error deleting artist", e);
            ArtistMessages.DeleteArtistResponse deleteResponse =
                    ArtistMessages.DeleteArtistResponse.newBuilder()
                            .setSuccess(false)
                            .setMessage("Error: " + e.getMessage())
                            .build();
                    return TransportProtocol.Response.newBuilder()
                            .setStatusCode(500)
                            .setPayload(deleteResponse.toByteString());
        }
        }
    }
