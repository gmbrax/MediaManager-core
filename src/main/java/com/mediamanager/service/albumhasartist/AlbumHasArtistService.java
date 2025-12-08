package com.mediamanager.service.albumhasartist;

import com.mediamanager.model.Album;
import com.mediamanager.model.AlbumHasArtist;
import com.mediamanager.model.Artist;
import com.mediamanager.repository.AlbumHasArtistRepository;
import com.mediamanager.repository.AlbumRepository;
import com.mediamanager.repository.ArtistRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class AlbumHasArtistService {
    private static final Logger logger = LogManager.getLogger(AlbumHasArtistService.class);
    private final AlbumHasArtistRepository repository;
    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;

    public AlbumHasArtistService(AlbumHasArtistRepository repository, AlbumRepository albumRepository, ArtistRepository artistRepository) {
        this.repository = repository;
        this.albumRepository = albumRepository;
        this.artistRepository = artistRepository;
    }

    public AlbumHasArtist createAlbumHasArtist(Integer albumId, Integer artistId) {
        logger.debug("Creating album has artist relationship - albumId:{}, artistId:{}", albumId, artistId);

        if (albumId == null || albumId <= 0) {
            throw new IllegalArgumentException("Album ID cannot be null or invalid");
        }
        if (artistId == null || artistId <= 0) {
            throw new IllegalArgumentException("Artist ID cannot be null or invalid");
        }

        // Verify Album exists
        Optional<Album> album = albumRepository.findById(albumId);
        if (album.isEmpty()) {
            throw new IllegalArgumentException("Album not found with id: " + albumId);
        }

        // Verify Artist exists
        Optional<Artist> artist = artistRepository.findById(artistId);
        if (artist.isEmpty()) {
            throw new IllegalArgumentException("Artist not found with id: " + artistId);
        }

        AlbumHasArtist albumHasArtist = new AlbumHasArtist();
        albumHasArtist.setAlbum(album.get());
        albumHasArtist.setArtist(artist.get());

        return repository.save(albumHasArtist);
    }

    public List<AlbumHasArtist> getAllAlbumHasArtists() {
        logger.info("Getting all album has artist relationships");
        return repository.findAll();
    }

    public Optional<AlbumHasArtist> getAlbumHasArtistById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        logger.info("Getting album has artist by id:{}", id);
        return repository.findById(id);
    }

    public boolean deleteAlbumHasArtist(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Album has artist id cannot be null");
        }
        logger.info("Deleting album has artist:{}", id);
        return repository.deleteById(id);
    }

}
