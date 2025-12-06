package com.mediamanager.mapper;

import com.mediamanager.model.SamplingRate;
import com.mediamanager.protocol.messages.SamplingRateMessages;

public class SamplingRateMapper {
    public static SamplingRateMessages.SamplingRate toProtobuf(SamplingRate entity) {
        if (entity == null) {
            return null;
        }
        String value = entity.getValue();
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Value cannot be null or empty");
        }
        SamplingRateMessages.SamplingRate.Builder builder = SamplingRateMessages.SamplingRate.newBuilder()
                .setValue(value);
        Integer id = entity.getId();
        if (id != null) {
            builder.setId(id);
        }
        return builder.build();
    }

    public static SamplingRate toEntity(SamplingRateMessages.SamplingRate protobuf) {
        if (protobuf == null) {
            return null;
        }
        SamplingRate entity = new SamplingRate();
        if (protobuf.getId() >0) {
            entity.setId(protobuf.getId());
        }
        entity.setValue(protobuf.getValue());
        return entity;
    }
}
