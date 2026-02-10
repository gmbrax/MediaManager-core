package com.mediamanager.mapper;

import com.mediamanager.model.AlbumHasGenre;
import com.mediamanager.protocol.messages.AlbumHasGenreMessages;

public class AlbumHasGenreMapper {
    public static AlbumHasGenreMessages.AlbumHasGenre toProtobuf(AlbumHasGenre entity) {
        if (entity == null) {
            return null;
        }

        AlbumHasGenreMessages.AlbumHasGenre.Builder builder = AlbumHasGenreMessages.AlbumHasGenre.newBuilder();

        Integer id = entity.getId();
        if (id != null) {
            builder.setId(id);
        }

        // Map Album foreign key
        if (entity.getAlbum() != null && entity.getAlbum().getId() != null) {
            builder.setFkAlbumId(entity.getAlbum().getId());
        }

        // Map Genre foreign key
        if (entity.getGenre() != null && entity.getGenre().getId() != null) {
            builder.setFkGenreId(entity.getGenre().getId());
        }

        return builder.build();
    }

    public static AlbumHasGenre toEntity(AlbumHasGenreMessages.AlbumHasGenre protobuf) {
        if (protobuf == null) {
            return null;
        }

        AlbumHasGenre entity = new AlbumHasGenre();

        if (protobuf.getId() > 0) {
            entity.setId(protobuf.getId());
        }

        // Note: Foreign key relationships (Album, Genre) are handled in the service layer

        return entity;
    }
}
