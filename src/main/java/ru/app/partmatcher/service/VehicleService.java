package ru.app.partmatcher.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.app.partmatcher.dto.PartDto;
import ru.app.partmatcher.dto.VehicleCreateDto;
import ru.app.partmatcher.dto.VehicleDto;
import ru.app.partmatcher.dto.VehicleSearchResultDto;
import ru.app.partmatcher.dto.VehicleUpdateDto;
import ru.app.partmatcher.entity.Vehicle;
import ru.app.partmatcher.exception.ResourceNotFoundException;
import ru.app.partmatcher.mapper.PartMapper;
import ru.app.partmatcher.mapper.VehicleMapper;
import ru.app.partmatcher.repository.VehicleRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final SearchHistoryService searchHistoryService;

    public VehicleSearchResultDto searchByVin(String vin) {
        Vehicle vehicle = vehicleRepository.findByVinIgnoreCase(vin)
                .orElseThrow(() -> new ResourceNotFoundException("Автомобиль с VIN не найден"));

        List<PartDto> parts = vehicle.getCompatibleParts().stream()
                .map(PartMapper::toDto)
                .collect(Collectors.toList());

        return VehicleSearchResultDto.builder()
                .vehicle(VehicleMapper.toDto(vehicle))
                .compatibleParts(parts)
                .build();
    }

    public List<PartDto> getPartsByVin(String vin) {
        Vehicle vehicle = vehicleRepository.findByVinIgnoreCase(vin)
                .orElseThrow(() -> new ResourceNotFoundException("Автомобиль с VIN не найден"));
        return vehicle.getCompatibleParts().stream()
                .map(PartMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public VehicleDto create(VehicleCreateDto dto) {
        Vehicle vehicle = Vehicle.builder()
                .vin(dto.getVin())
                .brand(dto.getBrand())
                .model(dto.getModel())
                .year(dto.getYear())
                .engine(dto.getEngine())
                .bodyType(dto.getBodyType())
                .build();
        return VehicleMapper.toDto(vehicleRepository.save(vehicle));
    }

    public VehicleDto update(Long id, VehicleUpdateDto dto) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Автомобиль не найден"));
        VehicleMapper.updateFromDto(dto, vehicle);
        return VehicleMapper.toDto(vehicleRepository.save(vehicle));
    }

    public void delete(Long id) {
        if (!vehicleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Автомобиль не найден");
        }
        vehicleRepository.deleteById(id);
    }
}
