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

@Action("albumtype.create")
public class CreateAlbumTypeHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(CreateAlbumTypeHandler.class);
    private final AlbumTypeService albumTypeService;

    public CreateAlbumTypeHandler(AlbumTypeService albumTypeService) {
        this.albumTypeService = albumTypeService;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload) throws InvalidProtocolBufferException {
        try{
            AlbumTypeMessages.CreateAlbumTypeRequest createRequest =
                    AlbumTypeMessages.CreateAlbumTypeRequest.parseFrom(requestPayload);
            AlbumType albumType = albumTypeService.createAlbumType(createRequest.getValue());
            AlbumTypeMessages.AlbumType albumTypeProto = AlbumTypeMapper.toProtobuf(albumType);
            AlbumTypeMessages.CreateAlbumTypeResponse createAlbumTypeResponse = AlbumTypeMessages.CreateAlbumTypeResponse.newBuilder()
                    .setAlbumtype(albumTypeProto)
                    .build();
            return TransportProtocol.Response.newBuilder()
                    .setPayload(createAlbumTypeResponse.toByteString());
        } catch (IllegalArgumentException e) {
            logger.error("Validation error", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(400)
                    .setPayload(ByteString.copyFromUtf8("Validation error: " + e.getMessage()));

        } catch (Exception e) {
            logger.error("Error creating album type", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(ByteString.copyFromUtf8("Error: " + e.getMessage()));
        }
    }
}
