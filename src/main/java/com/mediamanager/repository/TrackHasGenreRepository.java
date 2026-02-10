package com.mediamanager.repository;


import com.mediamanager.model.TrackHasGenre;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.util.List;
import java.util.Optional;

public class TrackHasGenreRepository {
    private static final Logger logger = LogManager.getLogger(TrackHasGenreRepository.class);

    private final EntityManagerFactory entityManagerFactory;

    public TrackHasGenreRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public TrackHasGenre save(TrackHasGenre trackHasGenre) {
        logger.debug("Saving TrackHasGenre: {}", trackHasGenre);
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(trackHasGenre);
            em.getTransaction().commit();
            logger.debug("TrackHasGenre has been saved successfully");
            return trackHasGenre;
        } catch (Exception e) {
            em.getTransaction().rollback();
            logger.error("Error while saving TrackHasGenre: {}", e.getMessage());
            throw e;
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public List<TrackHasGenre> findAll() {
        logger.debug("Finding All TrackHasGenre");
        EntityManager em = entityManagerFactory.createEntityManager();
        try{
            return em.createQuery("select t from TrackHasGenre t", TrackHasGenre.class).getResultList();
        }finally {
            if (em.isOpen()) em.close();
        }
    }

    public Optional<TrackHasGenre> findById(Integer id) {
        logger.debug("Finding TrackHasGenre with id: {}", id);
        EntityManager em = entityManagerFactory.createEntityManager();
        try{
            TrackHasGenre trackHasGenre = em.find(TrackHasGenre.class, id);
            return Optional.ofNullable(trackHasGenre);
        }finally {
            if (em.isOpen()) em.close();
        }
    }

    public boolean deleteById(Integer id){
        logger.debug("Deleting TrackHasGenre with id: {}", id);
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        try{
            TrackHasGenre trackHasGenre = em.find(TrackHasGenre.class, id);
            if (trackHasGenre == null) {
                em.getTransaction().rollback();
                return false;
            }
            em.remove(trackHasGenre);
            em.getTransaction().commit();
            logger.debug("TrackHasGenre has been deleted successfully");
            return true;
        } catch (Exception e) {
            em.getTransaction().rollback();
            logger.error("Error while deleting TrackHasGenre: {}", e.getMessage());
            throw e;
        } finally {
            if (em.isOpen()) em.close();
        }
    }

}
