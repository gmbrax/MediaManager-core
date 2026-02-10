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

import java.util.List;


@Action("trackhasgenre.getAll")
public class GetTrackHasGenreHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(GetTrackHasGenreHandler.class);

    private final TrackHasGenreService trackHasGenreService;

    public GetTrackHasGenreHandler(TrackHasGenreService trackHasGenreService){this.trackHasGenreService = trackHasGenreService;}

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload) throws InvalidProtocolBufferException {
        try{
            List<TrackHasGenre> trackHasGenres = trackHasGenreService.getAllTrackHasGenres();
            TrackHasGenreMessages.GetTrackHasGenresResponse.Builder responseBuilder = TrackHasGenreMessages.GetTrackHasGenresResponse.newBuilder();

            for (TrackHasGenre trackHasGenre : trackHasGenres) {
                TrackHasGenreMessages.TrackHasGenre trackHasGenreProto = TrackHasGenreMapper.toProtobuf(trackHasGenre);
                responseBuilder.addTrackhasgenre(trackHasGenreProto);
            }
            TrackHasGenreMessages.GetTrackHasGenresResponse getTrackHasGenresResponse = responseBuilder.build();

            return TransportProtocol.Response.newBuilder()
                    .setPayload(getTrackHasGenresResponse.toByteString());

        }catch (Exception e){
            logger.error("Error getting track has genres", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(ByteString.copyFromUtf8("Error: " + e.getMessage()));
        }
    }
}
