package com.mediamanager.model;

import jakarta.persistence.*;

@Entity
@Table(name = "albumshasgenre")
public class AlbumHasGenre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_album_id", nullable = false)
    private Album album;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_genre_id", nullable = false)
    private Genre genre;

    public AlbumHasGenre() {}

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "AlbumHasGenre{" +
                "id=" + id +
                ", albumId=" + (album != null ? album.getId() : null) +
                ", genreId=" + (genre != null ? genre.getId() : null) +
                '}';
    }
}