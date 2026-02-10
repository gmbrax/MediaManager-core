package com.mediamanager.mapper;

import com.mediamanager.model.AlbumType;
import com.mediamanager.protocol.messages.AlbumTypeMessages;

public class AlbumTypeMapper {
    public static AlbumTypeMessages.AlbumType toProtobuf(AlbumType entity) {
        if (entity == null) {
            return null;
        }
        String value = entity.getValue();
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Value cannot be null or empty");
        }
        AlbumTypeMessages.AlbumType.Builder builder = AlbumTypeMessages.AlbumType.newBuilder()
                .setValue(value);
        Integer id = entity.getId();
        if (id != null) {
            builder.setId(id);
        }
        return builder.build();
    }

    public static AlbumType toEntity(AlbumTypeMessages.AlbumType protobuf) {
        if (protobuf == null) {
            return null;
        }
        AlbumType entity = new AlbumType();
        if (protobuf.getId() >0) {
            entity.setId(protobuf.getId());
        }
        entity.setValue(protobuf.getValue());
        return entity;
    }
}
