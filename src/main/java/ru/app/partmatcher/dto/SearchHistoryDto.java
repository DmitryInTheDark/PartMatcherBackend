package ru.app.partmatcher.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SearchHistoryDto {
    private Long id;
    private String query;
    private LocalDateTime searchedAt;
}
