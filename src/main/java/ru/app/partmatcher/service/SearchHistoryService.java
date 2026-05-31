package ru.app.partmatcher.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.app.partmatcher.dto.SearchHistoryDto;
import ru.app.partmatcher.dto.VinHistoryDto;
import ru.app.partmatcher.entity.SearchHistory;
import ru.app.partmatcher.entity.User;
import ru.app.partmatcher.entity.VinHistory;
import ru.app.partmatcher.mapper.SearchHistoryMapper;
import ru.app.partmatcher.repository.SearchHistoryRepository;
import ru.app.partmatcher.repository.VinHistoryRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchHistoryService {

    private final SearchHistoryRepository searchHistoryRepository;
    private final VinHistoryRepository vinHistoryRepository;

    public void addSearchQuery(User user, String query) {
        if (query == null || query.isBlank()) {
            return;
        }
        searchHistoryRepository.save(SearchHistory.builder()
                .user(user)
                .query(query.trim())
                .searchedAt(LocalDateTime.now())
                .build());
    }

    public void addVinSearch(User user, String vin) {
        if (vin == null || vin.isBlank()) {
            return;
        }
        vinHistoryRepository.save(VinHistory.builder()
                .user(user)
                .vin(vin.trim())
                .searchedAt(LocalDateTime.now())
                .build());
    }

    public List<SearchHistoryDto> getSearchHistory(User user) {
        return searchHistoryRepository.findByUserOrderBySearchedAtDesc(user).stream()
                .map(SearchHistoryMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<VinHistoryDto> getVinHistory(User user) {
        return vinHistoryRepository.findByUserOrderBySearchedAtDesc(user).stream()
                .map(SearchHistoryMapper::toDto)
                .collect(Collectors.toList());
    }
}
