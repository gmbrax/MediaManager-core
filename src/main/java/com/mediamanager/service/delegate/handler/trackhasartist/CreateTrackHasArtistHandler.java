package com.mediamanager.service.delegate.handler.trackhasartist;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mediamanager.mapper.TrackHasArtistMapper;
import com.mediamanager.model.TrackHasArtist;
import com.mediamanager.protocol.TransportProtocol;
import com.mediamanager.protocol.messages.TrackHasArtistMessages;
import com.mediamanager.service.delegate.ActionHandler;
import com.mediamanager.service.delegate.annotation.Action;
import com.mediamanager.service.trackhasartist.TrackHasArtistService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Action("trackhasartist.create")
public class CreateTrackHasArtistHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(CreateTrackHasArtistHandler.class);
    private final TrackHasArtistService trackHasArtistService;

    public CreateTrackHasArtistHandler(TrackHasArtistService trackHasArtistService) {
        this.trackHasArtistService = trackHasArtistService;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload) throws InvalidProtocolBufferException {
        try{
            TrackHasArtistMessages.CreateTrackHasArtistRequest createRequest =
                    TrackHasArtistMessages.CreateTrackHasArtistRequest.parseFrom(requestPayload);

            TrackHasArtist trackHasArtist = trackHasArtistService.createTrackHasArtist(
                    createRequest.getFkTrackId() > 0 ? createRequest.getFkTrackId() : null,
                    createRequest.getFkArtistId() > 0 ? createRequest.getFkArtistId() : null
            );

            TrackHasArtistMessages.TrackHasArtist trackHasArtistProto = TrackHasArtistMapper.toProtobuf(trackHasArtist);
            TrackHasArtistMessages.CreateTrackHasArtistResponse createTrackHasArtistResponse = TrackHasArtistMessages.CreateTrackHasArtistResponse.newBuilder()
                    .setTrackhasartist(trackHasArtistProto)
                    .build();
            return TransportProtocol.Response.newBuilder()
                    .setPayload(createTrackHasArtistResponse.toByteString());
        } catch (IllegalArgumentException e) {
            logger.error("Validation error", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(400)
                    .setPayload(ByteString.copyFromUtf8("Validation error: " + e.getMessage()));

        } catch (Exception e) {
            logger.error("Error creating track has artist", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(ByteString.copyFromUtf8("Error: " + e.getMessage()));
        }
    }
}
