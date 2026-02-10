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

import java.util.List;


@Action("albumhasartist.getAll")
public class GetAlbumHasArtistHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(GetAlbumHasArtistHandler.class);

    private final AlbumHasArtistService albumHasArtistService;

    public GetAlbumHasArtistHandler(AlbumHasArtistService albumHasArtistService){this.albumHasArtistService = albumHasArtistService;}

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload) throws InvalidProtocolBufferException {
        try{
            List<AlbumHasArtist> albumHasArtists = albumHasArtistService.getAllAlbumHasArtists();
            AlbumHasArtistMessages.GetAlbumHasArtistsResponse.Builder responseBuilder = AlbumHasArtistMessages.GetAlbumHasArtistsResponse.newBuilder();

            for (AlbumHasArtist albumHasArtist : albumHasArtists) {
                AlbumHasArtistMessages.AlbumHasArtist albumHasArtistProto = AlbumHasArtistMapper.toProtobuf(albumHasArtist);
                responseBuilder.addAlbumhasartist(albumHasArtistProto);
            }
            AlbumHasArtistMessages.GetAlbumHasArtistsResponse getAlbumHasArtistsResponse = responseBuilder.build();

            return TransportProtocol.Response.newBuilder()
                    .setPayload(getAlbumHasArtistsResponse.toByteString());

        }catch (Exception e){
            logger.error("Error getting album has artists", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(ByteString.copyFromUtf8("Error: " + e.getMessage()));
        }
    }
}
