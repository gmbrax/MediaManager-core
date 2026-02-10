package com.mediamanager.model;

import jakarta.persistence.*;

@Entity
@Table(name = "albumshasartist")
public class AlbumHasArtist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_album_id", nullable = false)
    private Album album;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_artist_id", nullable = false)
    private Artist artist;

    public AlbumHasArtist() {}

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

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    @Override
    public String toString() {
        return "AlbumHasArtist{" +
                "id=" + id +
                ", albumId=" + (album != null ? album.getId() : null) +
                ", artistId=" + (artist != null ? artist.getId() : null) +
                '}';
    }
}
