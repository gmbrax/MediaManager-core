package com.mediamanager.service.delegate.handler.trackhasartist;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mediamanager.protocol.TransportProtocol;
import com.mediamanager.protocol.messages.TrackHasArtistMessages;
import com.mediamanager.service.delegate.ActionHandler;
import com.mediamanager.service.delegate.annotation.Action;
import com.mediamanager.service.trackhasartist.TrackHasArtistService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Action("trackhasartist.delete")
public class DeleteTrackHasArtistHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(DeleteTrackHasArtistHandler.class);

    private final TrackHasArtistService trackHasArtistService;

    public DeleteTrackHasArtistHandler(TrackHasArtistService trackHasArtistService) {
        this.trackHasArtistService = trackHasArtistService;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload)
            throws InvalidProtocolBufferException {

        try {
            TrackHasArtistMessages.DeleteTrackHasArtistRequest deleteRequest =
                    TrackHasArtistMessages.DeleteTrackHasArtistRequest.parseFrom(requestPayload);
            int id = deleteRequest.getId();
            boolean success = trackHasArtistService.deleteTrackHasArtist(id);
            TrackHasArtistMessages.DeleteTrackHasArtistResponse deleteResponse;
            if (success) {
                deleteResponse = TrackHasArtistMessages.DeleteTrackHasArtistResponse.newBuilder()
                        .setSuccess(true)
                        .setMessage("Track has artist deleted successfully")
                        .build();
                return TransportProtocol.Response.newBuilder()
                        .setPayload(deleteResponse.toByteString());
            } else {
                deleteResponse = TrackHasArtistMessages.DeleteTrackHasArtistResponse.newBuilder()
                        .setSuccess(false)
                        .setMessage("Track has artist not found")
                        .build();

                return TransportProtocol.Response.newBuilder()
                        .setStatusCode(404)
                        .setPayload(deleteResponse.toByteString());
            }
        } catch (Exception e) {
            logger.error("Error deleting track has artist", e);
            TrackHasArtistMessages.DeleteTrackHasArtistResponse deleteResponse =
                    TrackHasArtistMessages.DeleteTrackHasArtistResponse.newBuilder()
                            .setSuccess(false)
                            .setMessage("Error: " + e.getMessage())
                            .build();
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(deleteResponse.toByteString());
        }
    }
    }
