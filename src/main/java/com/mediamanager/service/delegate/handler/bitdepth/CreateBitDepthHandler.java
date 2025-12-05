package com.mediamanager.service.delegate.handler.bitdepth;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mediamanager.mapper.BitDepthMapper;
import com.mediamanager.model.BitDepth;
import com.mediamanager.protocol.TransportProtocol;
import com.mediamanager.protocol.messages.ArtistMessages;
import com.mediamanager.protocol.messages.BitDepthMessages;
import com.mediamanager.service.bitdepth.BitDepthService;
import com.mediamanager.service.delegate.ActionHandler;
import com.mediamanager.service.delegate.annotation.Action;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Action("bitdepth.create")
public class CreateBitDepthHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(CreateBitDepthHandler.class);
    private final BitDepthService bitDepthService;

    public CreateBitDepthHandler(BitDepthService bitDepthService) {
        this.bitDepthService = bitDepthService;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload) throws InvalidProtocolBufferException {
        try{
            BitDepthMessages.CreateBitDepthRequest createRequest =
                    BitDepthMessages.CreateBitDepthRequest.parseFrom(requestPayload);
            BitDepth bitDepth = bitDepthService.createBitDepth(createRequest.getValue());
            BitDepthMessages.BitDepth BitDepthProto = BitDepthMapper.toProtobuf(bitDepth);
            BitDepthMessages.CreateBitDepthResponse createBitDepthResponse = BitDepthMessages.CreateBitDepthResponse.newBuilder()
                    .setBitdepth(BitDepthProto)
                    .build();
            return TransportProtocol.Response.newBuilder()
                    .setPayload(createBitDepthResponse.toByteString());
        } catch (IllegalArgumentException e) {
            logger.error("Validation error", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(400)
                    .setPayload(ByteString.copyFromUtf8("Validation error: " + e.getMessage()));

        } catch (Exception e) {
            logger.error("Error creating bit-depth", e);
            return TransportProtocol.Response.newBuilder()
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(ByteString.copyFromUtf8("Error: " + e.getMessage()));
        }
    }
}

