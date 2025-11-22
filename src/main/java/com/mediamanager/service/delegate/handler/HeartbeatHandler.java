package com.mediamanager.service.delegate.handler;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mediamanager.protocol.TestProtocol.HeartbeatCommand;
import com.mediamanager.protocol.TestProtocol.HeartbeatResponse;
import com.mediamanager.protocol.TransportProtocol;
import com.mediamanager.service.delegate.ActionHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HeartbeatHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(HeartbeatHandler.class);

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload)
            throws InvalidProtocolBufferException {

        HeartbeatCommand command = HeartbeatCommand.parseFrom(requestPayload);

        long serverTime = System.currentTimeMillis();

        logger.debug("Heartbeat received. Client T1={}, Server T2={}",
                command.getClientTimestamp(), serverTime);

        HeartbeatResponse response = HeartbeatResponse.newBuilder()
                .setClientTimestamp(command.getClientTimestamp())  // Echo T1
                .setServerTimestamp(serverTime)                     // T2
                .build();

        return TransportProtocol.Response.newBuilder()
                .setPayload(ByteString.copyFrom(response.toByteArray()))
                .setStatusCode(200)
                .putHeaders("Content-Type", "application/x-protobuf");
    }
}