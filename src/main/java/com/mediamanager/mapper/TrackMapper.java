package com.mediamanager.mapper;

import com.mediamanager.model.Track;
import com.mediamanager.protocol.messages.TrackMessages;

public class TrackMapper {
    public static TrackMessages.Track toProtobuf(Track entity) {
        if (entity == null) {
            return null;
        }

        Integer trackNumber = entity.getTrackNumber();
        if (trackNumber == null) {
            throw new IllegalArgumentException("Track number cannot be null");
        }

        String title = entity.getTitle();
        if (title == null) {
            throw new IllegalArgumentException("Title cannot be null");
        }

        String filepath = entity.getFilepath();
        if (filepath == null) {
            throw new IllegalArgumentException("Filepath cannot be null");
        }

        TrackMessages.Track.Builder builder = TrackMessages.Track.newBuilder()
                .setTrackNumber(trackNumber)
                .setTitle(title)
                .setFilepath(filepath);

        Integer id = entity.getId();
        if (id != null) {
            builder.setId(id);
        }

        // Map duration (optional)
        Integer duration = entity.getDuration();
        if (duration != null) {
            builder.setDuration(duration);
        }

        // Map Disc foreign key (required)
        if (entity.getDisc() != null && entity.getDisc().getId() != null) {
            builder.setFkDiscId(entity.getDisc().getId());
        }

        // Map Composer foreign key (optional)
        if (entity.getComposer() != null && entity.getComposer().getId() != null) {
            builder.setFkComposerId(entity.getComposer().getId());
        }

        // Map BitDepth foreign key (optional)
        if (entity.getBitDepth() != null && entity.getBitDepth().getId() != null) {
            builder.setFkBitdepthId(entity.getBitDepth().getId());
        }

        // Map BitRate foreign key (optional)
        if (entity.getBitRate() != null && entity.getBitRate().getId() != null) {
            builder.setFkBitrateId(entity.getBitRate().getId());
        }

        // Map SamplingRate foreign key (optional)
        if (entity.getSamplingRate() != null && entity.getSamplingRate().getId() != null) {
            builder.setFkSamplingrateId(entity.getSamplingRate().getId());
        }

        return builder.build();
    }

    public static Track toEntity(TrackMessages.Track protobuf) {
        if (protobuf == null) {
            return null;
        }

        Track entity = new Track();

        if (protobuf.getId() > 0) {
            entity.setId(protobuf.getId());
        }

        entity.setTrackNumber(protobuf.getTrackNumber());
        entity.setTitle(protobuf.getTitle());
        entity.setFilepath(protobuf.getFilepath());

        if (protobuf.getDuration() > 0) {
            entity.setDuration(protobuf.getDuration());
        }

        // Note: Foreign key relationships (Disc, Composer, BitDepth, BitRate, SamplingRate)
        // are handled in the service layer

        return entity;
    }
}
