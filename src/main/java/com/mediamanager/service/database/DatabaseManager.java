package com.mediamanager.service.database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;


public class DatabaseManager {
    private final Properties configuration;
    private Connection connection;
    public DatabaseManager(Properties config) {
        this.configuration = config;

    }

    public void init() throws Exception {
        String databaseType = configuration.getProperty("database.type");
        String databaseUrl = configuration.getProperty("database.url");
        String databaseUsername = configuration.getProperty("database.username");
        String databasePassword = configuration.getProperty("database.password");
        String databasePort = configuration.getProperty("database.port");
        String databaseName = configuration.getProperty("database.name");


        String connectionString = String.format("jdbc:postgresql://%s:%s/%s", databaseUrl, databasePort, databaseName);
        connection = DriverManager.getConnection(connectionString, databaseUsername, databasePassword);

    }
    public Connection getConnection() {
        return connection;
    }

}
