package com.mediamanager.service.delegate.handler.trackhasgenre;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mediamanager.protocol.TransportProtocol;
import com.mediamanager.protocol.messages.TrackHasGenreMessages;
import com.mediamanager.service.delegate.ActionHandler;
import com.mediamanager.service.delegate.annotation.Action;
import com.mediamanager.service.trackhasgenre.TrackHasGenreService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Action("trackhasgenre.delete")
public class DeleteTrackHasGenreHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(DeleteTrackHasGenreHandler.class);

    private final TrackHasGenreService trackHasGenreService;

    public DeleteTrackHasGenreHandler(TrackHasGenreService trackHasGenreService) {
        this.trackHasGenreService = trackHasGenreService;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload)
            throws InvalidProtocolBufferException {

        try {
            TrackHasGenreMessages.DeleteTrackHasGenreRequest deleteRequest =
                    TrackHasGenreMessages.DeleteTrackHasGenreRequest.parseFrom(requestPayload);
            int id = deleteRequest.getId();
            boolean success = trackHasGenreService.deleteTrackHasGenre(id);
            TrackHasGenreMessages.DeleteTrackHasGenreResponse deleteResponse;
            if (success) {
                deleteResponse = TrackHasGenreMessages.DeleteTrackHasGenreResponse.newBuilder()
                        .setSuccess(true)
                        .setMessage("Track has genre deleted successfully")
                        .build();
                return TransportProtocol.Response.newBuilder()
                        .setPayload(deleteResponse.toByteString());
            } else {
                deleteResponse = TrackHasGenreMessages.DeleteTrackHasGenreResponse.newBuilder()
                        .setSuccess(false)
                        .setMessage("Track has genre not found")
                        .build();

                return TransportProtocol.Response.newBuilder()
                        .setStatusCode(404)
                        .setPayload(deleteResponse.toByteString());
            }
        } catch (Exception e) {
            logger.error("Error deleting track has genre", e);
            TrackHasGenreMessages.DeleteTrackHasGenreResponse deleteResponse =
                    TrackHasGenreMessages.DeleteTrackHasGenreResponse.newBuilder()
                            .setSuccess(false)
                            .setMessage("Error: " + e.getMessage())
                            .build();
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(deleteResponse.toByteString());
        }
    }
    }
