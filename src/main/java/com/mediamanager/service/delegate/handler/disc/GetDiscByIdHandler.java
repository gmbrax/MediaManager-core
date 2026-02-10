package com.mediamanager.service.delegate.handler.disc;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mediamanager.mapper.DiscMapper;
import com.mediamanager.model.Disc;
import com.mediamanager.protocol.TransportProtocol;
import com.mediamanager.protocol.messages.DiscMessages;
import com.mediamanager.service.delegate.ActionHandler;
import com.mediamanager.service.delegate.annotation.Action;
import com.mediamanager.service.disc.DiscService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

@Action(value = "disc.getById")
public class GetDiscByIdHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(GetDiscByIdHandler.class);
    private final DiscService discService;

    public GetDiscByIdHandler(DiscService discService) {
        this.discService = discService;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload)
        throws InvalidProtocolBufferException{

        try{
            DiscMessages.GetDiscByIdRequest getByIdRequest =
                    DiscMessages.GetDiscByIdRequest.parseFrom(requestPayload);
            int id = getByIdRequest.getId();

            Optional<Disc> discOpt = discService.getDiscById(id);

            if (discOpt.isEmpty()){
                logger.warn("Disc not found with ID: {}", id);
                return TransportProtocol.Response.newBuilder()
                        .setStatusCode(404)
                        .setPayload(ByteString.copyFromUtf8("Disc not found"));
            }
            DiscMessages.Disc discProto = DiscMapper.toProtobuf(discOpt.get());
            DiscMessages.GetDiscByIdResponse getByIdResponse = DiscMessages.GetDiscByIdResponse.newBuilder()
                    .setDisc(discProto)
                    .build();
            return TransportProtocol.Response.newBuilder()
                    .setPayload(getByIdResponse.toByteString());
        } catch (Exception e) {
            logger.error("Error getting disc by ID", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(ByteString.copyFromUtf8("Error: "+ e.getMessage()));
        }
    }
}
