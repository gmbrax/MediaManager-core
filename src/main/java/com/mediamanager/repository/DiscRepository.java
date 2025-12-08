package com.mediamanager.repository;

import com.mediamanager.model.Disc;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class DiscRepository {
    private static final Logger logger = LogManager.getLogger(DiscRepository.class);

    private final EntityManagerFactory entityManagerFactory;

    public DiscRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public Disc save(Disc disc) {
        logger.debug("Saving Disc: {}", disc.getDiscNumber());
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(disc);
            em.getTransaction().commit();
            logger.debug("Disc has been saved successfully");
            return disc;
        } catch (Exception e) {
            em.getTransaction().rollback();
            logger.error("Error while saving Disc: {}", e.getMessage());
            throw e;
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public List<Disc> findAll() {
        logger.debug("Finding All Disc");
        EntityManager em = entityManagerFactory.createEntityManager();
        try{
            return em.createQuery("select d from Disc d", Disc.class).getResultList();
        }finally {
            if (em.isOpen()) em.close();
        }
    }

    public Optional<Disc> findById(Integer id) {
        logger.debug("Finding Disc with id: {}", id);
        EntityManager em = entityManagerFactory.createEntityManager();
        try{
            Disc disc = em.find(Disc.class, id);
            return Optional.ofNullable(disc);
        }finally {
            if (em.isOpen()) em.close();
        }
    }

    public Disc update(Disc disc) {
        logger.debug("Updating Disc: {}", disc.getDiscNumber());
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        try {
            Disc updated = em.merge(disc);
            em.getTransaction().commit();
            logger.debug("Disc has been updated successfully");
            return updated;
        } catch (Exception e) {
            em.getTransaction().rollback();
            logger.error("Error while updating Disc: {}", e.getMessage());
            throw e;
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public boolean deleteById(Integer id){
        logger.debug("Deleting Disc with id: {}", id);
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        try{
            Disc disc = em.find(Disc.class, id);
            if (disc == null) {
                em.getTransaction().rollback();
                return false;
            }
            em.remove(disc);
            em.getTransaction().commit();
            logger.debug("Disc has been deleted successfully");
            return true;
        } catch (Exception e) {
            em.getTransaction().rollback();
            logger.error("Error while deleting Disc: {}", e.getMessage());
            throw e;
        } finally {
            if (em.isOpen()) em.close();
        }
    }
}
