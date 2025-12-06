package com.mediamanager.service.bitrate;


import com.mediamanager.model.BitRate;
import com.mediamanager.repository.BitRateRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class BitRateService {
    private static final Logger logger = LogManager.getLogger(BitRateService.class);
    private final BitRateRepository bitRateRepository;

    public BitRateService(BitRateRepository bitRateRepository) {
        this.bitRateRepository = bitRateRepository;
    }

    public BitRate createBitRate(String value){
        logger.info("Creating bit-rate: {}",value);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Bit-rate value cannot be empty");
        }
        BitRate bitRate = new BitRate();
        bitRate.setValue(value.trim());
        return bitRateRepository.save(bitRate);
    }

    public List<BitRate> getAllBitRates(){
        logger.info("Getting all bit-rates");
        return bitRateRepository.findAll();
    }

    public Optional<BitRate> getBitRateById(Integer id){
        logger.info("Getting bit-rate by ID: {}", id);
        return bitRateRepository.findById(id);
    }

    public Optional<BitRate> updateBitRate(Integer id, String value){
        logger.info("Updating bit-rate ID {}: {}", id, value);

        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Bit-rate value cannot be empty");
        }

        Optional<BitRate> existingBitRate = bitRateRepository.findById(id);

        if(existingBitRate.isEmpty()) {
            logger.warn("Bit-rate not found with ID: {}", id);
            return Optional.empty();
        }
        BitRate bitRate = existingBitRate.get();
        bitRate.setValue(value.trim());

        BitRate updatedBitRate = bitRateRepository.update(bitRate);
        return Optional.of(updatedBitRate);
    }

    public boolean deleteBitRate(Integer id){
        logger.info("Deleting bit-rate ID: {}", id);
        return bitRateRepository.deleteById(id);
    }

}
