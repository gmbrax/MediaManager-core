package com.mediamanager.service.albumtype;

import com.mediamanager.model.AlbumType;
import com.mediamanager.repository.AlbumTypeRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class AlbumTypeService {
    private static final Logger logger = LogManager.getLogger(AlbumTypeService.class);
    private final AlbumTypeRepository repository;

    public AlbumTypeService(AlbumTypeRepository repository) {
        this.repository = repository;
    }

    public AlbumType createAlbumType(String value) {
        logger.debug("Creating album type:{}", value);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("AlbumType value cannot be null or empty");
        }
        AlbumType albumType = new AlbumType();
        albumType.setValue(value);
        return repository.save(albumType);
    }

    public List<AlbumType> getAllAlbumTypes() {
        logger.info("Getting all album types");
        return repository.findAll();
    }

    public Optional<AlbumType> getAlbumTypeById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        logger.info("Getting album type by id:{}", id);
        return repository.findById(id);
    }

    public Optional<AlbumType> updateAlbumType(Integer id, String value) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        logger.info("Updating album type:{}", value);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("AlbumType value cannot be null or empty");
        }
        Optional<AlbumType> existingAlbumType = repository.findById(id);
        if(existingAlbumType.isEmpty()) {
            logger.warn("Album type not found with id:{}", id);
            return Optional.empty();
        }
        AlbumType albumType = existingAlbumType.get();
        albumType.setValue(value);
        AlbumType updatedAlbumType = repository.update(albumType);
        return Optional.of(updatedAlbumType);
    }

    public boolean deleteAlbumType(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Album type id cannot be null");
        }
        logger.info("Deleting album type:{}", id);
        return repository.deleteById(id);
    }

}
