package com.mediamanager.repository;

import com.mediamanager.model.Composer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class ComposerRepository {
    private static final Logger logger = LogManager.getLogger(ComposerRepository.class);

    private final EntityManagerFactory entityManagerFactory;

    public ComposerRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public Composer save(Composer composer) {
        logger.debug("Saving composer: {}", composer.getName());
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        try{
            //ToDo: Add Id Validation
            //ToDo: Add to all Repositories
            em.persist(composer);
            em.getTransaction().commit();
            logger.debug("Composer saved with IS: {}", composer.getId());
            return composer;
        } catch (Exception e) {
            em.getTransaction().rollback();
            logger.error("Error saving composer", e);
            throw e;
        }finally {
            if (em.isOpen()) em.close();
        }
    }

    public List<Composer> findAll(){
        logger.debug("Finding all composers");
        EntityManager em = entityManagerFactory.createEntityManager();
        try{
            return em.createQuery("SELECT c FROM Composer c ORDER BY c.name", Composer.class).getResultList();
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public Optional<Composer> findById(Integer id){
        logger.debug("Finding composer by ID: {}", id);
        EntityManager em = entityManagerFactory.createEntityManager();
        try{
            Composer composer = em.find(Composer.class, id);
            return Optional.ofNullable(composer);
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public Composer update(Composer composer){
        logger.debug("Updating composer ID: {}", composer.getId());
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        try{
            Composer updated = em.merge(composer);
            em.getTransaction().commit();
            logger.debug("Composer updated successfully");
            return updated;
        } catch (Exception e) {
            em.getTransaction().rollback();
            logger.error("Error updating composer", e);
            throw e;
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public boolean deleteById(Integer id){
        logger.debug("Deleting composer by ID: {}", id);
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        try{
           Composer composer = em.find(Composer.class, id);
           if (composer == null) {
               em.getTransaction().rollback();
               return false;
           }
           em.remove(composer);
           em.getTransaction().commit();
           logger.debug("Composer deleted successfully");
           return true;
        } catch (Exception e) {
            em.getTransaction().rollback();
            logger.error("Error deleting composer", e);
            throw e;
        }finally {
            if (em.isOpen()) em.close();
        }
    }
}
