package com.mediamanager.service.trackhasartist;

import com.mediamanager.model.Track;
import com.mediamanager.model.TrackHasArtist;
import com.mediamanager.model.Artist;
import com.mediamanager.repository.TrackHasArtistRepository;
import com.mediamanager.repository.TrackRepository;
import com.mediamanager.repository.ArtistRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class TrackHasArtistService {
    private static final Logger logger = LogManager.getLogger(TrackHasArtistService.class);
    private final TrackHasArtistRepository repository;
    private final TrackRepository trackRepository;
    private final ArtistRepository artistRepository;

    public TrackHasArtistService(TrackHasArtistRepository repository, TrackRepository trackRepository, ArtistRepository artistRepository) {
        this.repository = repository;
        this.trackRepository = trackRepository;
        this.artistRepository = artistRepository;
    }

    public TrackHasArtist createTrackHasArtist(Integer trackId, Integer artistId) {
        logger.debug("Creating track has artist relationship - trackId:{}, artistId:{}", trackId, artistId);

        if (trackId == null || trackId <= 0) {
            throw new IllegalArgumentException("Track ID cannot be null or invalid");
        }
        if (artistId == null || artistId <= 0) {
            throw new IllegalArgumentException("Artist ID cannot be null or invalid");
        }

        // Verify Track exists
        Optional<Track> track = trackRepository.findById(trackId);
        if (track.isEmpty()) {
            throw new IllegalArgumentException("Track not found with id: " + trackId);
        }

        // Verify Artist exists
        Optional<Artist> artist = artistRepository.findById(artistId);
        if (artist.isEmpty()) {
            throw new IllegalArgumentException("Artist not found with id: " + artistId);
        }

        TrackHasArtist trackHasArtist = new TrackHasArtist();
        trackHasArtist.setTrack(track.get());
        trackHasArtist.setArtist(artist.get());

        return repository.save(trackHasArtist);
    }

    public List<TrackHasArtist> getAllTrackHasArtists() {
        logger.info("Getting all track has artist relationships");
        return repository.findAll();
    }

    public Optional<TrackHasArtist> getTrackHasArtistById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        logger.info("Getting track has artist by id:{}", id);
        return repository.findById(id);
    }

    public boolean deleteTrackHasArtist(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Track has artist id cannot be null");
        }
        logger.info("Deleting track has artist:{}", id);
        return repository.deleteById(id);
    }

}
