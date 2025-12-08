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

import java.util.List;

@Action("disc.getAll")
public class GetDiscHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(GetDiscHandler.class);

    private final DiscService discService;

    public GetDiscHandler(DiscService discService){this.discService = discService;}

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload) throws InvalidProtocolBufferException {
        try{
            List<Disc> discs = discService.getAllDiscs();
            DiscMessages.GetDiscsResponse.Builder responseBuilder = DiscMessages.GetDiscsResponse.newBuilder();

            for (Disc disc : discs) {
                DiscMessages.Disc discProto = DiscMapper.toProtobuf(disc);
                responseBuilder.addDiscs(discProto);
            }
            DiscMessages.GetDiscsResponse getDiscsResponse = responseBuilder.build();

            return TransportProtocol.Response.newBuilder()
                    .setPayload(getDiscsResponse.toByteString());

        }catch (Exception e){
            logger.error("Error getting discs", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(ByteString.copyFromUtf8("Error: " + e.getMessage()));
        }
    }
}
