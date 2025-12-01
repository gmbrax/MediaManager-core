package com.mediamanager.service.composer;

import com.mediamanager.model.Composer;
import com.mediamanager.repository.ComposerRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class ComposerService {
    private static final Logger logger = LogManager.getLogger(ComposerService.class);

    private final ComposerRepository composerRepository;

    public ComposerService(ComposerRepository composerRepository) {
        this.composerRepository = composerRepository;
    }

    public Composer createComposer(String name) {
        logger.info("Creating composer: {}", name);
        if(name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Composer name cannot be empty");
        }
        Composer composer = new Composer();
        composer.setName(name.trim());
        return composerRepository.save(composer);


    }

    public List<Composer> getAllComposers() {
        logger.info("Getting all composers");
        return composerRepository.findAll();
    }

    public Optional<Composer> getComposerById(Integer id) {
        logger.info("Getting composer by ID: {}", id);
        return composerRepository.findById(id);
    }

    public Optional<Composer> updateComposer(Integer id, String name) {
        logger.info("Updating composer ID {}: {}", id, name);

        if(name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Composer name cannot be empty");
        }

        Optional<Composer> existingComposer = composerRepository.findById(id);

        if(existingComposer.isEmpty()) {
            logger.warn("Composer not found with ID: {}", id);
            return Optional.empty();
        }

        Composer composer = existingComposer.get();
        composer.setName(name.trim());

        Composer updated = composerRepository.update(composer);
        return Optional.of(updated);

    }

    public boolean deleteComposer(Integer id) {
        logger.info("Deleting composer ID: {}", id);
        return composerRepository.deleteById(id);
    }


}
