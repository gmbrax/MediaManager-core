package com.mediamanager.model;

import jakarta.persistence.*;

@Entity
@Table(name = "track")
public class Track {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "track_number")
    private Integer trackNumber;

    @Column
    private String title;

    @Column
    private Integer duration;

    @Column
    private String filepath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_disc_id")
    private Disc disc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_composer_id")
    private Composer composer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_bit_depth_id")
    private BitDepth bitDepth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_bit_rate_id")
    private BitRate bitRate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_sampling_rate_id")
    private SamplingRate samplingRate;

    public Track() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(Integer trackNumber) {
        this.trackNumber = trackNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public Disc getDisc() {
        return disc;
    }

    public void setDisc(Disc disc) {
        this.disc = disc;
    }

    public Composer getComposer() {
        return composer;
    }

    public void setComposer(Composer composer) {
        this.composer = composer;
    }

    public BitDepth getBitDepth() {
        return bitDepth;
    }

    public void setBitDepth(BitDepth bitDepth) {
        this.bitDepth = bitDepth;
    }

    public BitRate getBitRate() {
        return bitRate;
    }

    public void setBitRate(BitRate bitRate) {
        this.bitRate = bitRate;
    }

    public SamplingRate getSamplingRate() {
        return samplingRate;
    }

    public void setSamplingRate(SamplingRate samplingRate) {
        this.samplingRate = samplingRate;
    }
}
