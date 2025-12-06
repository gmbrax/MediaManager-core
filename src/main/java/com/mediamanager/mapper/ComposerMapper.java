package com.mediamanager.mapper;

import com.mediamanager.model.Composer;
import com.mediamanager.protocol.messages.ComposerMessages;

public class ComposerMapper {
    public static ComposerMessages.Composer toProtobuf(Composer entity) {
        if (entity == null) {
            return null;
        }
        ComposerMessages.Composer.Builder builder = ComposerMessages.Composer.newBuilder()
                .setName(entity.getName());

        Integer id = entity.getId();
        if (id != null && id > 0) {
            builder.setId(id);
        }

        return builder.build();

    }

    public static Composer toEntity(ComposerMessages.Composer protobuf) {
        if (protobuf == null) {
            return null;
        }

        Composer entity = new Composer();

        if (protobuf.getId() > 0) {
            entity.setId(protobuf.getId());
        }

        entity.setName(protobuf.getName());

        return entity;
    }
}
