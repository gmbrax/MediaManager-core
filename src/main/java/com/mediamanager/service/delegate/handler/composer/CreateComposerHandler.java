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

@Action("composer.create")
public class CreateComposerHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(CreateComposerHandler.class);
    private final ComposerService composerService;

    public CreateComposerHandler(ComposerService composerService) {
        this.composerService = composerService;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload)
            throws InvalidProtocolBufferException {
        try{
            ComposerMessages.CreateComposerRequest CreateRequest = ComposerMessages.CreateComposerRequest
                    .parseFrom(requestPayload);
            Composer composer = composerService.createComposer(CreateRequest.getName());
            ComposerMessages.Composer composerProto = ComposerMapper.toProtobuf(composer);
            ComposerMessages.CreateComposerResponse createResponse = ComposerMessages.CreateComposerResponse
                    .newBuilder().setComposer(composerProto).build();
            return TransportProtocol.Response.newBuilder().setPayload(createResponse.toByteString());

        } catch (IllegalArgumentException e){
            logger.error("Validation error", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(400)
                    .setPayload(ByteString.copyFromUtf8("Validation error: " + e.getMessage()));

        } catch (Exception e){
            logger.error("Error creating composer", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(ByteString.copyFromUtf8("Error: " + e.getMessage()));
        }

    }
}
