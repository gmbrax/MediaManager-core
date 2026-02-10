package com.mediamanager.repository;


import com.mediamanager.model.TrackHasComposer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.util.List;
import java.util.Optional;

public class TrackHasComposerRepository {
    private static final Logger logger = LogManager.getLogger(TrackHasComposerRepository.class);

    private final EntityManagerFactory entityManagerFactory;

    public TrackHasComposerRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public TrackHasComposer save(TrackHasComposer trackHasComposer) {
        logger.debug("Saving TrackHasComposer: {}", trackHasComposer);
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(trackHasComposer);
            em.getTransaction().commit();
            logger.debug("TrackHasComposer has been saved successfully");
            return trackHasComposer;
        } catch (Exception e) {
            em.getTransaction().rollback();
            logger.error("Error while saving TrackHasComposer: {}", e.getMessage());
            throw e;
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public List<TrackHasComposer> findAll() {
        logger.debug("Finding All TrackHasComposer");
        EntityManager em = entityManagerFactory.createEntityManager();
        try{
            return em.createQuery("select t from TrackHasComposer t", TrackHasComposer.class).getResultList();
        }finally {
            if (em.isOpen()) em.close();
        }
    }

    public Optional<TrackHasComposer> findById(Integer id) {
        logger.debug("Finding TrackHasComposer with id: {}", id);
        EntityManager em = entityManagerFactory.createEntityManager();
        try{
            TrackHasComposer trackHasComposer = em.find(TrackHasComposer.class, id);
            return Optional.ofNullable(trackHasComposer);
        }finally {
            if (em.isOpen()) em.close();
        }
    }

    public boolean deleteById(Integer id){
        logger.debug("Deleting TrackHasComposer with id: {}", id);
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        try{
            TrackHasComposer trackHasComposer = em.find(TrackHasComposer.class, id);
            if (trackHasComposer == null) {
                em.getTransaction().rollback();
                return false;
            }
            em.remove(trackHasComposer);
            em.getTransaction().commit();
            logger.debug("TrackHasComposer has been deleted successfully");
            return true;
        } catch (Exception e) {
            em.getTransaction().rollback();
            logger.error("Error while deleting TrackHasComposer: {}", e.getMessage());
            throw e;
        } finally {
            if (em.isOpen()) em.close();
        }
    }

}
