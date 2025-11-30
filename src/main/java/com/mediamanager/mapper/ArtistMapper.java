package com.mediamanager.mapper;

import com.mediamanager.model.Artist;
import com.mediamanager.protocol.messages.ArtistMessages;


public class ArtistMapper {
    public static ArtistMessages.Artist toProtobuf(Artist entity){
        if (entity == null) {
            return null;
        }

        ArtistMessages.Artist.Builder builder = ArtistMessages.Artist.newBuilder()
                .setName(entity.getName());

        // Only set ID when it's present and valid (> 0). Avoids NPE for null IDs.
        Integer id = entity.getId();
        if (id != null && id > 0) {
            builder.setId(id);
        }

        return builder.build();
    }
    public static Artist toEntity(ArtistMessages.Artist protobuf) {
        if (protobuf == null) {
            return null;
        }
        Artist entity = new Artist();

        if (protobuf.getId() > 0) {
            entity.setId(protobuf.getId());
        }
        entity.setName(protobuf.getName());
        return entity;
    }
}
