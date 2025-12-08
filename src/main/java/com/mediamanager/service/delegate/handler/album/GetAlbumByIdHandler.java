package com.mediamanager.service.delegate.handler.album;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mediamanager.mapper.AlbumMapper;
import com.mediamanager.model.Album;
import com.mediamanager.protocol.TransportProtocol;
import com.mediamanager.protocol.messages.AlbumMessages;
import com.mediamanager.service.delegate.ActionHandler;
import com.mediamanager.service.delegate.annotation.Action;
import com.mediamanager.service.album.AlbumService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

@Action(value = "album.getById")
public class GetAlbumByIdHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(GetAlbumByIdHandler.class);
    private final AlbumService albumService;

    public GetAlbumByIdHandler(AlbumService albumService) {
        this.albumService = albumService;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload)
        throws InvalidProtocolBufferException{

        try{
            AlbumMessages.GetAlbumByIdRequest getByIdRequest =
                    AlbumMessages.GetAlbumByIdRequest.parseFrom(requestPayload);
            int id = getByIdRequest.getId();

            Optional<Album> albumOpt = albumService.getAlbumById(id);

            if (albumOpt.isEmpty()){
                logger.warn("Album not found with ID: {}", id);
                return TransportProtocol.Response.newBuilder()
                        .setStatusCode(404)
                        .setPayload(ByteString.copyFromUtf8("Album not found"));
            }
            AlbumMessages.Album albumProto = AlbumMapper.toProtobuf(albumOpt.get());
            AlbumMessages.GetAlbumByIdResponse getByIdResponse = AlbumMessages.GetAlbumByIdResponse.newBuilder()
                    .setAlbum(albumProto)
                    .build();
            return TransportProtocol.Response.newBuilder()
                    .setPayload(getByIdResponse.toByteString());
        } catch (Exception e) {
            logger.error("Error getting album by ID", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(ByteString.copyFromUtf8("Error: "+ e.getMessage()));
        }
    }
}
