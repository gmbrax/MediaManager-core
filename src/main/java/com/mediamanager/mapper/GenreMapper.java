package com.mediamanager.mapper;

import com.mediamanager.model.Genre;
import com.mediamanager.protocol.messages.GenreMessages;

public class GenreMapper {
    public static GenreMessages.Genre toProtobuf(Genre entity) {
        if (entity == null) {
            return null;
        }

        GenreMessages.Genre.Builder builder = GenreMessages.Genre.newBuilder()
                .setName(entity.getName());

        // Only set ID when it's present and valid (> 0). Avoids NPE for null IDs.
        Integer id = entity.getId();
        if (id != null && id > 0) {
            builder.setId(id);
        }

        return builder.build();
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

