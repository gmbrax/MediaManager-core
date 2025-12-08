package com.mediamanager.repository;


import com.mediamanager.model.Album;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.util.List;
import java.util.Optional;

public class AlbumRepository {
    private static final Logger logger = LogManager.getLogger(AlbumRepository.class);

    private final EntityManagerFactory entityManagerFactory;

    public AlbumRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public Album save(Album album) {
        logger.debug("Saving Album: {}", album.getName());
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(album);
            em.getTransaction().commit();
            logger.debug("Album has been saved successfully");
            return album;
        } catch (Exception e) {
            em.getTransaction().rollback();
            logger.error("Error while saving Album: {}", e.getMessage());
            throw e;
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public List<Album> findAll() {
        logger.debug("Finding All Album");
        EntityManager em = entityManagerFactory.createEntityManager();
        try{
            return em.createQuery("select a from Album a", Album.class).getResultList();
        }finally {
            if (em.isOpen()) em.close();
        }
    }

    public Optional<Album> findById(Integer id) {
        logger.debug("Finding Album with id: {}", id);
        EntityManager em = entityManagerFactory.createEntityManager();
        try{
            Album album = em.find(Album.class, id);
            return Optional.ofNullable(album);
        }finally {
            if (em.isOpen()) em.close();
        }
    }

    public Album update(Album album) {
        logger.debug("Updating Album: {}", album.getName());
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        try {
            Album updated = em.merge(album);
            em.getTransaction().commit();
            logger.debug("Album has been updated successfully");
            return updated;
        } catch (Exception e) {
            em.getTransaction().rollback();
            logger.error("Error while updating Album: {}", e.getMessage());
            throw e;
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public boolean deleteById(Integer id){
        logger.debug("Deleting Album with id: {}", id);
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        try{
            Album album = em.find(Album.class, id);
            if (album == null) {
                em.getTransaction().rollback();
                return false;
            }
            em.remove(album);
            em.getTransaction().commit();
            logger.debug("Album has been deleted successfully");
            return true;
        } catch (Exception e) {
            em.getTransaction().rollback();
            logger.error("Error while deleting Album: {}", e.getMessage());
            throw e;
        } finally {
            if (em.isOpen()) em.close();
        }
    }

}
