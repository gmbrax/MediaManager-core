package com.mediamanager.service.delegate.handler;

import com.google.protobuf.ByteString;
import com.mediamanager.protocol.TransportProtocol;
import com.mediamanager.service.delegate.ActionHandler;

public class EchoHandler implements ActionHandler {
    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload) {
        String payloadText = requestPayload.toStringUtf8();
        String responseText = "Server received: " + payloadText;

        return TransportProtocol.Response.newBuilder()
                .setPayload(ByteString.copyFromUtf8(responseText))
                .setStatusCode(200);
    }
}
