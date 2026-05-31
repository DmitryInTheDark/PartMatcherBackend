package ru.app.partmatcher.mapper;

import ru.app.partmatcher.dto.SearchHistoryDto;
import ru.app.partmatcher.dto.VinHistoryDto;
import ru.app.partmatcher.entity.SearchHistory;
import ru.app.partmatcher.entity.VinHistory;

public class SearchHistoryMapper {

    public static SearchHistoryDto toDto(SearchHistory history) {
        return SearchHistoryDto.builder()
                .id(history.getId())
                .query(history.getQuery())
                .searchedAt(history.getSearchedAt())
                .build();
    }

    public static VinHistoryDto toDto(VinHistory history) {
        return VinHistoryDto.builder()
                .id(history.getId())
                .vin(history.getVin())
                .searchedAt(history.getSearchedAt())
                .build();
    }
}
