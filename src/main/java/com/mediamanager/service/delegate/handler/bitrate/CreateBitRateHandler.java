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

@Action("bitrate.create")
public class CreateBitRateHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(CreateBitRateHandler.class);
    private final BitRateService bitRateService;

    public CreateBitRateHandler(BitRateService bitRateService) {
        this.bitRateService = bitRateService;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload) throws InvalidProtocolBufferException {
        try{
            BitRateMessages.CreateBitRateRequest createRequest =
                    BitRateMessages.CreateBitRateRequest.parseFrom(requestPayload);
            BitRate bitRate = bitRateService.createBitRate(createRequest.getValue());
            BitRateMessages.BitRate BitRateProto = BitRateMapper.toProtobuf(bitRate);
            BitRateMessages.CreateBitRateResponse createBitRateResponse = BitRateMessages.CreateBitRateResponse.newBuilder()
                    .setBitrate(BitRateProto)
                    .build();
            return TransportProtocol.Response.newBuilder()
                    .setPayload(createBitRateResponse.toByteString());
        } catch (IllegalArgumentException e) {
            logger.error("Validation error", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(400)
                    .setPayload(ByteString.copyFromUtf8("Validation error: " + e.getMessage()));

        } catch (Exception e) {
            logger.error("Error creating bit-rate", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(ByteString.copyFromUtf8("Error: " + e.getMessage()));
        }
    }
}

