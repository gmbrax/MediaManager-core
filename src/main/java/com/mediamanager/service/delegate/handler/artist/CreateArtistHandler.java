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

@Action("artist.create")
public class CreateArtistHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(CreateArtistHandler.class);
    private final ArtistService artistService;

    public CreateArtistHandler(ArtistService artistService) {
        this.artistService = artistService;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload) throws InvalidProtocolBufferException {
        try{
            ArtistMessages.CreateArtistRequest createRequest =
                    ArtistMessages.CreateArtistRequest.parseFrom(requestPayload);
            Artist artist = artistService.createArtist(createRequest.getName());
            ArtistMessages.Artist artistProto = ArtistMapper.toProtobuf(artist);
            ArtistMessages.CreateArtistResponse createArtistResponse = ArtistMessages.CreateArtistResponse.newBuilder()
                    .setArtist(artistProto)
                    .build();
            return TransportProtocol.Response.newBuilder()
                    .setPayload(createArtistResponse.toByteString());
        } catch (IllegalArgumentException e) {
            logger.error("Validation error", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(400)
                    .setPayload(ByteString.copyFromUtf8("Validation error: " + e.getMessage()));

        } catch (Exception e) {
            logger.error("Error creating artist", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(ByteString.copyFromUtf8("Error: " + e.getMessage()));
        }
    }
}

