package com.mediamanager.service.delegate.handler.trackhascomposer;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mediamanager.mapper.TrackHasComposerMapper;
import com.mediamanager.model.TrackHasComposer;
import com.mediamanager.protocol.TransportProtocol;
import com.mediamanager.protocol.messages.TrackHasComposerMessages;
import com.mediamanager.service.delegate.ActionHandler;
import com.mediamanager.service.delegate.annotation.Action;
import com.mediamanager.service.trackhascomposer.TrackHasComposerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Action("trackhascomposer.create")
public class CreateTrackHasComposerHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(CreateTrackHasComposerHandler.class);
    private final TrackHasComposerService trackHasComposerService;

    public CreateTrackHasComposerHandler(TrackHasComposerService trackHasComposerService) {
        this.trackHasComposerService = trackHasComposerService;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload) throws InvalidProtocolBufferException {
        try{
            TrackHasComposerMessages.CreateTrackHasComposerRequest createRequest =
                    TrackHasComposerMessages.CreateTrackHasComposerRequest.parseFrom(requestPayload);

            TrackHasComposer trackHasComposer = trackHasComposerService.createTrackHasComposer(
                    createRequest.getFkTrackId() > 0 ? createRequest.getFkTrackId() : null,
                    createRequest.getFkComposerId() > 0 ? createRequest.getFkComposerId() : null
            );

            TrackHasComposerMessages.TrackHasComposer trackHasComposerProto = TrackHasComposerMapper.toProtobuf(trackHasComposer);
            TrackHasComposerMessages.CreateTrackHasComposerResponse createTrackHasComposerResponse = TrackHasComposerMessages.CreateTrackHasComposerResponse.newBuilder()
                    .setTrackhascomposer(trackHasComposerProto)
                    .build();
            return TransportProtocol.Response.newBuilder()
                    .setPayload(createTrackHasComposerResponse.toByteString());
        } catch (IllegalArgumentException e) {
            logger.error("Validation error", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(400)
                    .setPayload(ByteString.copyFromUtf8("Validation error: " + e.getMessage()));

        } catch (Exception e) {
            logger.error("Error creating track has composer", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(ByteString.copyFromUtf8("Error: " + e.getMessage()));
        }
    }
}
