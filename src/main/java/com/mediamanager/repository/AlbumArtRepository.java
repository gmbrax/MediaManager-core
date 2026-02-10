package com.mediamanager.repository;


import com.mediamanager.model.AlbumArt;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.util.List;
import java.util.Optional;

public class AlbumArtRepository {
    private static final Logger logger = LogManager.getLogger(AlbumArtRepository.class);

    private final EntityManagerFactory entityManagerFactory;

    public AlbumArtRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public AlbumArt save(AlbumArt albumArt) {
        logger.debug("Saving AlbumArt: {}", albumArt.getFilepath());
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(albumArt);
            em.getTransaction().commit();
            logger.debug("AlbumArt has been saved successfully");
            return albumArt;
        } catch (Exception e) {
            em.getTransaction().rollback();
            logger.error("Error while saving AlbumArt: {}", e.getMessage());
            throw e;
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public List<AlbumArt> findAll() {
        logger.debug("Finding All AlbumArt");
        EntityManager em = entityManagerFactory.createEntityManager();
        try{
            return em.createQuery("select a from AlbumArt a", AlbumArt.class).getResultList();
        }finally {
            if (em.isOpen()) em.close();
        }
    }

    public Optional<AlbumArt> findById(Integer id) {
        logger.debug("Finding AlbumArt with id: {}", id);
        EntityManager em = entityManagerFactory.createEntityManager();
        try{
            AlbumArt albumArt = em.find(AlbumArt.class, id);
            return Optional.ofNullable(albumArt);
        }finally {
            if (em.isOpen()) em.close();
        }
    }

    public AlbumArt update(AlbumArt albumArt) {
        logger.debug("Updating AlbumArt: {}", albumArt.getFilepath());
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        try {
            AlbumArt updated = em.merge(albumArt);
            em.getTransaction().commit();
            logger.debug("AlbumArt has been updated successfully");
            return updated;
        } catch (Exception e) {
            em.getTransaction().rollback();
            logger.error("Error while updating AlbumArt: {}", e.getMessage());
            throw e;
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public boolean deleteById(Integer id){
        logger.debug("Deleting AlbumArt with id: {}", id);
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        try{
            AlbumArt albumArt = em.find(AlbumArt.class, id);
            if (albumArt == null) {
                em.getTransaction().rollback();
                return false;
            }
            em.remove(albumArt);
            em.getTransaction().commit();
            logger.debug("AlbumArt has been deleted successfully");
            return true;
        } catch (Exception e) {
            em.getTransaction().rollback();
            logger.error("Error while deleting AlbumArt: {}", e.getMessage());
            throw e;
        } finally {
            if (em.isOpen()) em.close();
        }
    }

}
