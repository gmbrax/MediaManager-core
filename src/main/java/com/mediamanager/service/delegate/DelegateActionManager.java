package com.mediamanager.service.delegate;

import com.google.protobuf.ByteString;
import com.mediamanager.protocol.TransportProtocol;
import com.mediamanager.repository.*;
import com.mediamanager.service.albumart.AlbumArtService;
import com.mediamanager.service.albumtype.AlbumTypeService;
import com.mediamanager.service.bitdepth.BitDepthService;
import com.mediamanager.service.bitrate.BitRateService;
import com.mediamanager.service.composer.ComposerService;
import com.mediamanager.repository.GenreRepository;
import com.mediamanager.service.artist.ArtistService;
import com.mediamanager.service.delegate.annotation.Action;

import com.mediamanager.service.genre.GenreService;
import com.mediamanager.service.samplingrate.SamplingRateService;
import jakarta.persistence.EntityManagerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import java.lang.reflect.Constructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DelegateActionManager {
    private static final Logger logger = LogManager.getLogger(DelegateActionManager.class);
    
    private final Map<String, ActionHandler> handlerRegistry;
    private final ServiceLocator serviceLocator;
    private final EntityManagerFactory entityManagerFactory;


    public DelegateActionManager(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
        this.serviceLocator = new ServiceLocator();

        initializeServices();


        logger.debug("DelegateActionManager created");
        this.handlerRegistry = new HashMap<>();
        autoRegisterHandlers();

    }

    private void initializeServices() {
        logger.info("Initializing services...");


        GenreRepository genreRepository = new GenreRepository(entityManagerFactory);
        GenreService genreService = new GenreService(genreRepository);


        serviceLocator.register(GenreService.class, genreService);

        ArtistRepository artistRepository = new ArtistRepository(entityManagerFactory);
        ArtistService artistService = new ArtistService(artistRepository);

        serviceLocator.register(ArtistService.class, artistService);

        ComposerRepository composerRepository = new ComposerRepository(entityManagerFactory);
        ComposerService composerService = new ComposerService(composerRepository);
        serviceLocator.register(ComposerService.class, composerService);

        BitDepthRepository bitDepthRepository = new BitDepthRepository(entityManagerFactory);
        BitDepthService bitDepthService = new BitDepthService(bitDepthRepository);
        serviceLocator.register(BitDepthService.class, bitDepthService);

        BitRateRepository bitRateRepository = new BitRateRepository(entityManagerFactory);
        BitRateService bitRateService = new BitRateService(bitRateRepository);
        serviceLocator.register(BitRateService.class, bitRateService);

        SamplingRateRepository samplingRateRepository = new SamplingRateRepository(entityManagerFactory);
        SamplingRateService  samplingRateService = new SamplingRateService(samplingRateRepository);
        serviceLocator.register(SamplingRateService.class, samplingRateService);

        AlbumArtRepository albumArtRepository = new AlbumArtRepository(entityManagerFactory);
        AlbumArtService albumArtService = new AlbumArtService(albumArtRepository);
        serviceLocator.register(AlbumArtService.class, albumArtService);

        AlbumTypeRepository albumTypeRepository = new AlbumTypeRepository(entityManagerFactory);
        AlbumTypeService albumTypeService = new AlbumTypeService(albumTypeRepository);
        serviceLocator.register(AlbumTypeService.class, albumTypeService);

        serviceLocator.logRegisteredServices();

        logger.info("Services initialized successfully");
    }



    public void start(){
        logger.info("DelegateActionManager started");
    }

    public void stop(){
        logger.info("DelegateActionManager stopped");
    }

    @SuppressWarnings("unchecked")
    private ActionHandler instantiateHandler(Class<?> clazz) throws Exception {
        if(!ActionHandler.class.isAssignableFrom(clazz)){
            throw new IllegalArgumentException(
                    clazz.getName() + " is annotated with @Action but does not implement ActionHandler");

        }
        logger.debug("Attempting to instantiate handler: {}", clazz.getSimpleName());


        Constructor<?>[] constructors = clazz.getDeclaredConstructors();


        // Sort constructors by parameter count (descending) to prefer DI constructors
        java.util.Arrays.sort(constructors, (c1, c2) -> 
            Integer.compare(c2.getParameterCount(), c1.getParameterCount()));

        for (Constructor<?> constructor : constructors) {

            Class<?>[] paramTypes = constructor.getParameterTypes();


            Object[] params = new Object[paramTypes.length];
            boolean allDependenciesResolved = true;

            for (int i = 0; i < paramTypes.length; i++) {

                Object service = serviceLocator.get(paramTypes[i]);

                if (service == null) {

                    allDependenciesResolved = false;
                    logger.debug("Cannot resolve dependency {} for {}",
                            paramTypes[i].getSimpleName(),
                            clazz.getSimpleName());
                    break;  // Para de tentar esse construtor
                }


                params[i] = service;
            }


            if (allDependenciesResolved) {
                logger.debug("Using constructor with {} params for {}",
                        paramTypes.length, clazz.getSimpleName());
                return (ActionHandler) constructor.newInstance(params);
            }
        }


        throw new IllegalStateException(
                String.format(
                        "Cannot instantiate handler %s. No suitable constructor found. " +
                                "Make sure all required services are registered in ServiceLocator.",
                        clazz.getName()
                )
        );
    }

    private void autoRegisterHandlers() {
        logger.info("Starting auto-registration of handlers...");
        Reflections reflections = new Reflections(
                "com.mediamanager.service.delegate.handler",
                Scanners.TypesAnnotated
        );
        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(Action.class);
        logger.info("Found {} handler classes with @Action annotation", annotatedClasses.size());
        int successCount = 0;
        int failureCount = 0;
        for (Class<?> handlerClass : annotatedClasses) {
            try {

                Action actionAnnotation = handlerClass.getAnnotation(Action.class);
                String actionName = actionAnnotation.value();

                logger.debug("Processing handler: {} for action '{}'",
                        handlerClass.getSimpleName(),
                        actionName);


                ActionHandler handler = instantiateHandler(handlerClass);


                handlerRegistry.put(actionName, handler);

                logger.info("✓ Registered handler: '{}' -> {}",
                        actionName,
                        handlerClass.getSimpleName());
                successCount++;

            } catch (Exception e) {

                logger.error("✗ Failed to register handler: {}",
                        handlerClass.getName(),
                        e);
                failureCount++;
            }
        }


        logger.info("Auto-registration complete: {} successful, {} failed, {} total",
                successCount,
                failureCount,
                successCount + failureCount);

        if (failureCount > 0) {
            logger.warn("Some handlers failed to register. Check logs above for details.");
        }
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
