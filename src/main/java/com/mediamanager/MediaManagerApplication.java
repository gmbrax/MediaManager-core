package com.mediamanager;

import com.mediamanager.config.DatabaseManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MediaManagerApplication {
    private static final Logger logger = LogManager.getLogger(MediaManagerApplication.class);
    private static Properties config;
    private static DatabaseManager databaseManager;

    public static void main(String[] args) {
        logger.info("Starting MediaManager Core Application...");

        try {
            // Load configuration
            loadConfiguration();

            // Initialize database connection
            databaseManager = DatabaseManager.getInstance(config);

            // TODO: Initialize IPC server with named pipes
            // TODO: Start application services

            logger.info("MediaManager Core started successfully");
            logger.info("IPC Pipe: {}", config.getProperty("ipc.pipe.path") + "/" + config.getProperty("ipc.pipe.name"));

            // Keep application running
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                logger.info("Shutting down MediaManager Core...");
                if (databaseManager != null) {
                    databaseManager.shutdown();
                }
            }));

        } catch (Exception e) {
            logger.error("Failed to start MediaManager Core", e);
            System.exit(1);
        }
    }

    private static void loadConfiguration() throws IOException {
        config = new Properties();
        try (InputStream input = MediaManagerApplication.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new IOException("Unable to find application.properties");
            }
            config.load(input);
            logger.info("Configuration loaded successfully");
        }
    }

    public static Properties getConfig() {
        return config;
    }

    public static DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}
