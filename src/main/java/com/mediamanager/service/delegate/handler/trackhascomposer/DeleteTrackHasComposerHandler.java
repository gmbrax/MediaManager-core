package com.mediamanager.service.delegate.handler.trackhascomposer;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mediamanager.protocol.TransportProtocol;
import com.mediamanager.protocol.messages.TrackHasComposerMessages;
import com.mediamanager.service.delegate.ActionHandler;
import com.mediamanager.service.delegate.annotation.Action;
import com.mediamanager.service.trackhascomposer.TrackHasComposerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Action("trackhascomposer.delete")
public class DeleteTrackHasComposerHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(DeleteTrackHasComposerHandler.class);

    private final TrackHasComposerService trackHasComposerService;

    public DeleteTrackHasComposerHandler(TrackHasComposerService trackHasComposerService) {
        this.trackHasComposerService = trackHasComposerService;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload)
            throws InvalidProtocolBufferException {

        try {
            TrackHasComposerMessages.DeleteTrackHasComposerRequest deleteRequest =
                    TrackHasComposerMessages.DeleteTrackHasComposerRequest.parseFrom(requestPayload);
            int id = deleteRequest.getId();
            boolean success = trackHasComposerService.deleteTrackHasComposer(id);
            TrackHasComposerMessages.DeleteTrackHasComposerResponse deleteResponse;
            if (success) {
                deleteResponse = TrackHasComposerMessages.DeleteTrackHasComposerResponse.newBuilder()
                        .setSuccess(true)
                        .setMessage("Track has composer deleted successfully")
                        .build();
                return TransportProtocol.Response.newBuilder()
                        .setPayload(deleteResponse.toByteString());
            } else {
                deleteResponse = TrackHasComposerMessages.DeleteTrackHasComposerResponse.newBuilder()
                        .setSuccess(false)
                        .setMessage("Track has composer not found")
                        .build();

                return TransportProtocol.Response.newBuilder()
                        .setStatusCode(404)
                        .setPayload(deleteResponse.toByteString());
            }
        } catch (Exception e) {
            logger.error("Error deleting track has composer", e);
            TrackHasComposerMessages.DeleteTrackHasComposerResponse deleteResponse =
                    TrackHasComposerMessages.DeleteTrackHasComposerResponse.newBuilder()
                            .setSuccess(false)
                            .setMessage("Error: " + e.getMessage())
                            .build();
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(deleteResponse.toByteString());
        }
    }
