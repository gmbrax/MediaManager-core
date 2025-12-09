package com.mediamanager.mapper;

import com.mediamanager.model.BitDepth;
import com.mediamanager.protocol.messages.BitDepthMessages;

public class BitDepthMapper {
    public static BitDepthMessages.BitDepth toProtobuf(BitDepth entity) {
        if (entity == null){
            return null;
        }

        String value = entity.getValue();
        if (value == null) {
            throw new IllegalArgumentException("Bit depth value cannot be null");
        }
        BitDepthMessages.BitDepth.Builder builder = BitDepthMessages.BitDepth.newBuilder()
                .setValue(value);

        Integer id = entity.getId();
        if (id != null && id > 0) {
            builder.setId(id);
        }

        return builder.build();
    }

    public static BitDepth toEntity(BitDepthMessages.BitDepth protobuf) {
        if (protobuf == null) {
            return null;
        }
        BitDepth entity = new BitDepth();

        if (protobuf.getId() > 0) {
            entity.setId(protobuf.getId());
        }
        entity.setValue(protobuf.getValue());
        return entity;
    }
}
