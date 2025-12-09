package com.mediamanager.mapper;

import com.mediamanager.model.TrackHasArtist;
import com.mediamanager.protocol.messages.TrackHasArtistMessages;

public class TrackHasArtistMapper {
    public static TrackHasArtistMessages.TrackHasArtist toProtobuf(TrackHasArtist entity) {
        if (entity == null) {
            return null;
        }

        TrackHasArtistMessages.TrackHasArtist.Builder builder = TrackHasArtistMessages.TrackHasArtist.newBuilder();

        Integer id = entity.getId();
        if (id != null) {
            builder.setId(id);
        }

        // Map Track foreign key
        if (entity.getTrack() != null && entity.getTrack().getId() != null) {
            builder.setFkTrackId(entity.getTrack().getId());
        }

        // Map Artist foreign key
        if (entity.getArtist() != null && entity.getArtist().getId() != null) {
            builder.setFkArtistId(entity.getArtist().getId());
        }

        return builder.build();
    }

    public static TrackHasArtist toEntity(TrackHasArtistMessages.TrackHasArtist protobuf) {
        if (protobuf == null) {
            return null;
        }

        TrackHasArtist entity = new TrackHasArtist();

        if (protobuf.getId() > 0) {
            entity.setId(protobuf.getId());
        }

        // Note: Foreign key relationships (Track, Artist) are handled in the service layer

        return entity;
    }
}
