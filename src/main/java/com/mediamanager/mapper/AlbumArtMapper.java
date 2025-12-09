package com.mediamanager.mapper;

import com.mediamanager.model.AlbumArt;
import com.mediamanager.protocol.messages.AlbumArtMessages;

public class AlbumArtMapper {
    public static AlbumArtMessages.AlbumArt toProtobuf(AlbumArt entity) {
        if (entity == null) {
            return null;
        }
        String filepath = entity.getFilepath();
        if (filepath == null || filepath.isEmpty()) {
            throw new IllegalArgumentException("Filepath cannot be null or empty");
        }
        AlbumArtMessages.AlbumArt.Builder builder = AlbumArtMessages.AlbumArt.newBuilder()
                .setFilepath(filepath);
        Integer id = entity.getId();
        if (id != null) {
            builder.setId(id);
        }
        return builder.build();
    }

    public static AlbumArt toEntity(AlbumArtMessages.AlbumArt protobuf) {
        if (protobuf == null) {
            return null;
        }
        AlbumArt entity = new AlbumArt();
        if (protobuf.getId() >0) {
            entity.setId(protobuf.getId());
        }
        entity.setFilepath(protobuf.getFilepath());
        return entity;
    }
}
