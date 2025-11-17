package com.mediamanager.service.delegate;

import com.google.protobuf.ByteString;
import com.mediamanager.protocol.TransportProtocol;
import com.mediamanager.service.delegate.handler.EchoHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class DelegateActionManager {
    private static final Logger logger = LogManager.getLogger(DelegateActionManager.class);
    
    private final Map<String, ActionHandler> handlerRegistry;
    
    public DelegateActionManager() {
        
        logger.debug("DelegateActionManager created");
        this.handlerRegistry = new HashMap<>();
        registerHandlers();
    }

    private void registerHandlers() {
        handlerRegistry.put("echo",new EchoHandler());
    }

    public void start(){
        logger.info("DelegateActionManager started");
    }

    public void stop(){
        logger.info("DelegateActionManager stopped");
    }

    public TransportProtocol.Response ProcessedRequest(TransportProtocol.Request request){
        String requestId = request.getRequestId();
        logger.info("Processing request: {}", requestId);
        String action = request.getHeadersMap().getOrDefault("action", "unknown");
        ActionHandler handler = handlerRegistry.get(action);
        TransportProtocol.Response.Builder responseBuilder;
        if (handler == null) {
            logger.warn("No handler found for action: {}", action);
            responseBuilder = TransportProtocol.Response.newBuilder()
                    .setStatusCode(404) // 404 Not Found
                    .setPayload(ByteString.copyFromUtf8("Error: Action '" + action + "' not found."));
        } else{
            try {
                logger.debug("Delegating action '{}' to handler...", action);
                responseBuilder = handler.handle(request.getPayload());
            }catch (Exception e) {
                logger.error("Handler for action '{}' threw an exception:", action, e);
                responseBuilder = TransportProtocol.Response.newBuilder()
                        .setStatusCode(500) // 500 Internal Server Error
                        .setPayload(ByteString.copyFromUtf8("Error: " + e.getMessage()));
            }
        }
            return responseBuilder.setRequestId(requestId).build();
    }
}
