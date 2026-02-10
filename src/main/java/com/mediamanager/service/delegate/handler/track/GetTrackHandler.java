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

import java.util.List;

@Action("track.getAll")
public class GetTrackHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(GetTrackHandler.class);

    private final TrackService trackService;

    public GetTrackHandler(TrackService trackService){this.trackService = trackService;}

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload) throws InvalidProtocolBufferException {
        try{
            List<Track> tracks = trackService.getAllTracks();
            TrackMessages.GetTracksResponse.Builder responseBuilder = TrackMessages.GetTracksResponse.newBuilder();

            for (Track track : tracks) {
                TrackMessages.Track trackProto = TrackMapper.toProtobuf(track);
                responseBuilder.addTracks(trackProto);
            }
            TrackMessages.GetTracksResponse getTracksResponse = responseBuilder.build();

            return TransportProtocol.Response.newBuilder()
                    .setPayload(getTracksResponse.toByteString());

        }catch (Exception e){
            logger.error("Error getting tracks", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(ByteString.copyFromUtf8("Error: " + e.getMessage()));
        }
    }
}
