package com.mediamanager.service.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public abstract class DatabaseManager {
    protected final Properties configuration;
    protected Connection connection;
    protected static final Logger logger = LogManager.getLogger(DatabaseManager.class);

    public DatabaseManager(Properties config) {
        this.configuration = config;
        logger.debug("DatabaseManager created with configuration:");
    }

    public abstract void init() throws Exception;

    protected abstract Connection createConnection() throws Exception;

    protected void performSanityChecks() throws SQLException {
        logger.debug("Performing sanity checks...");
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("SELECT 1");

        }
        String databaseProductName = connection.getMetaData().getDatabaseProductName();
        String databaseProductVersion = connection.getMetaData().getDatabaseProductVersion();
        logger.info("Connected to database: {} v{}", databaseProductName, databaseProductVersion);
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
    protected boolean testConnection() {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("SELECT 1");
            return true;
        } catch (SQLException e) {
            logger.error("Connection test failed", e);
            return false;
        }
    }
}
