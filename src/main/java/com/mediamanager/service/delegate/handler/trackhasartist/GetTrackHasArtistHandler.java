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

import java.util.List;


@Action("trackhasartist.getAll")
public class GetTrackHasArtistHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(GetTrackHasArtistHandler.class);

    private final TrackHasArtistService trackHasArtistService;

    public GetTrackHasArtistHandler(TrackHasArtistService trackHasArtistService){this.trackHasArtistService = trackHasArtistService;}

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload) throws InvalidProtocolBufferException {
        try{
            List<TrackHasArtist> trackHasArtists = trackHasArtistService.getAllTrackHasArtists();
            TrackHasArtistMessages.GetTrackHasArtistsResponse.Builder responseBuilder = TrackHasArtistMessages.GetTrackHasArtistsResponse.newBuilder();

            for (TrackHasArtist trackHasArtist : trackHasArtists) {
                TrackHasArtistMessages.TrackHasArtist trackHasArtistProto = TrackHasArtistMapper.toProtobuf(trackHasArtist);
                responseBuilder.addTrackhasartist(trackHasArtistProto);
            }
            TrackHasArtistMessages.GetTrackHasArtistsResponse getTrackHasArtistsResponse = responseBuilder.build();

            return TransportProtocol.Response.newBuilder()
                    .setPayload(getTrackHasArtistsResponse.toByteString());

        }catch (Exception e){
            logger.error("Error getting track has artists", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(ByteString.copyFromUtf8("Error: " + e.getMessage()));
        }
    }
}
