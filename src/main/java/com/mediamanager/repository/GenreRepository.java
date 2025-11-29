package com.mediamanager.repository;

import com.mediamanager.model.Genre;
import jakarta.persistence.EntityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

/**
 * Repository para acesso a dados de Genre
 * Encapsula todas as operações de banco de dados
 */
public class GenreRepository {
    private static final Logger logger = LogManager.getLogger(GenreRepository.class);

    private final EntityManager entityManager;

    public GenreRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Salva um novo genre
     */
    public Genre save(Genre genre) {
        logger.debug("Saving genre: {}", genre.getName());
        entityManager.getTransaction().begin();
        try {
            entityManager.persist(genre);
            entityManager.getTransaction().commit();
            logger.debug("Genre saved with ID: {}", genre.getId());
            return genre;
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            logger.error("Error saving genre", e);
            throw e;
        }
    }

    /**
     * Busca todos os genres
     */
    public List<Genre> findAll() {
        logger.debug("Finding all genres");
        return entityManager
                .createQuery("SELECT g FROM Genre g ORDER BY g.name", Genre.class)
                .getResultList();
    }

    /**
     * Busca genre por ID
     */
    public Optional<Genre> findById(Integer id) {
        logger.debug("Finding genre by ID: {}", id);
        Genre genre = entityManager.find(Genre.class, id);
        return Optional.ofNullable(genre);
    }

    /**
     * Atualiza um genre existente
     */
    public Genre update(Genre genre) {
        logger.debug("Updating genre ID: {}", genre.getId());
        entityManager.getTransaction().begin();
        try {
            Genre updated = entityManager.merge(genre);
            entityManager.getTransaction().commit();
            logger.debug("Genre updated successfully");
            return updated;
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            logger.error("Error updating genre", e);
            throw e;
        }
    }

    /**
     * Deleta um genre por ID
     */
    public boolean deleteById(Integer id) {
        logger.debug("Deleting genre by ID: {}", id);
        entityManager.getTransaction().begin();
        try {
            Genre genre = entityManager.find(Genre.class, id);
            if (genre == null) {
                entityManager.getTransaction().rollback();
                return false;
            }
            entityManager.remove(genre);
            entityManager.getTransaction().commit();
            logger.debug("Genre deleted successfully");
            return true;
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            logger.error("Error deleting genre", e);
            throw e;
        }
    }
}