package com.mediamanager.service.delegate.handler.albumart;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mediamanager.mapper.AlbumArtMapper;
import com.mediamanager.model.AlbumArt;
import com.mediamanager.protocol.TransportProtocol;
import com.mediamanager.protocol.messages.AlbumArtMessages;
import com.mediamanager.service.delegate.ActionHandler;
import com.mediamanager.service.delegate.annotation.Action;
import com.mediamanager.service.albumart.AlbumArtService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

@Action("albumart.update")
public class UpdateAlbumArtHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(UpdateAlbumArtHandler.class);
    private final AlbumArtService albumArtService;

    public UpdateAlbumArtHandler(AlbumArtService albumArtService) {
        this.albumArtService = albumArtService;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload) throws InvalidProtocolBufferException {
        try{
            AlbumArtMessages.UpdateAlbumArtRequest updateRequest =
                    AlbumArtMessages.UpdateAlbumArtRequest.parseFrom(requestPayload);

            int id = updateRequest.getId();
            String newFilepath = updateRequest.getFilepath();

            Optional<AlbumArt> albumArtOpt = albumArtService.updateAlbumArt(id, newFilepath);

            if(albumArtOpt.isEmpty()){
                logger.warn("AlbumArt not found with ID: {}", id);
                return TransportProtocol.Response.newBuilder()
                        .setStatusCode(404)
                        .setPayload(ByteString.copyFromUtf8("AlbumArt not found"));
            }

            AlbumArtMessages.AlbumArt albumArtProto = AlbumArtMapper.toProtobuf(albumArtOpt.get());

            AlbumArtMessages.UpdateAlbumArtResponse updateResponse = AlbumArtMessages.UpdateAlbumArtResponse.newBuilder()
                    .setAlbumart(albumArtProto)
                    .build();

            return TransportProtocol.Response.newBuilder()
                    .setPayload(updateResponse.toByteString());

        } catch (IllegalArgumentException e){
            logger.error("Validation error", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(400)
                    .setPayload(ByteString.copyFromUtf8("Validation error: " + e.getMessage()));
        } catch (Exception e) {
            logger.error("Error updating album art", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(ByteString.copyFromUtf8("Error: " + e.getMessage()));
        }
    }
}
