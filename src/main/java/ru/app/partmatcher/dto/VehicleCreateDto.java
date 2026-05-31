package ru.app.partmatcher.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VehicleCreateDto {

    @NotBlank
    @Size(min = 11, max = 17)
    private String vin;

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
