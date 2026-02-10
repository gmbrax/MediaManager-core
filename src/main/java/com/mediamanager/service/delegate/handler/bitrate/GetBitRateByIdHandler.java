package com.mediamanager.service.delegate.handler.bitrate;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mediamanager.mapper.BitRateMapper;
import com.mediamanager.model.BitRate;
import com.mediamanager.protocol.TransportProtocol;
import com.mediamanager.protocol.messages.BitRateMessages;
import com.mediamanager.service.bitrate.BitRateService;
import com.mediamanager.service.delegate.ActionHandler;
import com.mediamanager.service.delegate.annotation.Action;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

@Action(value = "bitrate.getById")
public class GetBitRateByIdHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(GetBitRateByIdHandler.class);
    private final BitRateService bitRateService;

    public GetBitRateByIdHandler(BitRateService bitRateService) {
        this.bitRateService = bitRateService;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload)
        throws InvalidProtocolBufferException{

        try{
            BitRateMessages.GetBitRateByIdRequest getByIdRequest =
                    BitRateMessages.GetBitRateByIdRequest.parseFrom(requestPayload);
            int id = getByIdRequest.getId();

            Optional<BitRate> bitRateOpt = bitRateService.getBitRateById(id);

            if (bitRateOpt.isEmpty()){
                logger.warn("BitRate not found with ID: {}", id);
                return TransportProtocol.Response.newBuilder()
                        .setStatusCode(404)
                        .setPayload(ByteString.copyFromUtf8("BitRate not found"));
            }
            BitRateMessages.BitRate bitRateProto = BitRateMapper.toProtobuf(bitRateOpt.get());
            BitRateMessages.GetBitRateByIdResponse getByIdResponse = BitRateMessages.GetBitRateByIdResponse.newBuilder()
                    .setBitrate(bitRateProto)
                    .build();
            return TransportProtocol.Response.newBuilder()
                    .setPayload(getByIdResponse.toByteString());
        } catch (Exception e) {
            logger.error("Error getting bit-rate by ID", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(ByteString.copyFromUtf8("Error: "+ e.getMessage()));
        }
    }
}
