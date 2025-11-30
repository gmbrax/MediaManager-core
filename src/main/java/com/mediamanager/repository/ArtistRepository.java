package com.mediamanager.repository;

import com.mediamanager.model.Artist;
import com.mediamanager.model.Genre;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class ArtistRepository {
    private static final Logger logger = LogManager.getLogger(ArtistRepository.class);

    private final EntityManagerFactory entityManagerFactory;

    public ArtistRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public Artist save(Artist artist){
        logger.debug("Saving Artist: {}", artist.getName());
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(artist);
            em.getTransaction().commit();
            logger.debug("Artist saved with ID: {}", artist.getId());
            return artist;
        } catch (Exception e) {
            em.getTransaction().rollback();
            logger.error("Error saving Artist", e);
            throw e;
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public List<Artist> findAll(){
        logger.debug("Finding all Artists");
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            return em
                    .createQuery("SELECT a FROM Artist a ORDER BY a.name", Artist.class)
                    .getResultList();
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public Optional<Artist> findById(Integer id){
        logger.debug("Finding artist by ID: {}", id);
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            Artist artist = em.find(Artist.class, id);
            return Optional.ofNullable(artist);
        } finally {
            if (em.isOpen()) em.close();
        }

    }

    public Artist update(Artist artist){
        logger.debug("Updating genre ID: {}", artist.getId());
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        try {
            Artist updated = em.merge(artist);
            em.getTransaction().commit();
            logger.debug("Genre updated successfully");
            return updated;
        } catch (Exception e) {
            em.getTransaction().rollback();
            logger.error("Error updating genre", e);
            throw e;
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public boolean deleteById(Integer id){
        logger.debug("Deleting Artist by ID: {}", id);
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        try {
            Artist artist = em.find(Artist.class, id);
            if (artist == null) {
                em.getTransaction().rollback();
                return false;
            }
            em.remove(artist);
            em.getTransaction().commit();
            logger.debug("Artist deleted successfully");
            return true;
        } catch (Exception e) {
            em.getTransaction().rollback();
            logger.error("Error deleting genre", e);
            throw e;
        } finally {
            if (em.isOpen()) em.close();
        }
    }
        }


