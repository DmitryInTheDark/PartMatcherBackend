package ru.app.partmatcher.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.app.partmatcher.dto.ApiResponseDto;
import ru.app.partmatcher.dto.AnalogPartDto;
import ru.app.partmatcher.dto.PartCreateDto;
import ru.app.partmatcher.dto.PartDto;
import ru.app.partmatcher.dto.PartUpdateDto;
import ru.app.partmatcher.repository.AnalogPartRepository;
import ru.app.partmatcher.service.PartService;

import java.util.List;

@RestController
@RequestMapping("/api/parts")
@RequiredArgsConstructor
@Tag(name = "Parts", description = "Каталог запчастей и поиск аналогов")
public class PartController {

    private final PartService partService;
    private final AnalogPartRepository analogPartRepository;

    @Operation(summary = "Поиск деталей по ключевой фразе")
    @GetMapping("/search")
    public ResponseEntity<List<PartDto>> search(@RequestParam String query) {
        return ResponseEntity.ok(partService.search(query));
    }

    @Operation(summary = "Получить список аналогов для детали")
    @GetMapping("/{id}/analogs")
    public ResponseEntity<List<AnalogPartDto>> analogs(@PathVariable Long id) {
        return ResponseEntity.ok(analogPartRepository.findByOriginalPartId(id).stream()
                .map(ru.app.partmatcher.mapper.AnalogPartMapper::toDto)
                .toList());
    }

    @Operation(summary = "Получить информацию о детали по id")
    @GetMapping("/{id}")
    public ResponseEntity<PartDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(partService.getById(id));
    }

    @Operation(summary = "Получить детали совместимые с автомобилем по VIN")
    @GetMapping("/vehicle/{vin}")
    public ResponseEntity<List<PartDto>> partsForVehicle(@PathVariable String vin) {
        return ResponseEntity.ok(partService.findByVehicleVin(vin));
    }

    @Operation(summary = "Создать новую деталь (ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PartDto> create(@Valid @RequestBody PartCreateDto request) {
        return ResponseEntity.ok(partService.create(request));
    }

    @Operation(summary = "Обновить деталь по id (ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PartDto> update(@PathVariable Long id, @Valid @RequestBody PartUpdateDto request) {
        return ResponseEntity.ok(partService.update(id, request));
    }

    @Operation(summary = "Удалить деталь по id (ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto> delete(@PathVariable Long id) {
        partService.delete(id);
        return ResponseEntity.ok(ApiResponseDto.builder().message("Деталь удалена").success(true).build());
    }
}
