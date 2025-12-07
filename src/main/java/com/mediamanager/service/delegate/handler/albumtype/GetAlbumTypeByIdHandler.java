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

import java.util.Optional;

@Action(value = "albumtype.getById")
public class GetAlbumTypeByIdHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(GetAlbumTypeByIdHandler.class);
    private final AlbumTypeService albumTypeService;

    public GetAlbumTypeByIdHandler(AlbumTypeService albumTypeService) {
        this.albumTypeService = albumTypeService;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload)
        throws InvalidProtocolBufferException{

        try{
            AlbumTypeMessages.GetAlbumTypeByIdRequest getByIdRequest =
                    AlbumTypeMessages.GetAlbumTypeByIdRequest.parseFrom(requestPayload);
            int id = getByIdRequest.getId();

            Optional<AlbumType> albumTypeOpt = albumTypeService.getAlbumTypeById(id);

            if (albumTypeOpt.isEmpty()){
                logger.warn("AlbumType not found with ID: {}", id);
                return TransportProtocol.Response.newBuilder()
                        .setStatusCode(404)
                        .setPayload(ByteString.copyFromUtf8("AlbumType not found"));
            }
            AlbumTypeMessages.AlbumType albumTypeProto = AlbumTypeMapper.toProtobuf(albumTypeOpt.get());
            AlbumTypeMessages.GetAlbumTypeByIdResponse getByIdResponse = AlbumTypeMessages.GetAlbumTypeByIdResponse.newBuilder()
                    .setAlbumtype(albumTypeProto)
                    .build();
            return TransportProtocol.Response.newBuilder()
                    .setPayload(getByIdResponse.toByteString());
        } catch (Exception e) {
            logger.error("Error getting album type by ID", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(ByteString.copyFromUtf8("Error: "+ e.getMessage()));
        }
    }
}
