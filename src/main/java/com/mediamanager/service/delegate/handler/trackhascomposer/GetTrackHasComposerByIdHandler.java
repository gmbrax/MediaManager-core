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

import java.util.Optional;

@Action(value = "trackhascomposer.getById")
public class GetTrackHasComposerByIdHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(GetTrackHasComposerByIdHandler.class);
    private final TrackHasComposerService trackHasComposerService;

    public GetTrackHasComposerByIdHandler(TrackHasComposerService trackHasComposerService) {
        this.trackHasComposerService = trackHasComposerService;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload)
        throws InvalidProtocolBufferException{

        try{
            TrackHasComposerMessages.GetTrackHasComposerByIdRequest getByIdRequest =
                    TrackHasComposerMessages.GetTrackHasComposerByIdRequest.parseFrom(requestPayload);
            int id = getByIdRequest.getId();

            Optional<TrackHasComposer> trackHasComposerOpt = trackHasComposerService.getTrackHasComposerById(id);

            if (trackHasComposerOpt.isEmpty()){
                logger.warn("TrackHasComposer not found with ID: {}", id);
                return TransportProtocol.Response.newBuilder()
                        .setStatusCode(404)
                        .setPayload(ByteString.copyFromUtf8("TrackHasComposer not found"));
            }
            TrackHasComposerMessages.TrackHasComposer trackHasComposerProto = TrackHasComposerMapper.toProtobuf(trackHasComposerOpt.get());
            TrackHasComposerMessages.GetTrackHasComposerByIdResponse getByIdResponse = TrackHasComposerMessages.GetTrackHasComposerByIdResponse.newBuilder()
                    .setTrackhascomposer(trackHasComposerProto)
                    .build();
            return TransportProtocol.Response.newBuilder()
                    .setPayload(getByIdResponse.toByteString());
        } catch (Exception e) {
            logger.error("Error getting track has composer by ID", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(ByteString.copyFromUtf8("Error: "+ e.getMessage()));
        }
    }
}
