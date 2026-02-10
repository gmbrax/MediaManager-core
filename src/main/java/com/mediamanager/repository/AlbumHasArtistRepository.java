package com.mediamanager.repository;


import com.mediamanager.model.AlbumHasArtist;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.util.List;
import java.util.Optional;

public class AlbumHasArtistRepository {
    private static final Logger logger = LogManager.getLogger(AlbumHasArtistRepository.class);

    private final EntityManagerFactory entityManagerFactory;

    public AlbumHasArtistRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public AlbumHasArtist save(AlbumHasArtist albumHasArtist) {
        logger.debug("Saving AlbumHasArtist: {}", albumHasArtist);
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(albumHasArtist);
            em.getTransaction().commit();
            logger.debug("AlbumHasArtist has been saved successfully");
            return albumHasArtist;
        } catch (Exception e) {
            em.getTransaction().rollback();
            logger.error("Error while saving AlbumHasArtist: {}", e.getMessage());
            throw e;
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public List<AlbumHasArtist> findAll() {
        logger.debug("Finding All AlbumHasArtist");
        EntityManager em = entityManagerFactory.createEntityManager();
        try{
            return em.createQuery("select a from AlbumHasArtist a", AlbumHasArtist.class).getResultList();
        }finally {
            if (em.isOpen()) em.close();
        }
    }

    public Optional<AlbumHasArtist> findById(Integer id) {
        logger.debug("Finding AlbumHasArtist with id: {}", id);
        EntityManager em = entityManagerFactory.createEntityManager();
        try{
            AlbumHasArtist albumHasArtist = em.find(AlbumHasArtist.class, id);
            return Optional.ofNullable(albumHasArtist);
        }finally {
            if (em.isOpen()) em.close();
        }
    }

    public boolean deleteById(Integer id){
        logger.debug("Deleting AlbumHasArtist with id: {}", id);
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        try{
            AlbumHasArtist albumHasArtist = em.find(AlbumHasArtist.class, id);
            if (albumHasArtist == null) {
                em.getTransaction().rollback();
                return false;
            }
            em.remove(albumHasArtist);
            em.getTransaction().commit();
            logger.debug("AlbumHasArtist has been deleted successfully");
            return true;
        } catch (Exception e) {
            em.getTransaction().rollback();
            logger.error("Error while deleting AlbumHasArtist: {}", e.getMessage());
            throw e;
        } finally {
            if (em.isOpen()) em.close();
        }
    }

}
