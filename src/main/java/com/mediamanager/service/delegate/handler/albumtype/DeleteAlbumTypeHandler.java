package com.mediamanager.service.delegate.handler.albumtype;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mediamanager.protocol.TransportProtocol;
import com.mediamanager.protocol.messages.AlbumTypeMessages;
import com.mediamanager.service.delegate.ActionHandler;
import com.mediamanager.service.delegate.annotation.Action;
import com.mediamanager.service.albumtype.AlbumTypeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Action("albumtype.delete")
public class DeleteAlbumTypeHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(DeleteAlbumTypeHandler.class);

    private final AlbumTypeService albumTypeService;

    public DeleteAlbumTypeHandler(AlbumTypeService albumTypeService) {
        this.albumTypeService = albumTypeService;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload)
            throws InvalidProtocolBufferException {

        try {
            AlbumTypeMessages.DeleteAlbumTypeRequest deleteRequest =
                    AlbumTypeMessages.DeleteAlbumTypeRequest.parseFrom(requestPayload);
            int id = deleteRequest.getId();
            boolean success = albumTypeService.deleteAlbumType(id);
            AlbumTypeMessages.DeleteAlbumTypeResponse deleteResponse;
            if (success) {
                deleteResponse = AlbumTypeMessages.DeleteAlbumTypeResponse.newBuilder()
                        .setSuccess(true)
                        .setMessage("Album type deleted successfully")
                        .build();
                return TransportProtocol.Response.newBuilder()
                        .setPayload(deleteResponse.toByteString());
            } else {
                deleteResponse = AlbumTypeMessages.DeleteAlbumTypeResponse.newBuilder()
                        .setSuccess(false)
                        .setMessage("Album type not found")
                        .build();

                return TransportProtocol.Response.newBuilder()
                        .setStatusCode(404)
                        .setPayload(deleteResponse.toByteString());
            }
        } catch (Exception e) {
            logger.error("Error deleting album type", e);
            AlbumTypeMessages.DeleteAlbumTypeResponse deleteResponse =
                    AlbumTypeMessages.DeleteAlbumTypeResponse.newBuilder()
                            .setSuccess(false)
                            .setMessage("Error: " + e.getMessage())
                            .build();
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(deleteResponse.toByteString());
        }
    }
    }
