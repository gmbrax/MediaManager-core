package com.mediamanager.mapper;

import com.mediamanager.model.AlbumHasArtist;
import com.mediamanager.protocol.messages.AlbumHasArtistMessages;

public class AlbumHasArtistMapper {
    public static AlbumHasArtistMessages.AlbumHasArtist toProtobuf(AlbumHasArtist entity) {
        if (entity == null) {
            return null;
        }

        AlbumHasArtistMessages.AlbumHasArtist.Builder builder = AlbumHasArtistMessages.AlbumHasArtist.newBuilder();

        Integer id = entity.getId();
        if (id != null) {
            builder.setId(id);
        }

        // Map Album foreign key
        if (entity.getAlbum() != null && entity.getAlbum().getId() != null) {
            builder.setFkAlbumId(entity.getAlbum().getId());
        }

        // Map Artist foreign key
        if (entity.getArtist() != null && entity.getArtist().getId() != null) {
            builder.setFkArtistId(entity.getArtist().getId());
        }

        return builder.build();
    }

    public static AlbumHasArtist toEntity(AlbumHasArtistMessages.AlbumHasArtist protobuf) {
        if (protobuf == null) {
            return null;
        }

        AlbumHasArtist entity = new AlbumHasArtist();

        if (protobuf.getId() > 0) {
            entity.setId(protobuf.getId());
        }

        // Note: Foreign key relationships (Album, Artist) are handled in the service layer

        return entity;
    }
}
