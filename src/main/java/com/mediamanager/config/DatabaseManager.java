package com.mediamanager.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class DatabaseManager {
    private static final Logger logger = LogManager.getLogger(DatabaseManager.class);
    private static DatabaseManager instance;
    private EntityManagerFactory entityManagerFactory;

    private DatabaseManager(Properties config) {
        initializeEntityManagerFactory(config);
    }

    public static synchronized DatabaseManager getInstance(Properties config) {
        if (instance == null) {
            instance = new DatabaseManager(config);
        }
        return instance;
    }

    private void initializeEntityManagerFactory(Properties config) {
        try {
            logger.info("Initializing database connection...");

            Map<String, String> properties = new HashMap<>();
            properties.put("jakarta.persistence.jdbc.url", config.getProperty("db.url"));
            properties.put("jakarta.persistence.jdbc.user", config.getProperty("db.username"));
            properties.put("jakarta.persistence.jdbc.password", config.getProperty("db.password"));
            properties.put("jakarta.persistence.jdbc.driver", config.getProperty("db.driver"));

            properties.put("hibernate.hikari.maximumPoolSize", config.getProperty("db.pool.maximum-pool-size"));
            properties.put("hibernate.hikari.minimumIdle", config.getProperty("db.pool.minimum-idle"));
            properties.put("hibernate.hikari.connectionTimeout", config.getProperty("db.pool.connection-timeout"));
            properties.put("hibernate.hikari.idleTimeout", config.getProperty("db.pool.idle-timeout"));
            properties.put("hibernate.hikari.maxLifetime", config.getProperty("db.pool.max-lifetime"));

            entityManagerFactory = Persistence.createEntityManagerFactory("MediaManagerPU", properties);
            logger.info("Database connection initialized successfully");
        } catch (Exception e) {
            logger.error("Failed to initialize database connection", e);
            throw new RuntimeException("Failed to initialize database connection", e);
        }
    }

    public EntityManager createEntityManager() {
        if (entityManagerFactory == null) {
            throw new IllegalStateException("EntityManagerFactory is not initialized");
        }
        return entityManagerFactory.createEntityManager();
    }

    public void shutdown() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            logger.info("Closing database connection...");
            entityManagerFactory.close();
            logger.info("Database connection closed successfully");
        }
    }

    public boolean isInitialized() {
        return entityManagerFactory != null && entityManagerFactory.isOpen();
    }
}
