package com.mediamanager.mapper;

import com.mediamanager.model.Genre;
import com.mediamanager.protocol.messages.GenreMessages;

public class GenreMapper {
    public static GenreMessages.Genre toProtobuf(Genre entity) {
        if (entity == null) {
            return null;
        }

        return GenreMessages.Genre.newBuilder()
                .setId(entity.getId())
                .setName(entity.getName())
                .build();
    }
    public static Genre toEntity(GenreMessages.Genre protobuf) {
        if (protobuf == null) {
            return null;
        }

        Genre entity = new Genre();

        // Só seta ID se for > 0 (protobuf default é 0)
        if (protobuf.getId() > 0) {
            entity.setId(protobuf.getId());
        }

        entity.setName(protobuf.getName());

        return entity;
    }
}

