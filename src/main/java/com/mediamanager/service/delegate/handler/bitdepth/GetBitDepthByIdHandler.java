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

import java.util.Optional;

@Action(value = "bitdepth.getById")
public class GetBitDepthByIdHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(GetBitDepthByIdHandler.class);
    private final BitDepthService bitDepthServicee;

    public GetBitDepthByIdHandler(BitDepthService bitDepthServicee) {
        this.bitDepthServicee = bitDepthServicee;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload)
        throws InvalidProtocolBufferException{

        try{
            BitDepthMessages.GetBitDepthByIdRequest getByIdRequest =
                    BitDepthMessages.GetBitDepthByIdRequest.parseFrom(requestPayload);
            int id = getByIdRequest.getId();

            Optional<BitDepth> bitDepthOpt = bitDepthServicee.getBitDepthById(id);

            if (bitDepthOpt.isEmpty()){
                logger.warn("BitDepth not found with ID: {}", id);
                return TransportProtocol.Response.newBuilder()
                        .setStatusCode(404)
                        .setPayload(ByteString.copyFromUtf8("BitDepth not found"));
            }
            BitDepthMessages.BitDepth bitDepthProto = BitDepthMapper.toProtobuf(bitDepthOpt.get());
            BitDepthMessages.GetBitDepthByIdResponse getByIdResponse = BitDepthMessages.GetBitDepthByIdResponse.newBuilder()
                    .setBitdepth(bitDepthProto)
                    .build();
            return TransportProtocol.Response.newBuilder()
                    .setPayload(getByIdResponse.toByteString());
        } catch (Exception e) {
            logger.error("Error getting bit-depth by ID", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(ByteString.copyFromUtf8("Error: "+ e.getMessage()));
        }
    }
}
