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

@Action("track.create")
public class CreateTrackHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(CreateTrackHandler.class);
    private final TrackService trackService;

    public CreateTrackHandler(TrackService trackService) {
        this.trackService = trackService;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload) throws InvalidProtocolBufferException {
        try{
            TrackMessages.CreateTrackRequest createRequest =
                    TrackMessages.CreateTrackRequest.parseFrom(requestPayload);

            Track track = trackService.createTrack(
                    createRequest.getTrackNumber(),
                    createRequest.getTitle(),
                    createRequest.hasDuration() ? createRequest.getDuration().getValue() : null,
                    createRequest.getFilepath(),
                    createRequest.getFkDiscId() > 0 ? createRequest.getFkDiscId() : null,
                    createRequest.hasFkComposerId() ? createRequest.getFkComposerId().getValue() : null,
                    createRequest.hasFkBitdepthId() ? createRequest.getFkBitdepthId().getValue() : null,
                    createRequest.hasFkBitrateId() ? createRequest.getFkBitrateId().getValue() : null,
                    createRequest.hasFkSamplingrateId() ? createRequest.getFkSamplingrateId().getValue() : null
            );

            TrackMessages.Track trackProto = TrackMapper.toProtobuf(track);
            TrackMessages.CreateTrackResponse createTrackResponse = TrackMessages.CreateTrackResponse.newBuilder()
                    .setTrack(trackProto)
                    .build();
            return TransportProtocol.Response.newBuilder()
                    .setPayload(createTrackResponse.toByteString());
        } catch (IllegalArgumentException e) {
            logger.error("Validation error", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(400)
                    .setPayload(ByteString.copyFromUtf8("Validation error: " + e.getMessage()));

        } catch (Exception e) {
            logger.error("Error creating track", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(ByteString.copyFromUtf8("Error: " + e.getMessage()));
        }
    }
}
