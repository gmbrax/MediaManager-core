package com.mediamanager.mapper;

import com.mediamanager.model.TrackHasGenre;
import com.mediamanager.protocol.messages.TrackHasGenreMessages;

public class TrackHasGenreMapper {
    public static TrackHasGenreMessages.TrackHasGenre toProtobuf(TrackHasGenre entity) {
        if (entity == null) {
            return null;
        }

        TrackHasGenreMessages.TrackHasGenre.Builder builder = TrackHasGenreMessages.TrackHasGenre.newBuilder();

        Integer id = entity.getId();
        if (id != null) {
            builder.setId(id);
        }

        // Map Track foreign key
        if (entity.getTrack() != null && entity.getTrack().getId() != null) {
            builder.setFkTrackId(entity.getTrack().getId());
        }

        // Map Genre foreign key
        if (entity.getGenre() != null && entity.getGenre().getId() != null) {
            builder.setFkGenreId(entity.getGenre().getId());
        }

        return builder.build();
    }

    public static TrackHasGenre toEntity(TrackHasGenreMessages.TrackHasGenre protobuf) {
        if (protobuf == null) {
            return null;
        }

        TrackHasGenre entity = new TrackHasGenre();

        if (protobuf.getId() > 0) {
            entity.setId(protobuf.getId());
        }

        // Note: Foreign key relationships (Track, Genre) are handled in the service layer

        return entity;
    }
}
