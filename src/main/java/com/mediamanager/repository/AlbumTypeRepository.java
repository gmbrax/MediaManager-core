package com.mediamanager.repository;


import com.mediamanager.model.AlbumType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.util.List;
import java.util.Optional;

public class AlbumTypeRepository {
    private static final Logger logger = LogManager.getLogger(AlbumTypeRepository.class);

    private final EntityManagerFactory entityManagerFactory;

    public AlbumTypeRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public AlbumType save(AlbumType albumType) {
        logger.debug("Saving AlbumType: {}", albumType.getValue());
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(albumType);
            em.getTransaction().commit();
            logger.debug("AlbumType has been saved successfully");
            return albumType;
        } catch (Exception e) {
            em.getTransaction().rollback();
            logger.error("Error while saving AlbumType: {}", e.getMessage());
            throw e;
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public List<AlbumType> findAll() {
        logger.debug("Finding All AlbumType");
        EntityManager em = entityManagerFactory.createEntityManager();
        try{
            return em.createQuery("select a from AlbumType a", AlbumType.class).getResultList();
        }finally {
            if (em.isOpen()) em.close();
        }
    }

    public Optional<AlbumType> findById(Integer id) {
        logger.debug("Finding AlbumType with id: {}", id);
        EntityManager em = entityManagerFactory.createEntityManager();
        try{
            AlbumType albumType = em.find(AlbumType.class, id);
            return Optional.ofNullable(albumType);
        }finally {
            if (em.isOpen()) em.close();
        }
    }

    public AlbumType update(AlbumType albumType) {
        logger.debug("Updating AlbumType: {}", albumType.getValue());
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        try {
            AlbumType updated = em.merge(albumType);
            em.getTransaction().commit();
            logger.debug("AlbumType has been updated successfully");
            return updated;
        } catch (Exception e) {
            em.getTransaction().rollback();
            logger.error("Error while updating AlbumType: {}", e.getMessage());
            throw e;
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public boolean deleteById(Integer id){
        logger.debug("Deleting AlbumType with id: {}", id);
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        try{
            AlbumType albumType = em.find(AlbumType.class, id);
            if (albumType == null) {
                em.getTransaction().rollback();
                return false;
            }
            em.remove(albumType);
            em.getTransaction().commit();
            logger.debug("AlbumType has been deleted successfully");
            return true;
        } catch (Exception e) {
            em.getTransaction().rollback();
            logger.error("Error while deleting AlbumType: {}", e.getMessage());
            throw e;
        } finally {
            if (em.isOpen()) em.close();
        }
    }

}
