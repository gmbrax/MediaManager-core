package com.mediamanager.repository;

import com.mediamanager.model.BitDepth;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class BitDepthRepository {
    private static final Logger logger = LogManager.getLogger(BitDepthRepository.class);

    private final EntityManagerFactory entityManagerFactory;

    public BitDepthRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public BitDepth save(BitDepth bitDepth) {
        logger.debug("Saving BitDepth: {}", bitDepth.getValue());
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(bitDepth);
            em.getTransaction().commit();
            logger.debug("BitDepth saved with ID: {}", bitDepth.getId());
            return bitDepth;
        } catch (Exception e) {
            em.getTransaction().rollback();
            logger.error("Error saving BitDepth", e);
            throw e;
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public List<BitDepth> findAll(){
        logger.debug("Finding all BitDepths");
        EntityManager em = entityManagerFactory.createEntityManager();
        try{
            return em.createQuery("SELECT b FROM BitDepth b ORDER BY b.value", BitDepth.class).getResultList();
        }finally {
            if (em.isOpen()) em.close();
        }
    }

    public Optional<BitDepth> findById(Integer id){
        logger.debug("Finding BitDepth by ID: {}", id);
        EntityManager em = entityManagerFactory.createEntityManager();
        try{
            BitDepth bitDepth = em.find(BitDepth.class, id);
            return Optional.ofNullable(bitDepth);
        }finally {
            if (em.isOpen()) em.close();
        }
    }

    public BitDepth update(BitDepth bitDepth){
        logger.debug("Updating BitDepth ID: {}", bitDepth.getId());
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        try{
            BitDepth updated = em.merge(bitDepth);
            em.getTransaction().commit();
            logger.debug("BitDepth updated successfully");
            return updated;
        }catch (Exception e){
            em.getTransaction().rollback();
            logger.error("Error updating BitDepth", e);
            throw e;
        }finally {
            if (em.isOpen()) em.close();
        }
    }

    public boolean deleteById(Integer id){
        logger.debug("Deleting BitDepth by ID: {}", id);
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        try{
           BitDepth bitDepth = em.find(BitDepth.class, id);
           if (bitDepth == null) {
               em.getTransaction().rollback();
               return false;
           }
           em.remove(bitDepth);
           em.getTransaction().commit();
           logger.debug("BitDepth deleted successfully");
           return true;
        } catch (Exception e) {
            em.getTransaction().rollback();
            logger.error("Error deleting BitDepth", e);
            throw e;
        }finally {
            if (em.isOpen()) em.close();
        }
    }
}
