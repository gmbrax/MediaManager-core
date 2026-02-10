package com.mediamanager.model;

import jakarta.persistence.*;

@Entity
@Table(name = "disc")
public class Disc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "disc_number", nullable = false)
    private Integer discNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_album_id",nullable = false)
    private Album album;

    public Disc() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDiscNumber() {
        return discNumber;
    }

    public void setDiscNumber(Integer discNumber) {
        this.discNumber = discNumber;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }
}
