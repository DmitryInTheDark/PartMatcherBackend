package ru.app.partmatcher.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.app.partmatcher.entity.Part;
import ru.app.partmatcher.entity.Vehicle;
import ru.app.partmatcher.exception.ResourceNotFoundException;
import ru.app.partmatcher.repository.PartRepository;
import ru.app.partmatcher.repository.VehicleRepository;

@Service
@RequiredArgsConstructor
public class VehicleCompatibilityService {

    private final VehicleRepository vehicleRepository;
    private final PartRepository partRepository;

    @Transactional
    public void linkPartToVehicle(Long vehicleId, Long partId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Автомобиль не найден"));
        Part part = partRepository.findById(partId)
                .orElseThrow(() -> new ResourceNotFoundException("Деталь не найдена"));

        vehicle.getCompatibleParts().add(part);
        part.getCompatibleVehicles().add(vehicle);
        vehicleRepository.save(vehicle);
    }

    @Transactional
    public void unlinkPartFromVehicle(Long vehicleId, Long partId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Автомобиль не найден"));
        Part part = partRepository.findById(partId)
                .orElseThrow(() -> new ResourceNotFoundException("Деталь не найдена"));

        vehicle.getCompatibleParts().remove(part);
        part.getCompatibleVehicles().remove(vehicle);
        vehicleRepository.save(vehicle);
    }
}
