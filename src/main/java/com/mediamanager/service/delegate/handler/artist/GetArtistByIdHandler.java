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

import java.util.Optional;

@Action(value = "artist.getById")
public class GetArtistByIdHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(GetArtistByIdHandler.class);
    private final ArtistService artistService;

    public GetArtistByIdHandler(ArtistService artistService) {
        this.artistService = artistService;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload)
        throws InvalidProtocolBufferException{

        try{
            ArtistMessages.GetArtistByIdRequest getByIdRequest =
                    ArtistMessages.GetArtistByIdRequest.parseFrom(requestPayload);
            int id = getByIdRequest.getId();

            Optional<Artist> artistOpt = artistService.getArtistById(id);

            if (artistOpt.isEmpty()){
                logger.warn("Artist not found with ID: {}", id);
                return TransportProtocol.Response.newBuilder()
                        .setStatusCode(404)
                        .setPayload(ByteString.copyFromUtf8("Artist not found"));
            }
            ArtistMessages.Artist artistProto = ArtistMapper.toProtobuf(artistOpt.get());
            ArtistMessages.GetArtistByIdResponse getByIdResponse = ArtistMessages.GetArtistByIdResponse.newBuilder()
                    .setArtist(artistProto)
                    .build();
            return TransportProtocol.Response.newBuilder()
                    .setPayload(getByIdResponse.toByteString());
        } catch (Exception e) {
            logger.error("Error getting artist by ID", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(ByteString.copyFromUtf8("Error: "+ e.getMessage()));
        }
    }
}
