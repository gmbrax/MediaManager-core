package com.mediamanager.model;

import jakarta.persistence.*;

@Entity
@Table(name = "trackhasgenre")
public class TrackHasGenre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_track_id", nullable = false)
    private Track track;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_genre_id", nullable = false)
    private Genre genre;

    public TrackHasGenre() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "TrackHasGenre{" +
                "id=" + id +
                ", trackId=" + (track != null ? track.getId() : null) +
                ", genreId=" + (genre != null ? genre.getId() : null) +
                '}';
    }
}
