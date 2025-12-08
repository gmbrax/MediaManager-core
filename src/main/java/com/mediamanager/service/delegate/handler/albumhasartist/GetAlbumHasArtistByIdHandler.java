package com.mediamanager.service.delegate.handler.albumhasartist;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mediamanager.mapper.AlbumHasArtistMapper;
import com.mediamanager.model.AlbumHasArtist;
import com.mediamanager.protocol.TransportProtocol;
import com.mediamanager.protocol.messages.AlbumHasArtistMessages;
import com.mediamanager.service.delegate.ActionHandler;
import com.mediamanager.service.delegate.annotation.Action;
import com.mediamanager.service.albumhasartist.AlbumHasArtistService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

@Action(value = "albumhasartist.getById")
public class GetAlbumHasArtistByIdHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(GetAlbumHasArtistByIdHandler.class);
    private final AlbumHasArtistService albumHasArtistService;

    public GetAlbumHasArtistByIdHandler(AlbumHasArtistService albumHasArtistService) {
        this.albumHasArtistService = albumHasArtistService;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload)
        throws InvalidProtocolBufferException{

        try{
            AlbumHasArtistMessages.GetAlbumHasArtistByIdRequest getByIdRequest =
                    AlbumHasArtistMessages.GetAlbumHasArtistByIdRequest.parseFrom(requestPayload);
            int id = getByIdRequest.getId();

            Optional<AlbumHasArtist> albumHasArtistOpt = albumHasArtistService.getAlbumHasArtistById(id);

            if (albumHasArtistOpt.isEmpty()){
                logger.warn("AlbumHasArtist not found with ID: {}", id);
                return TransportProtocol.Response.newBuilder()
                        .setStatusCode(404)
                        .setPayload(ByteString.copyFromUtf8("AlbumHasArtist not found"));
            }
            AlbumHasArtistMessages.AlbumHasArtist albumHasArtistProto = AlbumHasArtistMapper.toProtobuf(albumHasArtistOpt.get());
            AlbumHasArtistMessages.GetAlbumHasArtistByIdResponse getByIdResponse = AlbumHasArtistMessages.GetAlbumHasArtistByIdResponse.newBuilder()
                    .setAlbumhasartist(albumHasArtistProto)
                    .build();
            return TransportProtocol.Response.newBuilder()
                    .setPayload(getByIdResponse.toByteString());
        } catch (Exception e) {
            logger.error("Error getting album has artist by ID", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(ByteString.copyFromUtf8("Error: "+ e.getMessage()));
        }
    }
}
