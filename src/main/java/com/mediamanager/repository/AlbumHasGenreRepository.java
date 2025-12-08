package com.mediamanager.repository;


import com.mediamanager.model.AlbumHasGenre;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.util.List;
import java.util.Optional;

public class AlbumHasGenreRepository {
    private static final Logger logger = LogManager.getLogger(AlbumHasGenreRepository.class);

    private final EntityManagerFactory entityManagerFactory;

    public AlbumHasGenreRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public AlbumHasGenre save(AlbumHasGenre albumHasGenre) {
        logger.debug("Saving AlbumHasGenre: {}", albumHasGenre);
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(albumHasGenre);
            em.getTransaction().commit();
            logger.debug("AlbumHasGenre has been saved successfully");
            return albumHasGenre;
        } catch (Exception e) {
            em.getTransaction().rollback();
            logger.error("Error while saving AlbumHasGenre: {}", e.getMessage());
            throw e;
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public List<AlbumHasGenre> findAll() {
        logger.debug("Finding All AlbumHasGenre");
        EntityManager em = entityManagerFactory.createEntityManager();
        try{
            return em.createQuery("select a from AlbumHasGenre a", AlbumHasGenre.class).getResultList();
        }finally {
            if (em.isOpen()) em.close();
        }
    }

    public Optional<AlbumHasGenre> findById(Integer id) {
        logger.debug("Finding AlbumHasGenre with id: {}", id);
        EntityManager em = entityManagerFactory.createEntityManager();
        try{
            AlbumHasGenre albumHasGenre = em.find(AlbumHasGenre.class, id);
            return Optional.ofNullable(albumHasGenre);
        }finally {
            if (em.isOpen()) em.close();
        }
    }

    public boolean deleteById(Integer id){
        logger.debug("Deleting AlbumHasGenre with id: {}", id);
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        try{
            AlbumHasGenre albumHasGenre = em.find(AlbumHasGenre.class, id);
            if (albumHasGenre == null) {
                em.getTransaction().rollback();
                return false;
            }
            em.remove(albumHasGenre);
            em.getTransaction().commit();
            logger.debug("AlbumHasGenre has been deleted successfully");
            return true;
        } catch (Exception e) {
            em.getTransaction().rollback();
            logger.error("Error while deleting AlbumHasGenre: {}", e.getMessage());
            throw e;
        } finally {
            if (em.isOpen()) em.close();
        }
    }

}
