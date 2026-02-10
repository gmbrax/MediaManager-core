package com.mediamanager.service.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class SqliteDatabaseManager extends DatabaseManager {
    private static final Logger logger = LogManager.getLogger(SqliteDatabaseManager.class);




    public SqliteDatabaseManager(Properties config) {
        super(config);
    }

        @Override
        public void init() throws Exception {

            logger.info("Initializing SQLite database...");
            String dataDir = configuration.getProperty("database.dir",
                    System.getProperty("user.home") + "/.mediamanager/db");
            String dbFilename = configuration.getProperty("database.filename", "mediamanager.db");
            String driverClassName = configuration.getProperty("database.driver", "org.sqlite.JDBC");
            try {
                Class.forName(driverClassName);
            }catch(ClassNotFoundException e) {
                logger.error("Failed to load SQLite driver", e);
                throw e;
            }
            Path dataPath = Paths.get(dataDir);
            if (!Files.exists(dataPath)) {
                Files.createDirectories(dataPath);
                logger.debug("Created database directory: {}", dataDir);
            }
            Path dbFile = dataPath.resolve(dbFilename);
            this.connectionUrl = "jdbc:sqlite:" + dbFile.toAbsolutePath().toString();
            logger.info("Database file path: {}", dbFile);
            logger.info("Connection URL: {}", this.connectionUrl);
            initializeHibernate();
            this.connection = createConnection();
            configurePerformancePragmas();
            performSanityChecks();
            ensureSchemaExists();
            logger.info("SQLite database initialized successfully");


    }

    @Override
    protected Connection createConnection() throws Exception {
        try {
            // O driver org.xerial.sqlite-jdbc é carregado automaticamente aqui
            Connection conn = DriverManager.getConnection(this.connectionUrl);
            logger.debug("Got connection to SQLite file");
            return conn;
        } catch (SQLException e) {
            logger.error("Failed to create SQLite connection", e);
            throw new Exception("SQLite connection failed: " + e.getMessage(), e);
        }
    }


    private void configurePerformancePragmas() throws SQLException {
        try (Statement stmt = connection.createStatement()) {

            stmt.execute("PRAGMA journal_mode=WAL;");


            stmt.execute("PRAGMA foreign_keys=ON;");


            stmt.execute("PRAGMA synchronous=NORMAL;");

            stmt.execute("PRAGMA busy_timeout=5000;");

            logger.debug("SQLite performance PRAGMAs applied (WAL, Synchronous, ForeignKeys).");
        }
    }

    private void ensureSchemaExists() throws SQLException {

    }

    @Override
    public void close() {

        super.close();
        logger.info("SQLite resources released.");
    }
}