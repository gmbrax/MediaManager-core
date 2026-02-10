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

import java.util.List;


@Action("trackhascomposer.getAll")
public class GetTrackHasComposerHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(GetTrackHasComposerHandler.class);

    private final TrackHasComposerService trackHasComposerService;

    public GetTrackHasComposerHandler(TrackHasComposerService trackHasComposerService){this.trackHasComposerService = trackHasComposerService;}

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload) throws InvalidProtocolBufferException {
        try{
            List<TrackHasComposer> trackHasComposers = trackHasComposerService.getAllTrackHasComposers();
            TrackHasComposerMessages.GetTrackHasComposersResponse.Builder responseBuilder = TrackHasComposerMessages.GetTrackHasComposersResponse.newBuilder();

            for (TrackHasComposer trackHasComposer : trackHasComposers) {
                TrackHasComposerMessages.TrackHasComposer trackHasComposerProto = TrackHasComposerMapper.toProtobuf(trackHasComposer);
                responseBuilder.addTrackhascomposer(trackHasComposerProto);
            }
            TrackHasComposerMessages.GetTrackHasComposersResponse getTrackHasComposersResponse = responseBuilder.build();

            return TransportProtocol.Response.newBuilder()
                    .setPayload(getTrackHasComposersResponse.toByteString());

        }catch (Exception e){
            logger.error("Error getting track has composers", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(ByteString.copyFromUtf8("Error: " + e.getMessage()));
        }
    }
}
