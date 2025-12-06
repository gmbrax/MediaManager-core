package com.mediamanager.service.genre;

import com.mediamanager.model.Genre;
import com.mediamanager.repository.GenreRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

/**
 * Service para lógica de negócio relacionada a Genre
 */
public class GenreService {
    private static final Logger logger = LogManager.getLogger(GenreService.class);

    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    /**
     * Cria um novo genre
     */
    public Genre createGenre(String name) {
        logger.info("Creating genre: {}", name);

        // Aqui poderia ter validações de negócio
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Genre name cannot be empty");
        }

        Genre genre = new Genre();
        genre.setName(name.trim());

        return genreRepository.save(genre);
    }

    /**
     * Busca todos os genres
     */
    public List<Genre> getAllGenres() {
        logger.info("Getting all genres");
        return genreRepository.findAll();
    }

    /**
     * Busca genre por ID
     */
    public Optional<Genre> getGenreById(Integer id) {
        logger.info("Getting genre by ID: {}", id);
        return genreRepository.findById(id);
    }

    /**
     * Atualiza um genre existente
     */
    public Optional<Genre> updateGenre(Integer id, String name) {
        logger.info("Updating genre ID {}: {}", id, name);

        // Validação
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Genre name cannot be empty");
        }

        Optional<Genre> existingGenre = genreRepository.findById(id);

        if (existingGenre.isEmpty()) {
            logger.warn("Genre not found with ID: {}", id);
            return Optional.empty();
        }

        Genre genre = existingGenre.get();
        genre.setName(name.trim());

        Genre updated = genreRepository.update(genre);
        return Optional.of(updated);
    }

    /**
     * Deleta um genre por ID
     */
    public boolean deleteGenre(Integer id) {
        logger.info("Deleting genre ID: {}", id);
        return genreRepository.deleteById(id);
    }
}