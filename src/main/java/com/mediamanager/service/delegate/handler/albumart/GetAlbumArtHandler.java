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

import java.util.List;


@Action("albumart.getAll")
public class GetAlbumArtHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(GetAlbumArtHandler.class);

    private final AlbumArtService albumArtService;

    public GetAlbumArtHandler(AlbumArtService albumArtService){this.albumArtService = albumArtService;}

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload) throws InvalidProtocolBufferException {
        try{
            List<AlbumArt> albumArts = albumArtService.getAllAlbumArts();
            AlbumArtMessages.GetAlbumArtsResponse.Builder responseBuilder = AlbumArtMessages.GetAlbumArtsResponse.newBuilder();

            for (AlbumArt albumArt : albumArts) {
                AlbumArtMessages.AlbumArt albumArtProto = AlbumArtMapper.toProtobuf(albumArt);
                responseBuilder.addAlbumarts(albumArtProto);
            }
            AlbumArtMessages.GetAlbumArtsResponse getAlbumArtsResponse = responseBuilder.build();

            return TransportProtocol.Response.newBuilder()
                    .setPayload(getAlbumArtsResponse.toByteString());

        }catch (Exception e){
            logger.error("Error getting album arts", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(ByteString.copyFromUtf8("Error: " + e.getMessage()));
        }
    }
}
