package com.mediamanager.service.trackhascomposer;

import com.mediamanager.model.Track;
import com.mediamanager.model.TrackHasComposer;
import com.mediamanager.model.Composer;
import com.mediamanager.repository.TrackHasComposerRepository;
import com.mediamanager.repository.TrackRepository;
import com.mediamanager.repository.ComposerRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class TrackHasComposerService {
    private static final Logger logger = LogManager.getLogger(TrackHasComposerService.class);
    private final TrackHasComposerRepository repository;
    private final TrackRepository trackRepository;
    private final ComposerRepository composerRepository;

    public TrackHasComposerService(TrackHasComposerRepository repository, TrackRepository trackRepository, ComposerRepository composerRepository) {
        this.repository = repository;
        this.trackRepository = trackRepository;
        this.composerRepository = composerRepository;
    }

    public TrackHasComposer createTrackHasComposer(Integer trackId, Integer composerId) {
        logger.debug("Creating track has composer relationship - trackId:{}, composerId:{}", trackId, composerId);

        if (trackId == null || trackId <= 0) {
            throw new IllegalArgumentException("Track ID cannot be null or invalid");
        }
        if (composerId == null || composerId <= 0) {
            throw new IllegalArgumentException("Composer ID cannot be null or invalid");
        }

        // Verify Track exists
        Optional<Track> track = trackRepository.findById(trackId);
        if (track.isEmpty()) {
            throw new IllegalArgumentException("Track not found with id: " + trackId);
        }

        // Verify Composer exists
        Optional<Composer> composer = composerRepository.findById(composerId);
        if (composer.isEmpty()) {
            throw new IllegalArgumentException("Composer not found with id: " + composerId);
        }

        TrackHasComposer trackHasComposer = new TrackHasComposer();
        trackHasComposer.setTrack(track.get());
        trackHasComposer.setComposer(composer.get());

        return repository.save(trackHasComposer);
    }

    public List<TrackHasComposer> getAllTrackHasComposers() {
        logger.info("Getting all track has composer relationships");
        return repository.findAll();
    }

    public Optional<TrackHasComposer> getTrackHasComposerById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        logger.info("Getting track has composer by id:{}", id);
        return repository.findById(id);
    }

    public boolean deleteTrackHasComposer(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Track has composer id cannot be null");
        }
        logger.info("Deleting track has composer:{}", id);
        return repository.deleteById(id);
    }

}
