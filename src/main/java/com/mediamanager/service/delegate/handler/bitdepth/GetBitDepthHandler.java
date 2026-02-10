package com.mediamanager.service.delegate.handler.bitdepth;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mediamanager.mapper.BitDepthMapper;
import com.mediamanager.model.BitDepth;
import com.mediamanager.protocol.TransportProtocol;
import com.mediamanager.protocol.messages.BitDepthMessages;
import com.mediamanager.service.bitdepth.BitDepthService;
import com.mediamanager.service.delegate.ActionHandler;
import com.mediamanager.service.delegate.annotation.Action;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;


@Action("bitdepth.getAll")
public class GetBitDepthHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(GetBitDepthHandler.class);

    private final BitDepthService bitDepthService;

    public GetBitDepthHandler(BitDepthService bitDepthService){this.bitDepthService = bitDepthService;}

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload) throws InvalidProtocolBufferException {
        try{
            List<BitDepth> bitDepths = bitDepthService.getAllBitDepths();
            BitDepthMessages.GetBitDepthsResponse.Builder responseBuilder = BitDepthMessages.GetBitDepthsResponse.newBuilder();

            for (BitDepth bitDepth : bitDepths) {
                BitDepthMessages.BitDepth bitDepthProto = BitDepthMapper.toProtobuf(bitDepth);
                responseBuilder.addBitdepths(bitDepthProto);
            }
            BitDepthMessages.GetBitDepthsResponse getBitDepthsResponse = responseBuilder.build();

            return TransportProtocol.Response.newBuilder()
                    .setPayload(getBitDepthsResponse.toByteString());

        }catch (Exception e){
            logger.error("Error getting bit-depths", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(ByteString.copyFromUtf8("Error: " + e.getMessage()));
        }
    }
}
