package com.mediamanager.service.delegate.handler.track;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mediamanager.protocol.TransportProtocol;
import com.mediamanager.protocol.messages.TrackMessages;
import com.mediamanager.service.delegate.ActionHandler;
import com.mediamanager.service.delegate.annotation.Action;
import com.mediamanager.service.track.TrackService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Action("track.delete")
public class DeleteTrackHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(DeleteTrackHandler.class);

    private final TrackService trackService;

    public DeleteTrackHandler(TrackService trackService) {
        this.trackService = trackService;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload)
            throws InvalidProtocolBufferException {

        try {
            TrackMessages.DeleteTrackRequest deleteRequest =
                    TrackMessages.DeleteTrackRequest.parseFrom(requestPayload);
            int id = deleteRequest.getId();
            boolean success = trackService.deleteTrack(id);
            TrackMessages.DeleteTrackResponse deleteResponse;
            if (success) {
                deleteResponse = TrackMessages.DeleteTrackResponse.newBuilder()
                        .setSuccess(true)
                        .setMessage("Track deleted successfully")
                        .build();
                return TransportProtocol.Response.newBuilder()
                        .setPayload(deleteResponse.toByteString());
            } else {
                deleteResponse = TrackMessages.DeleteTrackResponse.newBuilder()
                        .setSuccess(false)
                        .setMessage("Track not found")
                        .build();

                return TransportProtocol.Response.newBuilder()
                        .setStatusCode(404)
                        .setPayload(deleteResponse.toByteString());
            }
        } catch (Exception e) {
            logger.error("Error deleting track", e);
            TrackMessages.DeleteTrackResponse deleteResponse =
                    TrackMessages.DeleteTrackResponse.newBuilder()
                            .setSuccess(false)
                            .setMessage("Error: " + e.getMessage())
                            .build();
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(deleteResponse.toByteString());
        }
    }
}
