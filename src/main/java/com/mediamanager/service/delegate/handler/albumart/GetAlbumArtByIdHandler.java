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

@Action(value = "albumart.getById")
public class GetAlbumArtByIdHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(GetAlbumArtByIdHandler.class);
    private final AlbumArtService albumArtService;

    public GetAlbumArtByIdHandler(AlbumArtService albumArtService) {
        this.albumArtService = albumArtService;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload)
        throws InvalidProtocolBufferException{

        try{
            AlbumArtMessages.GetAlbumArtByIdRequest getByIdRequest =
                    AlbumArtMessages.GetAlbumArtByIdRequest.parseFrom(requestPayload);
            int id = getByIdRequest.getId();

            Optional<AlbumArt> albumArtOpt = albumArtService.getAlbumArtById(id);

            if (albumArtOpt.isEmpty()){
                logger.warn("AlbumArt not found with ID: {}", id);
                return TransportProtocol.Response.newBuilder()
                        .setStatusCode(404)
                        .setPayload(ByteString.copyFromUtf8("AlbumArt not found"));
            }
            AlbumArtMessages.AlbumArt albumArtProto = AlbumArtMapper.toProtobuf(albumArtOpt.get());
            AlbumArtMessages.GetAlbumArtByIdResponse getByIdResponse = AlbumArtMessages.GetAlbumArtByIdResponse.newBuilder()
                    .setAlbumart(albumArtProto)
                    .build();
            return TransportProtocol.Response.newBuilder()
                    .setPayload(getByIdResponse.toByteString());
        } catch (Exception e) {
            logger.error("Error getting album art by ID", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(ByteString.copyFromUtf8("Error: "+ e.getMessage()));
        }
    }
}
