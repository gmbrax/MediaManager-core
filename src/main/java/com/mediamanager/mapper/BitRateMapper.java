package com.mediamanager.mapper;

import com.mediamanager.model.BitRate;
import com.mediamanager.protocol.messages.BitRateMessages;

public class BitRateMapper {
    public static BitRateMessages.BitRate toProtobuf(BitRate entity) {
        if (entity == null) {
            return null;
        }

        String value = entity.getValue();
        if (value == null) {
            throw new IllegalArgumentException("Bit rate value cannot be null");
        }

        BitRateMessages.BitRate.Builder builder = BitRateMessages.BitRate.newBuilder()
                .setValue(value);

        Integer id = entity.getId();
        if (id != null && id > 0) {
            builder.setId(id);
        }
        return builder.build();
    }

    public static BitRate toEntity(BitRateMessages.BitRate protobuf) {
        if (protobuf == null) {
            return null;
        }
        BitRate entity = new BitRate();

        if (protobuf.getId() > 0) {
            entity.setId(protobuf.getId());
        }
        entity.setValue(protobuf.getValue());
        return entity;
    }
}
