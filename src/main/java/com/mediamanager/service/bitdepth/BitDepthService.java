package com.mediamanager.service.bitdepth;

import com.mediamanager.model.BitDepth;
import com.mediamanager.repository.BitDepthRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class BitDepthService {
    private static final Logger logger = LogManager.getLogger(BitDepthService.class);
    private final BitDepthRepository bitDepthRepository;

    public BitDepthService(BitDepthRepository bitDepthRepository) {
        this.bitDepthRepository = bitDepthRepository;
    }
    public BitDepth createBitDepth(String value){
        logger.info("Creating bit-depth: {}", value);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Bit-depth value cannot be empty");
        }
        BitDepth bitDepth = new BitDepth();
        bitDepth.setValue(value.trim());
        return bitDepthRepository.save(bitDepth);

    }

    public List<BitDepth> getAllBitDepths(){
        logger.info("Getting all bit-depths");
        return bitDepthRepository.findAll();
    }

    public Optional<BitDepth> getBitDepthById(Integer id){
        logger.info("Getting bit-depth by ID: {}", id);
        return bitDepthRepository.findById(id);
    }

    public Optional<BitDepth> updateBitDepth(Integer id, String value){
        logger.info("Updating bit-depth ID {}: {}", id, value);

        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Bit-depth value cannot be empty");
        }
        Optional<BitDepth> existingBitDepth = bitDepthRepository.findById(id);
        if(existingBitDepth.isEmpty()){
            logger.warn("Bit-depth not found with ID: {}", id);
            return Optional.empty();
        }
        BitDepth bitDepth = existingBitDepth.get();
        bitDepth.setValue(value.trim());
        BitDepth updatedBitDepth = bitDepthRepository.update(bitDepth);
        return Optional.of(updatedBitDepth);

    }

    public boolean deleteBitDepth(Integer id){
        logger.info("Deleting bit-depth ID: {}", id);
        return bitDepthRepository.deleteById(id);
    }
}
