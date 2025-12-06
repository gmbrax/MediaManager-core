package com.mediamanager.service.delegate.handler.artist;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mediamanager.mapper.ArtistMapper;
import com.mediamanager.model.Artist;
import com.mediamanager.protocol.TransportProtocol;
import com.mediamanager.protocol.messages.ArtistMessages;
import com.mediamanager.service.artist.ArtistService;
import com.mediamanager.service.delegate.ActionHandler;
import com.mediamanager.service.delegate.annotation.Action;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;


@Action("artist.getAll")
public class GetArtistHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(GetArtistHandler.class);

    private final ArtistService artistService;

    public GetArtistHandler(ArtistService artistService){this.artistService = artistService;}

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload) throws InvalidProtocolBufferException {
        try{
            List<Artist> artists = artistService.getAllArtists();
            ArtistMessages.GetArtistsResponse.Builder responseBuilder = ArtistMessages.GetArtistsResponse.newBuilder();

            for (Artist artist : artists) {
                ArtistMessages.Artist artistProto = ArtistMapper.toProtobuf(artist);
                responseBuilder.addArtists(artistProto);
            }
            ArtistMessages.GetArtistsResponse getArtistsResponse = responseBuilder.build();

            return TransportProtocol.Response.newBuilder()
                    .setPayload(getArtistsResponse.toByteString());

        }catch (Exception e){
            logger.error("Error getting artists", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(ByteString.copyFromUtf8("Error: " + e.getMessage()));
        }
    }
}
