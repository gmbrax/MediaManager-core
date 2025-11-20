package com.mediamanager.service.database;

import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.Properties;

public class LocalDatabaseManager extends DatabaseManager {
    private static final Logger logger = LogManager.getLogger(LocalDatabaseManager.class);
    private EmbeddedPostgres embeddedPostgres;

    public LocalDatabaseManager(Properties config) {
        super(config);
    }

    @Override
    public void init() throws Exception {
        logger.info("Initializing embedded PostgreSQL database...");

        // Configurações específicas do modo local
        String dataDir = configuration.getProperty("local.database.dir",
                System.getProperty("user.home") + "/.mediamanager/db");
        int port = Integer.parseInt(
                configuration.getProperty("local.database.port", "54321")
        );

        // Cria diretório se não existir
        Path dataPath = Paths.get(dataDir);
        Files.createDirectories(dataPath);
        logger.debug("Using data directory: {}", dataDir);

        // Inicia o PostgreSQL embedded
        logger.info("Starting embedded PostgreSQL on port {}", port);
        embeddedPostgres = EmbeddedPostgres.builder()
                .setDataDirectory(dataPath)
                .setCleanDataDirectory(false)  // Mantém dados
                .setPort(port)
                .start();

        // Cria a conexão
        this.connection = createConnection();
        logger.info("Embedded database connection established successfully");

        // Usa o método da classe pai para sanity checks
        performSanityChecks();
        logger.info("Database sanity checks passed successfully");
    }

    @Override
    protected Connection createConnection() throws Exception {
        if (embeddedPostgres == null) {
            throw new IllegalStateException("Embedded PostgreSQL not started");
        }

        try {
            // Pega conexão do embedded
            Connection conn = embeddedPostgres.getPostgresDatabase().getConnection();
            logger.debug("Got connection from embedded PostgreSQL");
            return conn;
        } catch (Exception e) {
            logger.error("Failed to get embedded connection", e);
            throw new Exception("Embedded connection failed: " + e.getMessage(), e);
        }
    }

    @Override
    public void close() {
        // Primeiro fecha a conexão (método da classe pai)
        super.close();

        // Depois para o embedded
        if (embeddedPostgres != null) {
            try {
                logger.info("Stopping embedded PostgreSQL...");
                embeddedPostgres.close();
                logger.info("Embedded PostgreSQL stopped successfully");
            } catch (Exception e) {
                logger.error("Error stopping embedded PostgreSQL", e);
            }
        }
    }
}