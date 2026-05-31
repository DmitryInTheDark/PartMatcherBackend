package ru.app.partmatcher.mapper;

import ru.app.partmatcher.dto.VehicleDto;
import ru.app.partmatcher.dto.VehicleUpdateDto;
import ru.app.partmatcher.entity.Vehicle;

public class VehicleMapper {

    public static VehicleDto toDto(Vehicle vehicle) {
        if (vehicle == null) {
            return null;
        }
        return VehicleDto.builder()
                .id(vehicle.getId())
                .vin(vehicle.getVin())
                .brand(vehicle.getBrand())
                .model(vehicle.getModel())
                .year(vehicle.getYear())
                .engine(vehicle.getEngine())
                .bodyType(vehicle.getBodyType())
                .build();
    }

    public static void updateFromDto(VehicleUpdateDto dto, Vehicle vehicle) {
        vehicle.setBrand(dto.getBrand());
        vehicle.setModel(dto.getModel());
        vehicle.setYear(dto.getYear());
        vehicle.setEngine(dto.getEngine());
        vehicle.setBodyType(dto.getBodyType());
    }
}
