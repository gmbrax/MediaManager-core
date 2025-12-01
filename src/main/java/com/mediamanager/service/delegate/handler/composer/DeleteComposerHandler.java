package com.mediamanager.service.delegate.handler.composer;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mediamanager.protocol.TransportProtocol;
import com.mediamanager.protocol.messages.ComposerMessages;
import com.mediamanager.service.composer.ComposerService;
import com.mediamanager.service.delegate.ActionHandler;
import com.mediamanager.service.delegate.annotation.Action;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Action( "composer.delete")
public class DeleteComposerHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(DeleteComposerHandler.class);
    private final ComposerService composerService;


    public DeleteComposerHandler(ComposerService composerService){
        this.composerService = composerService;
    }
    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload)
            throws InvalidProtocolBufferException {
        try {
            ComposerMessages.DeleteComposerRequest deleteRequest =
                    ComposerMessages.DeleteComposerRequest.parseFrom(requestPayload);
            int id = deleteRequest.getId();
            boolean success = composerService.deleteComposer(id);
            ComposerMessages.DeleteComposerResponse deleteResponse;
            if(success){
                deleteResponse = ComposerMessages.DeleteComposerResponse.newBuilder().setSuccess(true)
                        .setMessage("Composer deleted successfully").build();


                return TransportProtocol.Response.newBuilder()
                        .setPayload(deleteResponse.toByteString());
            }else {
                deleteResponse = ComposerMessages.DeleteComposerResponse.newBuilder().setSuccess(false)
                        .setMessage("Composer not found").build();
                return TransportProtocol.Response.newBuilder().setStatusCode(404)
                        .setPayload(deleteResponse.toByteString());
            }

        } catch (Exception e) {
            logger.error("Error deleting composer", e);
            ComposerMessages.DeleteComposerResponse deleteResponse = ComposerMessages.DeleteComposerResponse
                    .newBuilder().setSuccess(false).setMessage("Error: " + e.getMessage()).build();
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500).setPayload(deleteResponse.toByteString());
        }
    }
}
