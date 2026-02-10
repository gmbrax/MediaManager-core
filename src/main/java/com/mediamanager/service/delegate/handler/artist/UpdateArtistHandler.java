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

@Action("artist.update")
public class UpdateArtistHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(UpdateArtistHandler.class);
    private final ArtistService artistService;

    public UpdateArtistHandler(ArtistService artistService) {
        this.artistService = artistService;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload) throws InvalidProtocolBufferException {
        try{
            ArtistMessages.UpdateArtistRequest updateRequest =
                    ArtistMessages.UpdateArtistRequest.parseFrom(requestPayload);

            int id = updateRequest.getId();
            String newName = updateRequest.getName();

            Optional<Artist> artistOpt = artistService.updateArtist(id, newName);

            if(artistOpt.isEmpty()){
                logger.warn("Artist not found with ID: {}", id);
                return TransportProtocol.Response.newBuilder()
                        .setStatusCode(404)
                        .setPayload(ByteString.copyFromUtf8("Artist not found"));
            }

            ArtistMessages.Artist artistProto = ArtistMapper.toProtobuf(artistOpt.get());

            ArtistMessages.UpdateArtistResponse updateResponse = ArtistMessages.UpdateArtistResponse.newBuilder()
                    .setArtist(artistProto)
                    .build();

            return TransportProtocol.Response.newBuilder()
                    .setPayload(updateResponse.toByteString());

        } catch (IllegalArgumentException e){
            logger.error("Validation error", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(400)
                    .setPayload(ByteString.copyFromUtf8("Validation error: " + e.getMessage()));
        } catch (Exception e) {
            logger.error("Error updating artist", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(ByteString.copyFromUtf8("Error: " + e.getMessage()));
        }
    }
}
