package ru.app.partmatcher.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VehicleUpdateDto {

    @NotBlank
    private String brand;

    @NotBlank
    private String model;

    @NotNull
    private Integer year;

    @NotBlank
    private String engine;

    @NotBlank
    private String bodyType;
}
