package com.mediamanager.service.artist;

import com.mediamanager.model.Artist;
import com.mediamanager.repository.ArtistRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class ArtistService {
    private static final Logger logger = LogManager.getLogger(ArtistService.class);
    private final ArtistRepository artistRepository;

    public ArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    public Artist createArtist(String name){
        logger.info("Creating artist: {}", name);
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Artist name cannot be empty");
        }
        Artist artist = new Artist();
        artist.setName(name.trim());
        return artistRepository.save(artist);
    }

    public List<Artist> getAllArtists(){
        logger.info("Getting all artists");
        return artistRepository.findAll();
    }

    public Optional<Artist> getArtistById(Integer id){
        logger.info("Getting artist by ID: {}", id);
        return artistRepository.findById(id);
    }

    public Optional<Artist> updateArtist(Integer id, String name){
        logger.info("Updating artist ID {}: {}", id, name);

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Artist name cannot be empty");
        }

        Optional<Artist> existingArtist = artistRepository.findById(id);

        if(existingArtist.isEmpty()){
            logger.warn("Artist not found with ID: {}", id);
            return Optional.empty();
        }
        Artist artist = existingArtist.get();
        artist.setName(name.trim());
        Artist updatedArtist = artistRepository.update(artist);
        return Optional.of(updatedArtist);
    }

    public boolean deleteArtist(Integer id){
        logger.info("Deleting artist ID: {}", id);
        return artistRepository.deleteById(id);
    }



}
