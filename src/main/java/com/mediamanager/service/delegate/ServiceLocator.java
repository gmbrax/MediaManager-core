package com.mediamanager.service.delegate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class ServiceLocator {
    private static final Logger logger = LogManager.getLogger(ServiceLocator.class);
    private final Map<Class<?>, Object> services = new HashMap<>();

    public <T> void register(Class<T> serviceClass, T serviceInstance) {
        if (serviceInstance == null) {
            throw new IllegalArgumentException("Service instance cannot be null");
        }
        services.put(serviceClass, serviceInstance);
        logger.debug("Registered service: {} -> {}",
                serviceClass.getSimpleName(),
                serviceInstance.getClass().getSimpleName());
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> serviceClass) {
        return (T) services.get(serviceClass);
    }

    public boolean has(Class<?> serviceClass) {
        return services.containsKey(serviceClass);
    }
    public int size() {
        return services.size();
    }
    public void logRegisteredServices() {
        logger.info("Registered services: {}", services.size());


        services.forEach((clazz, instance) ->
                logger.info("  - {} -> {}",
                        clazz.getSimpleName(),
                        instance.getClass().getSimpleName())
        );
    }
}

