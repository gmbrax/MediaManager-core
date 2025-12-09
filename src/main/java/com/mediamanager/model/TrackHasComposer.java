package com.mediamanager.model;

import jakarta.persistence.*;

@Entity
@Table(
    name = "trackhascomposer",
    uniqueConstraints = @UniqueConstraint(columnNames = {"fk_track_id", "fk_composer_id"})
)
public class TrackHasComposer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_track_id", nullable = false)
    private Track track;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_composer_id", nullable = false)
    private Composer composer;

    public TrackHasComposer() {}

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

    public Composer getComposer() {
        return composer;
    }

    public void setComposer(Composer composer) {
        this.composer = composer;
    }

    @Override
    public String toString() {
        return "TrackHasComposer{" +
                "id=" + id +
                ", trackId=" + (track != null ? track.getId() : null) +
                ", composerId=" + (composer != null ? composer.getId() : null) +
                '}';
    }
}
