package ru.app.partmatcher.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.app.partmatcher.entity.Vehicle;

import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Optional<Vehicle> findByVinIgnoreCase(String vin);
    boolean existsByVinIgnoreCase(String vin);
}
