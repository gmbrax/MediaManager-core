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

@Action("albumtype.update")
public class UpdateAlbumTypeHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(UpdateAlbumTypeHandler.class);
    private final AlbumTypeService albumTypeService;

    public UpdateAlbumTypeHandler(AlbumTypeService albumTypeService) {
        this.albumTypeService = albumTypeService;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload) throws InvalidProtocolBufferException {
        try{
            AlbumTypeMessages.UpdateAlbumTypeRequest updateRequest =
                    AlbumTypeMessages.UpdateAlbumTypeRequest.parseFrom(requestPayload);

            int id = updateRequest.getId();
            String newValue = updateRequest.getValue();

            Optional<AlbumType> albumTypeOpt = albumTypeService.updateAlbumType(id, newValue);

            if(albumTypeOpt.isEmpty()){
                logger.warn("AlbumType not found with ID: {}", id);
                return TransportProtocol.Response.newBuilder()
                        .setStatusCode(404)
                        .setPayload(ByteString.copyFromUtf8("AlbumType not found"));
            }

            AlbumTypeMessages.AlbumType albumTypeProto = AlbumTypeMapper.toProtobuf(albumTypeOpt.get());

            AlbumTypeMessages.UpdateAlbumTypeResponse updateResponse = AlbumTypeMessages.UpdateAlbumTypeResponse.newBuilder()
                    .setAlbumtype(albumTypeProto)
                    .build();

            return TransportProtocol.Response.newBuilder()
                    .setPayload(updateResponse.toByteString());

        } catch (IllegalArgumentException e){
            logger.error("Validation error", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(400)
                    .setPayload(ByteString.copyFromUtf8("Validation error: " + e.getMessage()));
        } catch (Exception e) {
            logger.error("Error updating album type", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(ByteString.copyFromUtf8("Error: " + e.getMessage()));
        }
    }
}
