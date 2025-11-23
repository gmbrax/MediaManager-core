package com.mediamanager.service.delegate.handler;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mediamanager.protocol.TestProtocol.CloseCommand;
import com.mediamanager.protocol.TestProtocol.CloseResponse;
import com.mediamanager.protocol.TransportProtocol;
import com.mediamanager.service.delegate.ActionHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CloseHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(CloseHandler.class);

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload)
            throws InvalidProtocolBufferException {

        CloseCommand.parseFrom(requestPayload); // Valida

        logger.info("Close command received - connection will close");

        CloseResponse response = CloseResponse.newBuilder()
                .setMessage("Connection closing. Goodbye!")
                .build();

        return TransportProtocol.Response.newBuilder()
                .setPayload(ByteString.copyFrom(response.toByteArray()))
                .setStatusCode(200)
                .putHeaders("Connection", "close"); // ← Marca para fechar
    }
}