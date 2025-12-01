package com.mediamanager.service.delegate.handler.composer;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mediamanager.mapper.ComposerMapper;
import com.mediamanager.model.Composer;
import com.mediamanager.protocol.TransportProtocol;
import com.mediamanager.protocol.messages.ComposerMessages;
import com.mediamanager.service.composer.ComposerService;
import com.mediamanager.service.delegate.ActionHandler;
import com.mediamanager.service.delegate.annotation.Action;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

@Action( "composer.getById")
public class GetComposerByIdHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(GetComposerByIdHandler.class);
    private final ComposerService composerService;

    public GetComposerByIdHandler(ComposerService composerService){
        this.composerService = composerService;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload)
            throws InvalidProtocolBufferException {
        try{

            ComposerMessages.GetComposerByIdRequest getByIdRequest = ComposerMessages.GetComposerByIdRequest
                    .parseFrom(requestPayload);
            int id = getByIdRequest.getId();

            Optional<Composer> composerOpt = composerService.getComposerById(id);

            if (composerOpt.isEmpty()) {
              logger.warn("Composer not found with ID: {}", id);
              return TransportProtocol.Response.newBuilder()
                      .setStatusCode(404)
                      .setPayload(ByteString.copyFromUtf8("Composer not found"));
            }

            ComposerMessages.Composer composerProto = ComposerMapper.toProtobuf(composerOpt.get());

            ComposerMessages.GetComposerByIdResponse getByIdResponse = ComposerMessages.GetComposerByIdResponse
                    .newBuilder().setComposer(composerProto).build();

            return TransportProtocol.Response.newBuilder().setPayload(getByIdResponse.toByteString());
        } catch (Exception e) {
            logger.error("Error getting composer by ID", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(ByteString.copyFromUtf8("Error: " + e.getMessage()));
        }
    }
}
