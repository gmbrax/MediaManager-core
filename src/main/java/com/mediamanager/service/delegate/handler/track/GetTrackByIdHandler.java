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

@Action(value = "track.getById")
public class GetTrackByIdHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(GetTrackByIdHandler.class);
    private final TrackService trackService;

    public GetTrackByIdHandler(TrackService trackService) {
        this.trackService = trackService;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload)
        throws InvalidProtocolBufferException{

        try{
            TrackMessages.GetTrackByIdRequest getByIdRequest =
                    TrackMessages.GetTrackByIdRequest.parseFrom(requestPayload);
            int id = getByIdRequest.getId();

            Optional<Track> trackOpt = trackService.getTrackById(id);

            if (trackOpt.isEmpty()){
                logger.warn("Track not found with ID: {}", id);
                return TransportProtocol.Response.newBuilder()
                        .setStatusCode(404)
                        .setPayload(ByteString.copyFromUtf8("Track not found"));
            }
            TrackMessages.Track trackProto = TrackMapper.toProtobuf(trackOpt.get());
            TrackMessages.GetTrackByIdResponse getByIdResponse = TrackMessages.GetTrackByIdResponse.newBuilder()
                    .setTrack(trackProto)
                    .build();
            return TransportProtocol.Response.newBuilder()
                    .setPayload(getByIdResponse.toByteString());
        } catch (Exception e) {
            logger.error("Error getting track by ID", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(ByteString.copyFromUtf8("Error: "+ e.getMessage()));
        }
    }
}
