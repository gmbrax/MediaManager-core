package com.mediamanager.mapper;

import com.mediamanager.model.Album;
import com.mediamanager.protocol.messages.AlbumMessages;

import java.time.Instant;
import java.time.ZoneId;

public class AlbumMapper {
    public static AlbumMessages.Album toProtobuf(Album entity) {
        if (entity == null) {
            return null;
        }

        String name = entity.getName();
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }

        AlbumMessages.Album.Builder builder = AlbumMessages.Album.newBuilder()
                .setName(name);

        Integer id = entity.getId();
        if (id != null) {
            builder.setId(id);
        }

        Integer year = entity.getYear();
        if (year != null) {
            builder.setYear(year);
        }

        Integer numberOfDiscs = entity.getNumberOfDiscs();
        if (numberOfDiscs != null) {
            builder.setNumberOfDiscs(numberOfDiscs);
        }

        String code = entity.getCode();
        if (code != null) {
            builder.setCode(code);
        }

        Boolean isCompilation = entity.getIsCompilation();
        if (isCompilation != null) {
            builder.setIsCompilation(isCompilation);
        }

        // Map AlbumType foreign key
        if (entity.getAlbumType() != null && entity.getAlbumType().getId() != null) {
            builder.setFkAlbumtypeId(entity.getAlbumType().getId());
        }

        // Map AlbumArt foreign key
        if (entity.getAlbumArt() != null && entity.getAlbumArt().getId() != null) {
            builder.setFkAlbumartId(entity.getAlbumArt().getId());
        }

        // Map timestamps
        if (entity.getCreatedAt() != null) {
            long createdAtEpoch = entity.getCreatedAt()
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli();
            builder.setCreatedAt(createdAtEpoch);
        }

        if (entity.getUpdatedAt() != null) {
            long updatedAtEpoch = entity.getUpdatedAt()
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli();
            builder.setUpdatedAt(updatedAtEpoch);
        }

        return builder.build();
    }

    public static Album toEntity(AlbumMessages.Album protobuf) {
        if (protobuf == null) {
            return null;
        }

        Album entity = new Album();

        if (protobuf.getId() > 0) {
            entity.setId(protobuf.getId());
        }

        entity.setName(protobuf.getName());

        if (protobuf.getYear() > 0) {
            entity.setYear(protobuf.getYear());
        }

        if (protobuf.getNumberOfDiscs() > 0) {
            entity.setNumberOfDiscs(protobuf.getNumberOfDiscs());
        }

        if (!protobuf.getCode().isEmpty()) {
            entity.setCode(protobuf.getCode());
        }

        entity.setIsCompilation(protobuf.getIsCompilation());

        // Note: Foreign key relationships (AlbumType, AlbumArt) are handled in the service layer
        // Timestamps are managed by JPA @PrePersist and @PreUpdate

        return entity;
    }
}
