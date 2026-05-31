package ru.app.partmatcher.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.app.partmatcher.dto.ApiResponseDto;
import ru.app.partmatcher.dto.VehicleCreateDto;
import ru.app.partmatcher.dto.VehicleDto;
import ru.app.partmatcher.dto.VehicleSearchResultDto;
import ru.app.partmatcher.dto.VehicleUpdateDto;
import ru.app.partmatcher.service.UserService;
import ru.app.partmatcher.service.VehicleCompatibilityService;
import ru.app.partmatcher.service.VehicleService;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
@Tag(name = "Vehicles", description = "Поиск и управление автомобилями")
public class VehicleController {

    private final VehicleService vehicleService;
    private final UserService userService;
    private final VehicleCompatibilityService compatibilityService;

    @Operation(summary = "Поиск автомобиля по VIN и получение совместимых деталей")
    @GetMapping("/vin/{vin}")
    public ResponseEntity<VehicleSearchResultDto> searchVehicle(@PathVariable String vin) {
        VehicleSearchResultDto result = vehicleService.searchByVin(vin);
        userService.addVinSearch(vin);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Получить список совместимых деталей для автомобиля по VIN")
    @GetMapping("/vin/{vin}/parts")
    public ResponseEntity<java.util.List<ru.app.partmatcher.dto.PartDto>> partsByVehicle(@PathVariable String vin) {
        return ResponseEntity.ok(vehicleService.getPartsByVin(vin));
    }

    @Operation(summary = "Создать автомобиль (ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<VehicleDto> create(@Valid @RequestBody VehicleCreateDto request) {
        return ResponseEntity.ok(vehicleService.create(request));
    }

    @Operation(summary = "Обновить автомобиль (ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<VehicleDto> update(@PathVariable Long id, @Valid @RequestBody VehicleUpdateDto request) {
        return ResponseEntity.ok(vehicleService.update(id, request));
    }

    @Operation(summary = "Удалить автомобиль (ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto> delete(@PathVariable Long id) {
        vehicleService.delete(id);
        return ResponseEntity.ok(ApiResponseDto.builder().message("Автомобиль удален").success(true).build());
    }

    @Operation(summary = "Связать деталь с автомобилем (ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{vehicleId}/parts/{partId}")
    public ResponseEntity<ApiResponseDto> addCompatibility(@PathVariable Long vehicleId, @PathVariable Long partId) {
        compatibilityService.linkPartToVehicle(vehicleId, partId);
        return ResponseEntity.ok(ApiResponseDto.builder().message("Деталь связана с автомобилем").success(true).build());
    }

    @Operation(summary = "Удалить связь детали с автомобилем (ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{vehicleId}/parts/{partId}")
    public ResponseEntity<ApiResponseDto> removeCompatibility(@PathVariable Long vehicleId, @PathVariable Long partId) {
        compatibilityService.unlinkPartFromVehicle(vehicleId, partId);
        return ResponseEntity.ok(ApiResponseDto.builder().message("Связь между автомобилем и деталью удалена").success(true).build());
    }
}
