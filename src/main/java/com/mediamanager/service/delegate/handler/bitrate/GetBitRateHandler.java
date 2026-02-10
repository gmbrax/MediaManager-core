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

import java.util.List;


@Action("bitrate.getAll")
public class GetBitRateHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(GetBitRateHandler.class);

    private final BitRateService bitRateService;

    public GetBitRateHandler(BitRateService bitRateService){this.bitRateService = bitRateService;}

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload) throws InvalidProtocolBufferException {
        try{
            List<BitRate> bitRates = bitRateService.getAllBitRates();
            BitRateMessages.GetBitRatesResponse.Builder responseBuilder = BitRateMessages.GetBitRatesResponse.newBuilder();

            for (BitRate bitRate : bitRates) {
                BitRateMessages.BitRate bitRateProto = BitRateMapper.toProtobuf(bitRate);
                responseBuilder.addBitrates(bitRateProto);
            }
            BitRateMessages.GetBitRatesResponse getBitRatesResponse = responseBuilder.build();

            return TransportProtocol.Response.newBuilder()
                    .setPayload(getBitRatesResponse.toByteString());

        }catch (Exception e){
            logger.error("Error getting bit-rates", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(ByteString.copyFromUtf8("Error: " + e.getMessage()));
        }
    }
}
