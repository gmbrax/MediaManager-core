package com.mediamanager.repository;

import com.mediamanager.model.BitRate;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class BitRateRepository {
        private static final Logger logger = LogManager.getLogger(BitRateRepository.class);

        private final EntityManagerFactory entityManagerFactory;

        public BitRateRepository(EntityManagerFactory entityManagerFactory) {
            this.entityManagerFactory = entityManagerFactory;
        }

        public BitRate save(BitRate bitRate) {
            logger.debug("Saving BitRate: {}", bitRate.getValue());
            EntityManager em = entityManagerFactory.createEntityManager();
            em.getTransaction().begin();
            try {
                em.persist(bitRate);
                em.getTransaction().commit();
                logger.debug("BitRate saved with ID: {}", bitRate.getId());
                return bitRate;
            } catch (Exception e) {
                em.getTransaction().rollback();
                logger.error("Error saving BitRate", e);
                throw e;
            } finally {
                if (em.isOpen()) em.close();
            }
        }

     public List<BitRate> findAll(){
            logger.debug("Finding all BitRates");
            EntityManager em = entityManagerFactory.createEntityManager();
            try{
                return em.createQuery("SELECT b FROM BitRate b ORDER BY b.value", BitRate.class).getResultList();
            }finally {
                if (em.isOpen()) em.close();
            }
     }

     public Optional<BitRate> findById(Integer id){
        logger.debug("Finding BitRate by ID: {}", id);
        EntityManager em = entityManagerFactory.createEntityManager();
        try{
            BitRate bitRate = em.find(BitRate.class, id);
            return Optional.ofNullable(bitRate);
        } finally {
            if (em.isOpen()) em.close();
        }
     }

     public BitRate update(BitRate bitRate){
            logger.debug("Updating BitRate ID: {}", bitRate.getId());
             EntityManager em = entityManagerFactory.createEntityManager();
             em.getTransaction().begin();
             try{
                 BitRate updated = em.merge(bitRate);
                 em.getTransaction().commit();
                 logger.debug("BitRate updated with ID: {}", bitRate.getId());
                 return updated;
             } catch (Exception e) {
                 em.getTransaction().rollback();
                 logger.error("Error updating BitRate", e);
                 throw e;
             } finally {
                 if (em.isOpen()) em.close();
             }
     }

     public boolean deleteById(Integer id){
        logger.debug("Deleting BitRate by ID: {}", id);
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        try{
            BitRate bitRate = em.find(BitRate.class, id);
            if (bitRate == null) {
                em.getTransaction().rollback();
                return false;
            }
            em.remove(bitRate);
            em.getTransaction().commit();
            logger.debug("BitRate deleted successfully");
            return true;
        } catch (Exception e) {
            em.getTransaction().rollback();
            logger.error("Error deleting BitRate", e);
            throw e;
        } finally {
            if (em.isOpen()) em.close();
        }

     }
}
