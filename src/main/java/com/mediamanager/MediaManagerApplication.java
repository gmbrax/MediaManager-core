package com.mediamanager;

import com.mediamanager.service.database.DatabaseManager;
import com.mediamanager.service.delegate.DelegateActionManager;
import com.mediamanager.service.ipc.IPCManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.mediamanager.service.database.SqliteDatabaseManager;

public class MediaManagerApplication {
    private static final Logger logger = LogManager.getLogger(MediaManagerApplication.class);
    private static Properties config;
    private static DatabaseManager databaseManager;
    private static DelegateActionManager actionManager;
    private static IPCManager ipcManager;

    public enum ApplicationMode {
        LOCAL("local"),
        SERVER("server");

        private final String value;

        ApplicationMode(String value) {
            this.value = value;
        }
    }

    public static void main(String[] args) {
        logger.info("Starting MediaManager Core Application...");

        try {
            // Load configuration
            loadConfiguration();
            String runTypeString = config.getProperty("runtype","local");
            ApplicationMode mode = null;

            for (ApplicationMode am : ApplicationMode.values()) {
                if (am.value.equalsIgnoreCase(runTypeString)) {
                    mode = am;
                    break;
                }
            }
            if (mode == null) {
                logger.error("Invalid run type: {}", runTypeString);
                throw new Exception("Invalid run type: " + runTypeString);
            }
            logger.info("Run type: {}", mode);
            switch (mode) {
                case LOCAL:
                    logger.info("Starting local database...");
                    databaseManager = new SqliteDatabaseManager(config);
                    break;
                case SERVER:
                    throw new Exception("Server mode not yet implemented");
                default:
            }
            databaseManager.init();
            actionManager = new DelegateActionManager();
            actionManager.start();
            ipcManager = new IPCManager(config,actionManager);
            ipcManager.init();

            // TODO: Start application services

            logger.info("MediaManager Core started successfully");
            logger.info("IPC Socket: {}", ipcManager.getSocketPath().toAbsolutePath().toString());

            // Keep application running
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {

                logger.info("Shutting down MediaManager Core...");


                if (databaseManager != null) {
                    databaseManager.close();
                }

                if (ipcManager != null) {
                    try {
                        ipcManager.close();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

                if (actionManager != null) {
                    actionManager.stop();
                }

                logger.info("MediaManager Core shutdown successfully");
                logger.info("Goodbye!");


                // Give Log4j2 time to write all pending messages before shutting down
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                // Now shutdown Log4j2
                org.apache.logging.log4j.LogManager.shutdown();
            }));
            logger.info("Application is running");
            logger.info("Press Ctrl+C to exit");
            Thread.currentThread().join();

        } catch (InterruptedException e) {

            logger.info("Application interrupted, initiating shutdown...");

            Thread.currentThread().interrupt();

        } catch (Exception e) {
            logger.error("Failed to start MediaManager Core", e);
            System.exit(1);
        }
    }

    private static void loadConfiguration() throws IOException {
        config = new Properties();
        try (InputStream input = MediaManagerApplication.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new IOException("Unable to find config.properties");
            }
            config.load(input);
            logger.info("Configuration loaded successfully");
        }
    }

    public static Properties getConfig() {
        return config;
    }
}