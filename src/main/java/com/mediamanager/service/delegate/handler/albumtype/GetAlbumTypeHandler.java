package com.mediamanager.service.delegate.handler.albumtype;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mediamanager.mapper.AlbumTypeMapper;
import com.mediamanager.model.AlbumType;
import com.mediamanager.protocol.TransportProtocol;
import com.mediamanager.protocol.messages.AlbumTypeMessages;
import com.mediamanager.service.delegate.ActionHandler;
import com.mediamanager.service.delegate.annotation.Action;
import com.mediamanager.service.albumtype.AlbumTypeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;


@Action("albumtype.getAll")
public class GetAlbumTypeHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(GetAlbumTypeHandler.class);

    private final AlbumTypeService albumTypeService;

    public GetAlbumTypeHandler(AlbumTypeService albumTypeService){this.albumTypeService = albumTypeService;}

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload) throws InvalidProtocolBufferException {
        try{
            List<AlbumType> albumTypes = albumTypeService.getAllAlbumTypes();
            AlbumTypeMessages.GetAlbumTypesResponse.Builder responseBuilder = AlbumTypeMessages.GetAlbumTypesResponse.newBuilder();

            for (AlbumType albumType : albumTypes) {
                AlbumTypeMessages.AlbumType albumTypeProto = AlbumTypeMapper.toProtobuf(albumType);
                responseBuilder.addAlbumtypes(albumTypeProto);
            }
            AlbumTypeMessages.GetAlbumTypesResponse getAlbumTypesResponse = responseBuilder.build();

            return TransportProtocol.Response.newBuilder()
                    .setPayload(getAlbumTypesResponse.toByteString());

        }catch (Exception e){
            logger.error("Error getting album types", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(ByteString.copyFromUtf8("Error: " + e.getMessage()));
        }
    }
}
