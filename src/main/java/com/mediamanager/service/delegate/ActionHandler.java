package com.mediamanager.service.delegate;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mediamanager.protocol.TransportProtocol;

@FunctionalInterface
public interface ActionHandler {
    TransportProtocol.Response.Builder handle(ByteString requestPayload) throws InvalidProtocolBufferException;
}

