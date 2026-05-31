package ru.app.partmatcher.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.app.partmatcher.dto.AdminStatisticsDto;
import ru.app.partmatcher.repository.*;

@Service
@RequiredArgsConstructor
public class AdminStatisticsService {

    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final PartRepository partRepository;
    private final VinHistoryRepository vinHistoryRepository;
    private final SearchHistoryRepository searchHistoryRepository;
    private final SupportChatMessageRepository supportChatMessageRepository;

    public AdminStatisticsDto getStatistics() {
        return AdminStatisticsDto.builder()
                .totalUsers(userRepository.count())
                .totalVehicles(vehicleRepository.count())
                .totalParts(partRepository.count())
                .totalVinSearches(vinHistoryRepository.count())
                .totalPartSearches(searchHistoryRepository.count())
                .totalChatMessages(supportChatMessageRepository.count())
                .build();
    }
}
