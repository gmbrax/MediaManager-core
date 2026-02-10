package com.mediamanager.service.track;

import com.mediamanager.model.*;
import com.mediamanager.repository.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class TrackService {
    private static final Logger logger = LogManager.getLogger(TrackService.class);
    private final TrackRepository repository;
    private final DiscRepository discRepository;
    private final ComposerRepository composerRepository;
    private final BitDepthRepository bitDepthRepository;
    private final BitRateRepository bitRateRepository;
    private final SamplingRateRepository samplingRateRepository;

    public TrackService(TrackRepository repository,
                       DiscRepository discRepository,
                       ComposerRepository composerRepository,
                       BitDepthRepository bitDepthRepository,
                       BitRateRepository bitRateRepository,
                       SamplingRateRepository samplingRateRepository) {
        this.repository = repository;
        this.discRepository = discRepository;
        this.composerRepository = composerRepository;
        this.bitDepthRepository = bitDepthRepository;
        this.bitRateRepository = bitRateRepository;
        this.samplingRateRepository = samplingRateRepository;
    }

    public Track createTrack(Integer trackNumber, String title, Integer duration,
                           String filepath, Integer discId, Integer composerId,
                           Integer bitDepthId, Integer bitRateId, Integer samplingRateId) {
        logger.debug("Creating track with title: {}", title);

        if (trackNumber == null) {
            throw new IllegalArgumentException("Track number cannot be null");
        }
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        if (filepath == null || filepath.isEmpty()) {
            throw new IllegalArgumentException("Filepath cannot be null or empty");
        }
        if (discId == null) {
            throw new IllegalArgumentException("Disc ID cannot be null");
        }

        Track track = new Track();
        track.setTrackNumber(trackNumber);
        track.setTitle(title);
        track.setDuration(duration);
        track.setFilepath(filepath);

        // Set Disc (required)
        Optional<Disc> disc = discRepository.findById(discId);
        if (disc.isEmpty()) {
            throw new IllegalArgumentException("Disc not found with id: " + discId);
        }
        track.setDisc(disc.get());

        // Set Composer (optional)
        if (composerId != null) {
            Optional<Composer> composer = composerRepository.findById(composerId);
            if (composer.isEmpty()) {
                throw new IllegalArgumentException("Composer not found with id: " + composerId);
            }
            track.setComposer(composer.get());
        }

        // Set BitDepth (optional)
        if (bitDepthId != null) {
            Optional<BitDepth> bitDepth = bitDepthRepository.findById(bitDepthId);
            if (bitDepth.isEmpty()) {
                throw new IllegalArgumentException("BitDepth not found with id: " + bitDepthId);
            }
            track.setBitDepth(bitDepth.get());
        }

        // Set BitRate (optional)
        if (bitRateId != null) {
            Optional<BitRate> bitRate = bitRateRepository.findById(bitRateId);
            if (bitRate.isEmpty()) {
                throw new IllegalArgumentException("BitRate not found with id: " + bitRateId);
            }
            track.setBitRate(bitRate.get());
        }

        // Set SamplingRate (optional)
        if (samplingRateId != null) {
            Optional<SamplingRate> samplingRate = samplingRateRepository.findById(samplingRateId);
            if (samplingRate.isEmpty()) {
                throw new IllegalArgumentException("SamplingRate not found with id: " + samplingRateId);
            }
            track.setSamplingRate(samplingRate.get());
        }

        return repository.save(track);
    }

    public List<Track> getAllTracks() {
        logger.info("Getting all tracks");
        return repository.findAll();
    }

    public Optional<Track> getTrackById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        logger.info("Getting track by id: {}", id);
        return repository.findById(id);
    }

    public Optional<Track> updateTrack(Integer id, Integer trackNumber, String title,
                                      Integer duration, String filepath, Integer discId,
                                      Integer composerId, Integer bitDepthId,
                                      Integer bitRateId, Integer samplingRateId) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        if (trackNumber == null) {
            throw new IllegalArgumentException("Track number cannot be null");
        }
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        if (filepath == null || filepath.isEmpty()) {
            throw new IllegalArgumentException("Filepath cannot be null or empty");
        }
        if (discId == null) {
            throw new IllegalArgumentException("Disc ID cannot be null");
        }

        logger.info("Updating track with id: {}", id);

        Optional<Track> existingTrack = repository.findById(id);
        if (existingTrack.isEmpty()) {
            logger.warn("Track not found with id: {}", id);
            return Optional.empty();
        }

        Track track = existingTrack.get();
        track.setTrackNumber(trackNumber);
        track.setTitle(title);
        track.setDuration(duration);
        track.setFilepath(filepath);

        // Update Disc (required)
        Optional<Disc> disc = discRepository.findById(discId);
        if (disc.isEmpty()) {
            throw new IllegalArgumentException("Disc not found with id: " + discId);
        }
        track.setDisc(disc.get());

        // Update Composer (optional)
        if (composerId != null) {
            Optional<Composer> composer = composerRepository.findById(composerId);
            if (composer.isEmpty()) {
                throw new IllegalArgumentException("Composer not found with id: " + composerId);
            }
            track.setComposer(composer.get());
        } else {
            track.setComposer(null);
        }

        // Update BitDepth (optional)
        if (bitDepthId != null) {
            Optional<BitDepth> bitDepth = bitDepthRepository.findById(bitDepthId);
            if (bitDepth.isEmpty()) {
                throw new IllegalArgumentException("BitDepth not found with id: " + bitDepthId);
            }
            track.setBitDepth(bitDepth.get());
        } else {
            track.setBitDepth(null);
        }

        // Update BitRate (optional)
        if (bitRateId != null) {
            Optional<BitRate> bitRate = bitRateRepository.findById(bitRateId);
            if (bitRate.isEmpty()) {
                throw new IllegalArgumentException("BitRate not found with id: " + bitRateId);
            }
            track.setBitRate(bitRate.get());
        } else {
            track.setBitRate(null);
        }

        // Update SamplingRate (optional)
        if (samplingRateId != null) {
            Optional<SamplingRate> samplingRate = samplingRateRepository.findById(samplingRateId);
            if (samplingRate.isEmpty()) {
                throw new IllegalArgumentException("SamplingRate not found with id: " + samplingRateId);
            }
            track.setSamplingRate(samplingRate.get());
        } else {
            track.setSamplingRate(null);
        }

        Track updatedTrack = repository.update(track);
        return Optional.of(updatedTrack);
    }

    public boolean deleteTrack(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Track id cannot be null");
        }
        logger.info("Deleting track: {}", id);
        return repository.deleteById(id);
    }
}
