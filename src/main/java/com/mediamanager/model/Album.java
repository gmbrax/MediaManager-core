package com.mediamanager.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "album")
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column
    private Integer year;

    @Column(name = "number_of_discs")
    private Integer numberOfDiscs;

    @Column
    private String code;

    @Column(name = "is_compilation")
    private Boolean isCompilation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_albumtype_id")
    private AlbumType albumType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_albumart_id")
    private AlbumArt albumArt;

    // Relacionamento ManyToMany com Artist através da tabela de junção
    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AlbumHasArtist> albumArtists = new ArrayList<>();

    // Relacionamento ManyToMany com Genre através da tabela de junção
    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AlbumHasGenre> albumGenres = new ArrayList<>();  // ← ADICIONE ESSA LINHA

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Album() {}

    // Métodos helper para Artist (ManyToMany)
    public void addArtist(Artist artist) {
        AlbumHasArtist albumHasArtist = new AlbumHasArtist();
        albumHasArtist.setAlbum(this);
        albumHasArtist.setArtist(artist);
        albumArtists.add(albumHasArtist);
    }

    public void removeArtist(Artist artist) {
        albumArtists.removeIf(aa ->
                aa.getArtist() != null && 
                aa.getArtist().getId() != null && 
                aa.getArtist().getId().equals(artist.getId())
        );
    }

    // Método conveniente para pegar só os artistas
    public List<Artist> getArtists() {
        return albumArtists.stream()
                .map(AlbumHasArtist::getArtist)
                .collect(Collectors.toList());
    }

    // ========== ADICIONE ESSES MÉTODOS PARA GENRE ==========

    // Métodos helper para Genre (ManyToMany)
    public void addGenre(Genre genre) {
        AlbumHasGenre albumHasGenre = new AlbumHasGenre();
        albumHasGenre.setAlbum(this);
        albumHasGenre.setGenre(genre);
        albumGenres.add(albumHasGenre);
    }

    public void removeGenre(Genre genre) {
        albumGenres.removeIf(ag ->
                ag.getGenre() != null &&
                        ag.getGenre().getId() != null &&
                        ag.getGenre().getId().equals(genre.getId())
        );
    }

    // Método conveniente para pegar só os gêneros
    public List<Genre> getGenres() {
        return albumGenres.stream()
                .map(AlbumHasGenre::getGenre)
                .collect(Collectors.toList());
    }

    // ========== FIM DOS MÉTODOS DE GENRE ==========

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getNumberOfDiscs() {
        return numberOfDiscs;
    }

    public void setNumberOfDiscs(Integer numberOfDiscs) {
        this.numberOfDiscs = numberOfDiscs;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getIsCompilation() {
        return isCompilation;
    }

    public void setIsCompilation(Boolean isCompilation) {
        this.isCompilation = isCompilation;
    }

    public AlbumType getAlbumType() {
        return albumType;
    }

    public void setAlbumType(AlbumType albumType) {
        this.albumType = albumType;
    }

    public AlbumArt getAlbumArt() {
        return albumArt;
    }

    public void setAlbumArt(AlbumArt albumArt) {
        this.albumArt = albumArt;
    }

    public List<AlbumHasArtist> getAlbumArtists() {
        return albumArtists;
    }

    public void setAlbumArtists(List<AlbumHasArtist> albumArtists) {
        this.albumArtists = albumArtists;
    }

    // ========== ADICIONE ESSES GETTERS/SETTERS ==========

    public List<AlbumHasGenre> getAlbumGenres() {
        return albumGenres;
    }

    public void setAlbumGenres(List<AlbumHasGenre> albumGenres) {
        this.albumGenres = albumGenres;
    }

    // ========== FIM DOS GETTERS/SETTERS DE GENRE ==========

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Album{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", year=" + year +
                '}';
    }
}
