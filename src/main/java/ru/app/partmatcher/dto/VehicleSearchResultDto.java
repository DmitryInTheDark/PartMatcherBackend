package ru.app.partmatcher.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class VehicleSearchResultDto {
    private VehicleDto vehicle;
    private List<PartDto> compatibleParts;
}
