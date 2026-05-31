package ru.app.partmatcher.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnalogPartDto {
    private Long id;
    private PartDto originalPart;
    private PartDto analogPart;
}
