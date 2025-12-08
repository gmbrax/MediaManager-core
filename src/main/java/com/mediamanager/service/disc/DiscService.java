package com.mediamanager.service.disc;

import com.mediamanager.model.Album;
import com.mediamanager.model.Disc;
import com.mediamanager.repository.AlbumRepository;
import com.mediamanager.repository.DiscRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class DiscService {
    private static final Logger logger = LogManager.getLogger(DiscService.class);
    private final DiscRepository repository;
    private final AlbumRepository albumRepository;

    public DiscService(DiscRepository repository, AlbumRepository albumRepository) {
        this.repository = repository;
        this.albumRepository = albumRepository;
    }

    public Disc createDisc(Integer discNumber, Integer albumId) {
        logger.debug("Creating disc with number: {}", discNumber);
        if (discNumber == null) {
            throw new IllegalArgumentException("Disc number cannot be null");
        }
        if (albumId == null) {
            throw new IllegalArgumentException("Album ID cannot be null");
        }

        Disc disc = new Disc();
        disc.setDiscNumber(discNumber);

        // Set Album (required)
        Optional<Album> album = albumRepository.findById(albumId);
        if (album.isEmpty()) {
            throw new IllegalArgumentException("Album not found with id: " + albumId);
        }
        disc.setAlbum(album.get());

        return repository.save(disc);
    }

    public List<Disc> getAllDiscs() {
        logger.info("Getting all discs");
        return repository.findAll();
    }

    public Optional<Disc> getDiscById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        logger.info("Getting disc by id: {}", id);
        return repository.findById(id);
    }

    public Optional<Disc> updateDisc(Integer id, Integer discNumber, Integer albumId) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        if (discNumber == null) {
            throw new IllegalArgumentException("Disc number cannot be null");
        }
        if (albumId == null) {
            throw new IllegalArgumentException("Album ID cannot be null");
        }

        logger.info("Updating disc with id: {}", id);

        Optional<Disc> existingDisc = repository.findById(id);
        if (existingDisc.isEmpty()) {
            logger.warn("Disc not found with id: {}", id);
            return Optional.empty();
        }

        Disc disc = existingDisc.get();
        disc.setDiscNumber(discNumber);

        // Update Album
        Optional<Album> album = albumRepository.findById(albumId);
        if (album.isEmpty()) {
            throw new IllegalArgumentException("Album not found with id: " + albumId);
        }
        disc.setAlbum(album.get());

        Disc updatedDisc = repository.update(disc);
        return Optional.of(updatedDisc);
    }

    public boolean deleteDisc(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Disc id cannot be null");
        }
        logger.info("Deleting disc: {}", id);
        return repository.deleteById(id);
    }
}
