package com.mediamanager.service.albumart;

import com.mediamanager.model.AlbumArt;
import com.mediamanager.repository.AlbumArtRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class AlbumArtService {
    private static final Logger logger = LogManager.getLogger(AlbumArtService.class);
    private final AlbumArtRepository repository;

    public AlbumArtService(AlbumArtRepository repository) {
        this.repository = repository;
    }

    public AlbumArt createAlbumArt(String filepath) {
        logger.debug("Creating album art:{}", filepath);
        if (filepath == null || filepath.trim().isEmpty()) {
            throw new IllegalArgumentException("AlbumArt filepath cannot be null or empty");
        }
        AlbumArt albumArt = new AlbumArt();
        albumArt.setFilepath(filepath);
        return repository.save(albumArt);
    }

    public List<AlbumArt> getAllAlbumArts() {
        logger.info("Getting all album arts");
        return repository.findAll();
    }

    public Optional<AlbumArt> getAlbumArtById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        logger.info("Getting album art by id:{}", id);
        return repository.findById(id);
    }

    public Optional<AlbumArt> updateAlbumArt(Integer id, String filepath) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        logger.info("Updating album art:{}", filepath);
        if (filepath == null || filepath.trim().isEmpty()) {
            throw new IllegalArgumentException("AlbumArt filepath cannot be null or empty");
        }
        Optional<AlbumArt> existingAlbumArt = repository.findById(id);
        if(existingAlbumArt.isEmpty()) {
            logger.warn("Album art not found with id:{}", id);
            return Optional.empty();
        }
        AlbumArt albumArt = existingAlbumArt.get();
        albumArt.setFilepath(filepath);
        AlbumArt updatedAlbumArt = repository.update(albumArt);
        return Optional.of(updatedAlbumArt);
    }

    public boolean deleteAlbumArt(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Album art id cannot be null");
        }
        logger.info("Deleting album art:{}", id);
        return repository.deleteById(id);
    }

}
