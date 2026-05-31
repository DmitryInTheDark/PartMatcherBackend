package ru.app.partmatcher.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponseDto {
    private String message;
    private boolean success;
}
