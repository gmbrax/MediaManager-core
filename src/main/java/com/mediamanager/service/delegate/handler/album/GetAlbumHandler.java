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

import java.util.List;


@Action("album.getAll")
public class GetAlbumHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(GetAlbumHandler.class);

    private final AlbumService albumService;

    public GetAlbumHandler(AlbumService albumService){this.albumService = albumService;}

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload) throws InvalidProtocolBufferException {
        try{
            List<Album> albums = albumService.getAllAlbums();
            AlbumMessages.GetAlbumsResponse.Builder responseBuilder = AlbumMessages.GetAlbumsResponse.newBuilder();

            for (Album album : albums) {
                AlbumMessages.Album albumProto = AlbumMapper.toProtobuf(album);
                responseBuilder.addAlbums(albumProto);
            }
            AlbumMessages.GetAlbumsResponse getAlbumsResponse = responseBuilder.build();

            return TransportProtocol.Response.newBuilder()
                    .setPayload(getAlbumsResponse.toByteString());

        }catch (Exception e){
            logger.error("Error getting albums", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(ByteString.copyFromUtf8("Error: " + e.getMessage()));
        }
    }
}
