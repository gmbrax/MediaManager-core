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

import java.util.Optional;

@Action(value = "trackhasartist.getById")
public class GetTrackHasArtistByIdHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(GetTrackHasArtistByIdHandler.class);
    private final TrackHasArtistService trackHasArtistService;

    public GetTrackHasArtistByIdHandler(TrackHasArtistService trackHasArtistService) {
        this.trackHasArtistService = trackHasArtistService;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload)
        throws InvalidProtocolBufferException{

        try{
            TrackHasArtistMessages.GetTrackHasArtistByIdRequest getByIdRequest =
                    TrackHasArtistMessages.GetTrackHasArtistByIdRequest.parseFrom(requestPayload);
            int id = getByIdRequest.getId();

            Optional<TrackHasArtist> trackHasArtistOpt = trackHasArtistService.getTrackHasArtistById(id);

            if (trackHasArtistOpt.isEmpty()){
                logger.warn("TrackHasArtist not found with ID: {}", id);
                return TransportProtocol.Response.newBuilder()
                        .setStatusCode(404)
                        .setPayload(ByteString.copyFromUtf8("TrackHasArtist not found"));
            }
            TrackHasArtistMessages.TrackHasArtist trackHasArtistProto = TrackHasArtistMapper.toProtobuf(trackHasArtistOpt.get());
            TrackHasArtistMessages.GetTrackHasArtistByIdResponse getByIdResponse = TrackHasArtistMessages.GetTrackHasArtistByIdResponse.newBuilder()
                    .setTrackhasartist(trackHasArtistProto)
                    .build();
            return TransportProtocol.Response.newBuilder()
                    .setPayload(getByIdResponse.toByteString());
        } catch (Exception e) {
            logger.error("Error getting track has artist by ID", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(ByteString.copyFromUtf8("Error: "+ e.getMessage()));
        }
    }
}
