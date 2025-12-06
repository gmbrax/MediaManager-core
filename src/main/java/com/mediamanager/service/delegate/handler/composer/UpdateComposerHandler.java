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

@Action("composer.update")
public class UpdateComposerHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(UpdateComposerHandler.class);
    private final ComposerService composerService;
    public UpdateComposerHandler(ComposerService composerService){
        this.composerService = composerService;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload)
            throws InvalidProtocolBufferException {
        try{
            ComposerMessages.UpdateComposerRequest updateRequest = ComposerMessages.UpdateComposerRequest
                    .parseFrom(requestPayload);
            int id = updateRequest.getId();
            String newName = updateRequest.getName();
            Optional<Composer> composerOpt = composerService.updateComposer(id, newName);
            if (composerOpt.isEmpty()) {
                logger.warn("Composer not found with ID: {}", id);
                return TransportProtocol.Response.newBuilder()
                        .setStatusCode(404)
                        .setPayload(ByteString.copyFromUtf8("Composer not found"));
            }
            ComposerMessages.Composer composerProto = ComposerMapper.toProtobuf(composerOpt.get());
            ComposerMessages.UpdateComposerResponse updateResponse = ComposerMessages.UpdateComposerResponse
                    .newBuilder().setComposer(composerProto).build();
            return TransportProtocol.Response.newBuilder().setPayload(updateResponse.toByteString());
        }catch (IllegalArgumentException e){
            logger.error("Validation error", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(400)
                    .setPayload(ByteString.copyFromUtf8("Validation error: " + e.getMessage()));
        } catch (Exception e) {
            logger.error("Error updating composer", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(ByteString.copyFromUtf8("Error: " + e.getMessage()));
        }
    }
}
