package com.mediamanager.service.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.cfg.Configuration;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import jakarta.persistence.Entity;
import java.util.Set;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;


public abstract class DatabaseManager {
    protected final Properties configuration;
    protected Connection connection;
    protected String connectionUrl;
    protected EntityManagerFactory entityManagerFactory;
    protected EntityManager entityManager;
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
        if (entityManager != null && entityManager.isOpen()) {
            try {
                entityManager.close();
                logger.info("EntityManager closed");
            } catch (Exception e) {
                logger.error("Error closing EntityManager: {}", e.getMessage());
            }
        }
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            try {
                entityManagerFactory.close();
                logger.info("EntityManagerFactory closed");
            } catch (Exception e) {
                logger.error("Error closing EntityManagerFactory: {}", e.getMessage());
            }
        }
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
    protected void initializeHibernate() {
        logger.info("Initializing Hibernate ORM...");

        Configuration hibernateConfig = new Configuration();

        // DEBUG PRIMEIRO - antes de usar as propriedades
        String dialect = configuration.getProperty("hibernate.dialect");
        String hbm2ddl = configuration.getProperty("hibernate.hbm2ddl.auto");
        String driver = configuration.getProperty("database.driver");

        logger.info("DEBUG - dialect: {}", dialect);
        logger.info("DEBUG - hbm2ddl: {}", hbm2ddl);
        logger.info("DEBUG - driver: {}", driver);
        logger.info("DEBUG - connectionUrl: {}", connectionUrl);

        // Agora usa as propriedades
        hibernateConfig.setProperty("hibernate.connection.url", connectionUrl);
        hibernateConfig.setProperty("hibernate.connection.driver_class", driver);
        hibernateConfig.setProperty("hibernate.dialect", dialect);
        hibernateConfig.setProperty("hibernate.hbm2ddl.auto", hbm2ddl);
        hibernateConfig.setProperty("hibernate.show_sql",
                configuration.getProperty("hibernate.show_sql", "false"));
        hibernateConfig.setProperty("hibernate.format_sql",
                configuration.getProperty("hibernate.format_sql", "true"));

        logger.info("Scanning for entities in package: com.mediamanager.model");
        Reflections reflections = new Reflections("com.mediamanager.model", Scanners.TypesAnnotated);
        Set<Class<?>> entityClasses = reflections.getTypesAnnotatedWith(Entity.class);

        logger.info("Found {} entities", entityClasses.size());
        for (Class<?> entityClass : entityClasses) {
            logger.debug("Registering entity: {}", entityClass.getSimpleName());
            hibernateConfig.addAnnotatedClass(entityClass);
        }

        // Criar EntityManagerFactory
        entityManagerFactory = hibernateConfig.buildSessionFactory().unwrap(EntityManagerFactory.class);
        entityManager = entityManagerFactory.createEntityManager();

        logger.info("Hibernate ORM initialized successfully");
    }
}