package com.mediamanager.service.albumhasgenre;

import com.mediamanager.model.Album;
import com.mediamanager.model.AlbumHasGenre;
import com.mediamanager.model.Genre;
import com.mediamanager.repository.AlbumHasGenreRepository;
import com.mediamanager.repository.AlbumRepository;
import com.mediamanager.repository.GenreRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class AlbumHasGenreService {
    private static final Logger logger = LogManager.getLogger(AlbumHasGenreService.class);
    private final AlbumHasGenreRepository repository;
    private final AlbumRepository albumRepository;
    private final GenreRepository genreRepository;

    public AlbumHasGenreService(AlbumHasGenreRepository repository, AlbumRepository albumRepository, GenreRepository genreRepository) {
        this.repository = repository;
        this.albumRepository = albumRepository;
        this.genreRepository = genreRepository;
    }

    public AlbumHasGenre createAlbumHasGenre(Integer albumId, Integer genreId) {
        logger.debug("Creating album has genre relationship - albumId:{}, genreId:{}", albumId, genreId);

        if (albumId == null || albumId <= 0) {
            throw new IllegalArgumentException("Album ID cannot be null or invalid");
        }
        if (genreId == null || genreId <= 0) {
            throw new IllegalArgumentException("Genre ID cannot be null or invalid");
        }

        // Verify Album exists
        Optional<Album> album = albumRepository.findById(albumId);
        if (album.isEmpty()) {
            throw new IllegalArgumentException("Album not found with id: " + albumId);
        }

        // Verify Genre exists
        Optional<Genre> genre = genreRepository.findById(genreId);
        if (genre.isEmpty()) {
            throw new IllegalArgumentException("Genre not found with id: " + genreId);
        }

        AlbumHasGenre albumHasGenre = new AlbumHasGenre();
        albumHasGenre.setAlbum(album.get());
        albumHasGenre.setGenre(genre.get());

        return repository.save(albumHasGenre);
    }

    public List<AlbumHasGenre> getAllAlbumHasGenres() {
        logger.info("Getting all album has genre relationships");
        return repository.findAll();
    }

    public Optional<AlbumHasGenre> getAlbumHasGenreById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        logger.info("Getting album has genre by id:{}", id);
        return repository.findById(id);
    }

    public boolean deleteAlbumHasGenre(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Album has genre id cannot be null");
        }
        logger.info("Deleting album has genre:{}", id);
        return repository.deleteById(id);
    }

}
