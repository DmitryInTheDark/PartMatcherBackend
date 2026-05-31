package ru.app.partmatcher.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class VinHistoryDto {
    private Long id;
    private String vin;
    private LocalDateTime searchedAt;
}
