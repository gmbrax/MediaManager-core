package com.mediamanager.repository;


import com.mediamanager.model.TrackHasArtist;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.util.List;
import java.util.Optional;

public class TrackHasArtistRepository {
    private static final Logger logger = LogManager.getLogger(TrackHasArtistRepository.class);

    private final EntityManagerFactory entityManagerFactory;

    public TrackHasArtistRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public TrackHasArtist save(TrackHasArtist trackHasArtist) {
        logger.debug("Saving TrackHasArtist: {}", trackHasArtist);
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(trackHasArtist);
            em.getTransaction().commit();
            logger.debug("TrackHasArtist has been saved successfully");
            return trackHasArtist;
        } catch (Exception e) {
            em.getTransaction().rollback();
            logger.error("Error while saving TrackHasArtist: {}", e.getMessage());
            throw e;
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public List<TrackHasArtist> findAll() {
        logger.debug("Finding All TrackHasArtist");
        EntityManager em = entityManagerFactory.createEntityManager();
        try{
            return em.createQuery("select t from TrackHasArtist t", TrackHasArtist.class).getResultList();
        }finally {
            if (em.isOpen()) em.close();
        }
    }

    public Optional<TrackHasArtist> findById(Integer id) {
        logger.debug("Finding TrackHasArtist with id: {}", id);
        EntityManager em = entityManagerFactory.createEntityManager();
        try{
            TrackHasArtist trackHasArtist = em.find(TrackHasArtist.class, id);
            return Optional.ofNullable(trackHasArtist);
        }finally {
            if (em.isOpen()) em.close();
        }
    }

    public boolean deleteById(Integer id){
        logger.debug("Deleting TrackHasArtist with id: {}", id);
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        try{
            TrackHasArtist trackHasArtist = em.find(TrackHasArtist.class, id);
            if (trackHasArtist == null) {
                em.getTransaction().rollback();
                return false;
            }
            em.remove(trackHasArtist);
            em.getTransaction().commit();
            logger.debug("TrackHasArtist has been deleted successfully");
            return true;
        } catch (Exception e) {
            em.getTransaction().rollback();
            logger.error("Error while deleting TrackHasArtist: {}", e.getMessage());
            throw e;
        } finally {
            if (em.isOpen()) em.close();
        }
    }

}
