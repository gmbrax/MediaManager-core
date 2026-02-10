package com.mediamanager.service.delegate.handler.composer;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mediamanager.mapper.ComposerMapper;
import com.mediamanager.model.Composer;
import com.mediamanager.protocol.TransportProtocol;
import com.mediamanager.protocol.messages.ComposerMessages;
import com.mediamanager.protocol.messages.GenreMessages;
import com.mediamanager.service.composer.ComposerService;
import com.mediamanager.service.delegate.ActionHandler;
import com.mediamanager.service.delegate.annotation.Action;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@Action( "composer.getAll")
public class GetComposerHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(GetComposerHandler.class);
    private final ComposerService composerService;

    public GetComposerHandler(ComposerService composerService){
        this.composerService = composerService;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload)
            throws InvalidProtocolBufferException {

        try{
            List<Composer> composers = composerService.getAllComposers();

            ComposerMessages.GetComposersResponse.Builder responseBuilder = ComposerMessages.GetComposersResponse.newBuilder();
            for(Composer composer : composers){
                ComposerMessages.Composer composerProto = ComposerMapper.toProtobuf(composer);
                responseBuilder.addComposers(composerProto);
            }
            ComposerMessages.GetComposersResponse getGenresResponse = responseBuilder.build();
            return TransportProtocol.Response.newBuilder().setPayload(getGenresResponse.toByteString());
        } catch (Exception e) {
            logger.error("Error getting composers", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(ByteString.copyFromUtf8("Error: " + e.getMessage()));
        }

    }

}
