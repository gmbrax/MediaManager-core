package com.mediamanager.service.database;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;


public class DatabaseManager {
    private final Properties configuration;
    private Connection connection;
    private static final Logger logger = LogManager.getLogger(DatabaseManager.class);
    public DatabaseManager(Properties config) {
        this.configuration = config;
        logger.debug("DatabaseManager created with configuration:");

    }

    public void init() throws Exception {
        logger.info("Initializing database connection...");
        validateConfiguration();

        String databaseType = configuration.getProperty("database.type");
        String databaseUrl = configuration.getProperty("database.url");
        String databaseUsername = configuration.getProperty("database.username");
        String databasePassword = configuration.getProperty("database.password");
        String databasePort = configuration.getProperty("database.port");
        String databaseName = configuration.getProperty("database.name");


        String connectionString = String.format("jdbc:postgresql://%s:%s/%s", databaseUrl, databasePort, databaseName);
        logger.debug("Attempting to connect to: {}", connectionString);
        try {
            connection = DriverManager.getConnection(connectionString, databaseUsername, databasePassword);
            logger.info("Database connection established successfully");

            performSanityChecks();
            logger.info("Database sanity checks passed successfully");


        } catch (SQLException e) {
            logger.error("Failed to connect to database", e);
            throw new Exception("Database connection failed: " + e.getMessage(), e);
        }



    }
    private void performSanityChecks() throws SQLException {
        logger.debug("Performing sanity checks...");
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("SELECT 1");

        }
        String databaseProductName = connection.getMetaData().getDatabaseProductName();
        String databaseProductVersion = connection.getMetaData().getDatabaseProductVersion();
        logger.info("Connected to database: {} v{}", databaseProductName, databaseProductVersion);
    }
    private void validateConfiguration() throws Exception {
        String[] requiredProperties = {
                "database.url",
                "database.username",
                "database.password",
                "database.port",
                "database.name"
        };

        for (String property : requiredProperties) {
            if (configuration.getProperty(property) == null ||
                    configuration.getProperty(property).trim().isEmpty()) {
                throw new Exception("Required database configuration missing: " + property);
            }
        }

        logger.debug("Database configuration validated successfully");

        }



    public Connection getConnection() {
        return connection;
    }

    public void close() {
        if (connection != null) {
            try {
                logger.info("Closing database connection...");
                connection.close();
                logger.info("Database connection closed successfully");
            } catch (SQLException e) {
                logger.error("Error closing database connection: {}", e.getMessage());
            }
        } else {
            logger.debug("No database connection to close");
        }
    }
}
