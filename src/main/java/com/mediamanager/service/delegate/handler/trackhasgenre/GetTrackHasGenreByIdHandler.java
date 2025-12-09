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

import java.util.Optional;

@Action(value = "trackhasgenre.getById")
public class GetTrackHasGenreByIdHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(GetTrackHasGenreByIdHandler.class);
    private final TrackHasGenreService trackHasGenreService;

    public GetTrackHasGenreByIdHandler(TrackHasGenreService trackHasGenreService) {
        this.trackHasGenreService = trackHasGenreService;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload)
        throws InvalidProtocolBufferException{

        try{
            TrackHasGenreMessages.GetTrackHasGenreByIdRequest getByIdRequest =
                    TrackHasGenreMessages.GetTrackHasGenreByIdRequest.parseFrom(requestPayload);
            int id = getByIdRequest.getId();

            Optional<TrackHasGenre> trackHasGenreOpt = trackHasGenreService.getTrackHasGenreById(id);

            if (trackHasGenreOpt.isEmpty()){
                logger.warn("TrackHasGenre not found with ID: {}", id);
                return TransportProtocol.Response.newBuilder()
                        .setStatusCode(404)
                        .setPayload(ByteString.copyFromUtf8("TrackHasGenre not found"));
            }
            TrackHasGenreMessages.TrackHasGenre trackHasGenreProto = TrackHasGenreMapper.toProtobuf(trackHasGenreOpt.get());
            TrackHasGenreMessages.GetTrackHasGenreByIdResponse getByIdResponse = TrackHasGenreMessages.GetTrackHasGenreByIdResponse.newBuilder()
                    .setTrackhasgenre(trackHasGenreProto)
                    .build();
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(200)
                    .setPayload(getByIdResponse.toByteString());
        } catch (Exception e) {
            logger.error("Error getting track has genre by ID", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(ByteString.copyFromUtf8("Error: "+ e.getMessage()));
        }
    }
}
