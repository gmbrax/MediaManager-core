package com.mediamanager.mapper;

import com.mediamanager.model.TrackHasComposer;
import com.mediamanager.protocol.messages.TrackHasComposerMessages;

public class TrackHasComposerMapper {
    public static TrackHasComposerMessages.TrackHasComposer toProtobuf(TrackHasComposer entity) {
        if (entity == null) {
            return null;
        }

        TrackHasComposerMessages.TrackHasComposer.Builder builder = TrackHasComposerMessages.TrackHasComposer.newBuilder();

        Integer id = entity.getId();
        if (id != null) {
            builder.setId(id);
        }

        // Map Track foreign key
        if (entity.getTrack() != null && entity.getTrack().getId() != null) {
            builder.setFkTrackId(entity.getTrack().getId());
        }

        // Map Composer foreign key
        if (entity.getComposer() != null && entity.getComposer().getId() != null) {
            builder.setFkComposerId(entity.getComposer().getId());
        }

        return builder.build();
    }

    public static TrackHasComposer toEntity(TrackHasComposerMessages.TrackHasComposer protobuf) {
        if (protobuf == null) {
            return null;
        }

        TrackHasComposer entity = new TrackHasComposer();

        if (protobuf.getId() > 0) {
            entity.setId(protobuf.getId());
        }

        // Note: Foreign key relationships (Track, Composer) are handled in the service layer

        return entity;
    }
}
