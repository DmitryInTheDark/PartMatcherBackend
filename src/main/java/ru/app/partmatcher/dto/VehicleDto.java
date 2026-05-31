package ru.app.partmatcher.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VehicleDto {
    private Long id;
    private String vin;
    private String brand;
    private String model;
    private Integer year;
    private String engine;
    private String bodyType;
}
