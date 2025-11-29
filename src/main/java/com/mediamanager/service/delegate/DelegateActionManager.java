package com.mediamanager.service.delegate;

import com.google.protobuf.ByteString;
import com.mediamanager.protocol.TransportProtocol;
import com.mediamanager.repository.GenreRepository;
import com.mediamanager.service.delegate.handler.CloseHandler;
import com.mediamanager.service.delegate.handler.EchoHandler;
import com.mediamanager.service.delegate.handler.HeartbeatHandler;
import com.mediamanager.service.delegate.handler.genre.*;
import com.mediamanager.service.genre.GenreService;
import jakarta.persistence.EntityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class DelegateActionManager {
    private static final Logger logger = LogManager.getLogger(DelegateActionManager.class);
    
    private final Map<String, ActionHandler> handlerRegistry;
    private final EntityManager entityManager;
    private final GenreService genreService;

    public DelegateActionManager(EntityManager entityManager) {
        this.entityManager = entityManager;

        GenreRepository genreRepository = new GenreRepository(entityManager);
        this.genreService = new GenreService(genreRepository);
        logger.debug("DelegateActionManager created");
        this.handlerRegistry = new HashMap<>();
        registerHandlers();
    }

    private void registerHandlers() {
        handlerRegistry.put("echo",new EchoHandler());
        handlerRegistry.put("heartbeat",new HeartbeatHandler());
        handlerRegistry.put("close", new CloseHandler());
        handlerRegistry.put("create_genre", new CreateGenreHandler(genreService));
        handlerRegistry.put("get_genres", new GetGenreHandler(genreService));
        handlerRegistry.put("get_genre_by_id", new GetGenreByIdHandler(genreService));
        handlerRegistry.put("update_genre", new UpdateGenreHandler(genreService));
        handlerRegistry.put("delete_genre", new DeleteGenreHandler(genreService));
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
