package ru.app.partmatcher.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.app.partmatcher.dto.AdminStatisticsDto;
import ru.app.partmatcher.service.AdminStatisticsService;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Admin", description = "Административные API")
public class AdminStatisticsController {

    private final AdminStatisticsService adminStatisticsService;

    @Operation(summary = "Статистика системы")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/statistics")
    public ResponseEntity<AdminStatisticsDto> getStatistics() {
        return ResponseEntity.ok(adminStatisticsService.getStatistics());
    }
}
