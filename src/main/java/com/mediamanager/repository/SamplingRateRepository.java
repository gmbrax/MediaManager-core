package com.mediamanager.repository;


import com.mediamanager.model.SamplingRate;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.util.List;
import java.util.Optional;

public class SamplingRateRepository {
    private static final Logger logger = LogManager.getLogger(SamplingRateRepository.class);

    private final EntityManagerFactory entityManagerFactory;

    public SamplingRateRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public SamplingRate save(SamplingRate samplingRate) {
        logger.debug("Saving SamplingRate: {}", samplingRate.getValue());
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(samplingRate);
            em.getTransaction().commit();
            logger.debug("SamplingRate has been saved successfully");
            return samplingRate;
        } catch (Exception e) {
            em.getTransaction().rollback();
            logger.error("Error while saving SamplingRate: {}", e.getMessage());
            throw e;
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public List<SamplingRate> findAll() {
        logger.debug("Finding All SamplingRate");
        EntityManager em = entityManagerFactory.createEntityManager();
        try{
            return em.createQuery("select s from SamplingRate s", SamplingRate.class).getResultList();
        }finally {
            if (em.isOpen()) em.close();
        }
    }

    public Optional<SamplingRate> findById(Integer id) {
        logger.debug("Finding SamplingRate with id: {}", id);
        EntityManager em = entityManagerFactory.createEntityManager();
        try{
            SamplingRate samplingRate = em.find(SamplingRate.class, id);
            return Optional.of(samplingRate);
        }finally {
            if (em.isOpen()) em.close();
        }
    }

    public SamplingRate update(SamplingRate samplingRate) {
        logger.debug("Updating SamplingRate: {}", samplingRate.getValue());
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        try {
            SamplingRate updated = em.merge(samplingRate);
            em.getTransaction().commit();
            logger.debug("SamplingRate has been updated successfully");
            return updated;
        } catch (Exception e) {
            em.getTransaction().rollback();
            logger.error("Error while updating SamplingRate: {}", e.getMessage());
            throw e;
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public boolean deleteById(Integer id){
        logger.debug("Deleting SamplingRate with id: {}", id);
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        try{
            SamplingRate samplingRate = em.find(SamplingRate.class, id);
            if (samplingRate == null) {
                em.getTransaction().rollback();
                return false;
            }
            em.remove(samplingRate);
            em.getTransaction().commit();
            logger.debug("SamplingRate has been deleted successfully");
            return true;
        } catch (Exception e) {
            em.getTransaction().rollback();
            logger.error("Error while deleting SamplingRate: {}", e.getMessage());
            throw e;
        } finally {
            if (em.isOpen()) em.close();
        }
    }

}
