package com.mediamanager.service.samplingrate;

import com.mediamanager.model.SamplingRate;
import com.mediamanager.repository.SamplingRateRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class SamplingRateService {
    private static final Logger logger = LogManager.getLogger(SamplingRateService.class);
    private final SamplingRateRepository repository;

    public SamplingRateService(SamplingRateRepository repository) {
        this.repository = repository;
    }

    public SamplingRate createSamplingRate(String value) {
        logger.debug("Creating sampling rate:{}", value);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Sampling-Rate value cannot be null or empty");
        }
        SamplingRate samplingRate = new SamplingRate();
        samplingRate.setValue(value);
        return repository.save(samplingRate);
    }

    public List<SamplingRate> getAllSamplingRates() {
        logger.info("Getting all sampling rates");
        return repository.findAll();
    }

    public Optional<SamplingRate> getSamplingRateById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        logger.info("Getting sampling rate by id:{}", id);
        return repository.findById(id);
    }

    public Optional<SamplingRate> updateSamplingRate(Integer id, String value) {
    logger.info("Updating sampling rate:{}", value);
    if (value == null || value.trim().isEmpty()) {
    throw new IllegalArgumentException("Sampling-Rate value cannot be null or empty");}
    Optional<SamplingRate> existingSamplingRate = repository.findById(id);
    if(existingSamplingRate.isEmpty()) {
        logger.warn("Sampling rate not found with id:{}", id);
        return Optional.empty();
    }
    SamplingRate samplingRate = existingSamplingRate.get();
    samplingRate.setValue(value);
    SamplingRate updatedSamplingRate = repository.update(samplingRate);
    return Optional.of(updatedSamplingRate);
    }

    public boolean deleteSamplingRate(Integer id) {
        logger.info("Deleting sampling rate:{}", id);
        return repository.deleteById(id);
    }

}
