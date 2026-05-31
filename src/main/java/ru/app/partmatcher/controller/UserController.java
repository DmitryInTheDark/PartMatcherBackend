package ru.app.partmatcher.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.app.partmatcher.dto.ApiResponseDto;
import ru.app.partmatcher.dto.PartDto;
import ru.app.partmatcher.dto.UserDto;
import ru.app.partmatcher.repository.PartRepository;
import ru.app.partmatcher.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "User", description = "Профиль пользователя и избранное")
public class UserController {

    private final UserService userService;
    private final PartRepository partRepository;

    @Operation(summary = "Получить текущего пользователя")
    @GetMapping("/me")
    public ResponseEntity<UserDto> currentUser() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @Operation(summary = "Список избранных деталей текущего пользователя")
    @GetMapping("/favorites")
    public ResponseEntity<List<PartDto>> favoriteParts() {
        return ResponseEntity.ok(userService.getFavoriteParts());
    }

    @Operation(summary = "Добавить деталь в избранное текущего пользователя")
    @PostMapping("/favorites/{partId}")
    public ResponseEntity<ApiResponseDto> addFavorite(@PathVariable Long partId) {
        userService.addFavoritePart(partId);
        return ResponseEntity.ok(ApiResponseDto.builder().message("Деталь добавлена в избранное").success(true).build());
    }

    @Operation(summary = "Удалить деталь из избранного текущего пользователя")
    @DeleteMapping("/favorites/{partId}")
    public ResponseEntity<ApiResponseDto> removeFavorite(@PathVariable Long partId) {
        userService.removeFavoritePart(partId);
        return ResponseEntity.ok(ApiResponseDto.builder().message("Деталь удалена из избранного").success(true).build());
    }

    @Operation(summary = "История поисковых запросов текущего пользователя")
    @GetMapping("/history/search")
    public ResponseEntity<List<ru.app.partmatcher.dto.SearchHistoryDto>> searchHistory() {
        return ResponseEntity.ok(userService.getSearchHistory());
    }

    @Operation(summary = "История VIN-поисков текущего пользователя")
    @GetMapping("/history/vin")
    public ResponseEntity<List<ru.app.partmatcher.dto.VinHistoryDto>> vinHistory() {
        return ResponseEntity.ok(userService.getVinHistory());
    }
}
