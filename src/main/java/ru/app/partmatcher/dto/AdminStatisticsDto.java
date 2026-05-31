package ru.app.partmatcher.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminStatisticsDto {
    private long totalUsers;
    private long totalVehicles;
    private long totalParts;
    private long totalVinSearches;
    private long totalPartSearches;
    private long totalChatMessages;
}
