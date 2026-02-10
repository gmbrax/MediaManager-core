package com.mediamanager.model;

import jakarta.persistence.*;

@Entity
@Table(
    name = "trackhasartist",
    uniqueConstraints = @UniqueConstraint(columnNames = {"fk_track_id", "fk_artist_id"})
)
public class TrackHasArtist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_track_id", nullable = false)
    private Track track;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_artist_id", nullable = false)
    private Artist artist;

    public TrackHasArtist() {}

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

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    @Override
    public String toString() {
        return "TrackHasArtist{" +
                "id=" + id +
                ", trackId=" + (track != null ? track.getId() : null) +
                ", artistId=" + (artist != null ? artist.getId() : null) +
                '}';
    }
}
