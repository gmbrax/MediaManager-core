package com.mediamanager.repository;

import com.mediamanager.model.Track;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class TrackRepository {
    private static final Logger logger = LogManager.getLogger(TrackRepository.class);

    private final EntityManagerFactory entityManagerFactory;

    public TrackRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public Track save(Track track) {
        logger.debug("Saving Track: {}", track.getTitle());
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(track);
            em.getTransaction().commit();
            logger.debug("Track has been saved successfully");
            return track;
        } catch (Exception e) {
            em.getTransaction().rollback();
            logger.error("Error while saving Track: {}", e.getMessage());
            throw e;
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public List<Track> findAll() {
        logger.debug("Finding All Track");
        EntityManager em = entityManagerFactory.createEntityManager();
        try{
            return em.createQuery("select t from Track t", Track.class).getResultList();
        }finally {
            if (em.isOpen()) em.close();
        }
    }

    public Optional<Track> findById(Integer id) {
        logger.debug("Finding Track with id: {}", id);
        EntityManager em = entityManagerFactory.createEntityManager();
        try{
            Track track = em.find(Track.class, id);
            return Optional.ofNullable(track);
        }finally {
            if (em.isOpen()) em.close();
        }
    }

    public Track update(Track track) {
        logger.debug("Updating Track: {}", track.getTitle());
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        try {
            Track updated = em.merge(track);
            em.getTransaction().commit();
            logger.debug("Track has been updated successfully");
            return updated;
        } catch (Exception e) {
            em.getTransaction().rollback();
            logger.error("Error while updating Track: {}", e.getMessage());
            throw e;
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public boolean deleteById(Integer id){
        logger.debug("Deleting Track with id: {}", id);
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        try{
            Track track = em.find(Track.class, id);
            if (track == null) {
                em.getTransaction().rollback();
                return false;
            }
            em.remove(track);
            em.getTransaction().commit();
            logger.debug("Track has been deleted successfully");
            return true;
        } catch (Exception e) {
            em.getTransaction().rollback();
            logger.error("Error while deleting Track: {}", e.getMessage());
            throw e;
        } finally {
            if (em.isOpen()) em.close();
        }
    }
}
