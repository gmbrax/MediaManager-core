package com.mediamanager.service.trackhasgenre;

import com.mediamanager.model.Track;
import com.mediamanager.model.TrackHasGenre;
import com.mediamanager.model.Genre;
import com.mediamanager.repository.TrackHasGenreRepository;
import com.mediamanager.repository.TrackRepository;
import com.mediamanager.repository.GenreRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class TrackHasGenreService {
    private static final Logger logger = LogManager.getLogger(TrackHasGenreService.class);
    private final TrackHasGenreRepository repository;
    private final TrackRepository trackRepository;
    private final GenreRepository genreRepository;

    public TrackHasGenreService(TrackHasGenreRepository repository, TrackRepository trackRepository, GenreRepository genreRepository) {
        this.repository = repository;
        this.trackRepository = trackRepository;
        this.genreRepository = genreRepository;
    }

    public TrackHasGenre createTrackHasGenre(Integer trackId, Integer genreId) {
        logger.debug("Creating track has genre relationship - trackId:{}, genreId:{}", trackId, genreId);

        if (trackId == null || trackId <= 0) {
            throw new IllegalArgumentException("Track ID cannot be null or invalid");
        }
        if (genreId == null || genreId <= 0) {
            throw new IllegalArgumentException("Genre ID cannot be null or invalid");
        }

        // Verify Track exists
        Optional<Track> track = trackRepository.findById(trackId);
        if (track.isEmpty()) {
            throw new IllegalArgumentException("Track not found with id: " + trackId);
        }

        // Verify Genre exists
        Optional<Genre> genre = genreRepository.findById(genreId);
        if (genre.isEmpty()) {
            throw new IllegalArgumentException("Genre not found with id: " + genreId);
        }

        TrackHasGenre trackHasGenre = new TrackHasGenre();
        trackHasGenre.setTrack(track.get());
        trackHasGenre.setGenre(genre.get());

        return repository.save(trackHasGenre);
    }

    public List<TrackHasGenre> getAllTrackHasGenres() {
        logger.info("Getting all track has genre relationships");
        return repository.findAll();
    }

    public Optional<TrackHasGenre> getTrackHasGenreById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        logger.info("Getting track has genre by id:{}", id);
        return repository.findById(id);
    }

    public boolean deleteTrackHasGenre(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Track has genre id cannot be null");
        }
        logger.info("Deleting track has genre:{}", id);
        return repository.deleteById(id);
    }

}
