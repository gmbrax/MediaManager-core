package com.mediamanager.service.delegate.handler.trackhasgenre;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mediamanager.mapper.TrackHasGenreMapper;
import com.mediamanager.model.TrackHasGenre;
import com.mediamanager.protocol.TransportProtocol;
import com.mediamanager.protocol.messages.TrackHasGenreMessages;
import com.mediamanager.service.delegate.ActionHandler;
import com.mediamanager.service.delegate.annotation.Action;
import com.mediamanager.service.trackhasgenre.TrackHasGenreService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Action("trackhasgenre.create")
public class CreateTrackHasGenreHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(CreateTrackHasGenreHandler.class);
    private final TrackHasGenreService trackHasGenreService;

    public CreateTrackHasGenreHandler(TrackHasGenreService trackHasGenreService) {
        this.trackHasGenreService = trackHasGenreService;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload) throws InvalidProtocolBufferException {
        try{
            TrackHasGenreMessages.CreateTrackHasGenreRequest createRequest =
                    TrackHasGenreMessages.CreateTrackHasGenreRequest.parseFrom(requestPayload);

            TrackHasGenre trackHasGenre = trackHasGenreService.createTrackHasGenre(
                    createRequest.getFkTrackId() > 0 ? createRequest.getFkTrackId() : null,
                    createRequest.getFkGenreId() > 0 ? createRequest.getFkGenreId() : null
            );

            TrackHasGenreMessages.TrackHasGenre trackHasGenreProto = TrackHasGenreMapper.toProtobuf(trackHasGenre);
            TrackHasGenreMessages.CreateTrackHasGenreResponse createTrackHasGenreResponse = TrackHasGenreMessages.CreateTrackHasGenreResponse.newBuilder()
                    .setTrackhasgenre(trackHasGenreProto)
                    .build();
            return TransportProtocol.Response.newBuilder()
                    .setPayload(createTrackHasGenreResponse.toByteString());
        } catch (IllegalArgumentException e) {
            logger.error("Validation error", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(400)
                    .setPayload(ByteString.copyFromUtf8("Validation error: " + e.getMessage()));

        } catch (Exception e) {
            logger.error("Error creating track has genre", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(ByteString.copyFromUtf8("Error: " + e.getMessage()));
        }
    }
}
