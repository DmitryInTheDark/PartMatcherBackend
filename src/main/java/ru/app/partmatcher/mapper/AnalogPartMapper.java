package ru.app.partmatcher.mapper;

import ru.app.partmatcher.dto.AnalogPartDto;
import ru.app.partmatcher.dto.PartDto;
import ru.app.partmatcher.entity.AnalogPart;

public class AnalogPartMapper {

    public static AnalogPartDto toDto(AnalogPart analogPart) {
        if (analogPart == null) {
            return null;
        }
        return AnalogPartDto.builder()
                .id(analogPart.getId())
                .originalPart(PartMapper.toDto(analogPart.getOriginalPart()))
                .analogPart(PartMapper.toDto(analogPart.getAnalogPart()))
                .build();
    }
}
