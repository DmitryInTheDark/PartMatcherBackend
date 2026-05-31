package ru.app.partmatcher.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.app.partmatcher.dto.AuthRequestDto;
import ru.app.partmatcher.dto.AuthResponseDto;
import ru.app.partmatcher.dto.UserRegistrationDto;
import ru.app.partmatcher.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Аутентификация и регистрация")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Регистрация нового пользователя")
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody UserRegistrationDto request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @Operation(summary = "Вход пользователя по email и паролю")
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody AuthRequestDto request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
}
