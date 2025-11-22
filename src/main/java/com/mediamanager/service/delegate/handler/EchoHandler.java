package com.mediamanager.service.delegate.handler;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mediamanager.protocol.TestProtocol.EchoCommand;      // ← Import
import com.mediamanager.protocol.TestProtocol.EchoResponse;    // ← Import
import com.mediamanager.protocol.TransportProtocol;
import com.mediamanager.service.delegate.ActionHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EchoHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(EchoHandler.class);

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload)
            throws InvalidProtocolBufferException {  // ← Pode lançar exceção

        // 1. Parse Protobuf bytes → EchoCommand
        EchoCommand command = EchoCommand.parseFrom(requestPayload);

        logger.debug("Echo received: {}", command.getMessage());

        // 2. Cria EchoResponse (Protobuf)
        EchoResponse echoResponse = EchoResponse.newBuilder()
                .setMessage(command.getMessage())
                .setServerTimestamp(System.currentTimeMillis())
                .build();

        // 3. Serializa EchoResponse → bytes
        ByteString responsePayload = ByteString.copyFrom(echoResponse.toByteArray());

        // 4. Retorna Response
        return TransportProtocol.Response.newBuilder()
                .setPayload(responsePayload)
                .setStatusCode(200)
                .putHeaders("Content-Type", "application/x-protobuf");
    }
}
