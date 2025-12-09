package com.mediamanager.mapper;

import com.mediamanager.model.Disc;
import com.mediamanager.protocol.messages.DiscMessages;

public class DiscMapper {
    public static DiscMessages.Disc toProtobuf(Disc entity) {
        if (entity == null) {
            return null;
        }

        Integer discNumber = entity.getDiscNumber();
        if (discNumber == null) {
            throw new IllegalArgumentException("Disc number cannot be null");
        }

        DiscMessages.Disc.Builder builder = DiscMessages.Disc.newBuilder()
                .setDiscNumber(discNumber);

        Integer id = entity.getId();
        if (id != null) {
            builder.setId(id);
        }

        // Map Album foreign key
        if (entity.getAlbum() != null && entity.getAlbum().getId() != null) {
            builder.setFkAlbumId(entity.getAlbum().getId());
        }

        return builder.build();
    }

    public static Disc toEntity(DiscMessages.Disc protobuf) {
        if (protobuf == null) {
            return null;
        }

        Disc entity = new Disc();

        if (protobuf.getId() > 0) {
            entity.setId(protobuf.getId());
        }

        entity.setDiscNumber(protobuf.getDiscNumber());

        // Note: Foreign key relationship (Album) is handled in the service layer

        return entity;
    }
}
