package com.mediamanager.service.delegate.handler.track;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mediamanager.mapper.TrackMapper;
import com.mediamanager.model.Track;
import com.mediamanager.protocol.TransportProtocol;
import com.mediamanager.protocol.messages.TrackMessages;
import com.mediamanager.service.delegate.ActionHandler;
import com.mediamanager.service.delegate.annotation.Action;
import com.mediamanager.service.track.TrackService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

@Action("track.update")
public class UpdateTrackHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(UpdateTrackHandler.class);
    private final TrackService trackService;

    public UpdateTrackHandler(TrackService trackService) {
        this.trackService = trackService;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload)
            throws InvalidProtocolBufferException {
        try {
            TrackMessages.UpdateTrackRequest updateRequest =
                    TrackMessages.UpdateTrackRequest.parseFrom(requestPayload);

            int id = updateRequest.getId();
            Integer trackNumber = updateRequest.hasTrackNumber() ? updateRequest.getTrackNumber().getValue() : null;
            String title = updateRequest.getTitle();
            Integer duration = updateRequest.hasDuration() ? updateRequest.getDuration().getValue() : null;
            String filepath = updateRequest.getFilepath();
            Integer discId = updateRequest.hasFkDiscId() ? updateRequest.getFkDiscId().getValue() : null;
            Integer composerId = updateRequest.hasFkComposerId() ? updateRequest.getFkComposerId().getValue() : null;
            Integer bitDepthId = updateRequest.hasFkBitdepthId() ? updateRequest.getFkBitdepthId().getValue() : null;
            Integer bitRateId = updateRequest.hasFkBitrateId() ? updateRequest.getFkBitrateId().getValue() : null;
            Integer samplingRateId = updateRequest.hasFkSamplingrateId() ? updateRequest.getFkSamplingrateId().getValue() : null;

            Optional<Track> trackOpt = trackService.updateTrack(
                    id,
                    trackNumber,
                    title,
                    duration,
                    filepath,
                    discId,
                    composerId,
                    bitDepthId,
                    bitRateId,
                    samplingRateId
            );

            if (trackOpt.isEmpty()) {
                logger.warn("Track not found with ID: {}", id);
                return TransportProtocol.Response.newBuilder()
                        .setStatusCode(404)
                        .setPayload(ByteString.copyFromUtf8("Track not found"));
            }

            TrackMessages.Track trackProto = TrackMapper.toProtobuf(trackOpt.get());

            TrackMessages.UpdateTrackResponse updateResponse =
                    TrackMessages.UpdateTrackResponse.newBuilder()
                            .setTrack(trackProto)
                            .build();

            return TransportProtocol.Response.newBuilder()
                    .setPayload(updateResponse.toByteString());

        } catch (IllegalArgumentException e) {
            logger.error("Validation error", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(400)
                    .setPayload(ByteString.copyFromUtf8("Validation error: " + e.getMessage()));
        } catch (Exception e) {
            logger.error("Error updating track", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(ByteString.copyFromUtf8("Error: " + e.getMessage()));
        }
    }
}
