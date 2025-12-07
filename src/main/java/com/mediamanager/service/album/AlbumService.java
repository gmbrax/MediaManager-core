package com.mediamanager.service.album;

import com.mediamanager.model.Album;
import com.mediamanager.model.AlbumArt;
import com.mediamanager.model.AlbumType;
import com.mediamanager.repository.AlbumRepository;
import com.mediamanager.repository.AlbumArtRepository;
import com.mediamanager.repository.AlbumTypeRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class AlbumService {
    private static final Logger logger = LogManager.getLogger(AlbumService.class);
    private final AlbumRepository repository;
    private final AlbumTypeRepository albumTypeRepository;
    private final AlbumArtRepository albumArtRepository;

    public AlbumService(AlbumRepository repository, AlbumTypeRepository albumTypeRepository, AlbumArtRepository albumArtRepository) {
        this.repository = repository;
        this.albumTypeRepository = albumTypeRepository;
        this.albumArtRepository = albumArtRepository;
    }

    public Album createAlbum(String name, Integer year, Integer numberOfDiscs, String code, Boolean isCompilation, Integer albumTypeId, Integer albumArtId) {
        logger.debug("Creating album:{}", name);
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Album name cannot be null or empty");
        }

        Album album = new Album();
        album.setName(name);
        album.setYear(year);
        album.setNumberOfDiscs(numberOfDiscs);
        album.setCode(code);
        album.setIsCompilation(isCompilation);

        // Set AlbumType if provided
        if (albumTypeId != null && albumTypeId > 0) {
            Optional<AlbumType> albumType = albumTypeRepository.findById(albumTypeId);
            if (albumType.isEmpty()) {
                throw new IllegalArgumentException("AlbumType not found with id: " + albumTypeId);
            }
            album.setAlbumType(albumType.get());
        }

        // Set AlbumArt if provided
        if (albumArtId != null && albumArtId > 0) {
            Optional<AlbumArt> albumArt = albumArtRepository.findById(albumArtId);
            if (albumArt.isEmpty()) {
                throw new IllegalArgumentException("AlbumArt not found with id: " + albumArtId);
            }
            album.setAlbumArt(albumArt.get());
        }

        return repository.save(album);
    }

    public List<Album> getAllAlbums() {
        logger.info("Getting all albums");
        return repository.findAll();
    }

    public Optional<Album> getAlbumById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        logger.info("Getting album by id:{}", id);
        return repository.findById(id);
    }

    public Optional<Album> updateAlbum(Integer id, String name, Integer year, Integer numberOfDiscs, String code, Boolean isCompilation, Integer albumTypeId, Integer albumArtId) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        logger.info("Updating album:{}", name);
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Album name cannot be null or empty");
        }

        Optional<Album> existingAlbum = repository.findById(id);
        if(existingAlbum.isEmpty()) {
            logger.warn("Album not found with id:{}", id);
            return Optional.empty();
        }

        Album album = existingAlbum.get();
        album.setName(name);
        album.setYear(year);
        album.setNumberOfDiscs(numberOfDiscs);
        album.setCode(code);
        album.setIsCompilation(isCompilation);

        // Update AlbumType if provided
        if (albumTypeId != null && albumTypeId > 0) {
            Optional<AlbumType> albumType = albumTypeRepository.findById(albumTypeId);
            if (albumType.isEmpty()) {
                throw new IllegalArgumentException("AlbumType not found with id: " + albumTypeId);
            }
            album.setAlbumType(albumType.get());
        } else {
            album.setAlbumType(null);
        }

        // Update AlbumArt if provided
        if (albumArtId != null && albumArtId > 0) {
            Optional<AlbumArt> albumArt = albumArtRepository.findById(albumArtId);
            if (albumArt.isEmpty()) {
                throw new IllegalArgumentException("AlbumArt not found with id: " + albumArtId);
            }
            album.setAlbumArt(albumArt.get());
        } else {
            album.setAlbumArt(null);
        }

        Album updatedAlbum = repository.update(album);
        return Optional.of(updatedAlbum);
    }

    public boolean deleteAlbum(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Album id cannot be null");
        }
        logger.info("Deleting album:{}", id);
        return repository.deleteById(id);
    }

}
