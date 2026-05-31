package ru.app.partmatcher.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.app.partmatcher.dto.PartDto;
import ru.app.partmatcher.dto.SearchHistoryDto;
import ru.app.partmatcher.dto.UserDto;
import ru.app.partmatcher.dto.VinHistoryDto;
import ru.app.partmatcher.entity.Part;
import ru.app.partmatcher.entity.User;
import ru.app.partmatcher.exception.ResourceNotFoundException;
import ru.app.partmatcher.mapper.PartMapper;
import ru.app.partmatcher.mapper.UserMapper;
import ru.app.partmatcher.repository.PartRepository;
import ru.app.partmatcher.repository.UserRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final SearchHistoryService searchHistoryService;
    private final PartRepository partRepository;

    public UserDto getCurrentUser() {
        return UserMapper.toDto(getCurrentUserEntity());
    }

    public User getCurrentUserEntity() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new ResourceNotFoundException("Текущий пользователь не найден");
        }
        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));
    }

    public List<PartDto> getFavoriteParts() {
        return getCurrentUserEntity().getFavoriteParts().stream()
                .map(PartMapper::toDto)
                .collect(Collectors.toList());
    }

    public void addFavoritePart(Long partId) {
        User user = getCurrentUserEntity();
        Part part = partRepository.findById(partId)
                .orElseThrow(() -> new ResourceNotFoundException("Деталь не найдена"));
        user.getFavoriteParts().add(part);
        userRepository.save(user);
    }

    public void removeFavoritePart(Long partId) {
        User user = getCurrentUserEntity();
        boolean removed = user.getFavoriteParts().removeIf(part -> part.getId().equals(partId));
        if (!removed) {
            throw new ResourceNotFoundException("Избранная деталь не найдена");
        }
        userRepository.save(user);
    }

    public List<SearchHistoryDto> getSearchHistory() {
        return searchHistoryService.getSearchHistory(getCurrentUserEntity());
    }

    public List<VinHistoryDto> getVinHistory() {
        return searchHistoryService.getVinHistory(getCurrentUserEntity());
    }

    public void addVinSearch(String vin) {
        searchHistoryService.addVinSearch(getCurrentUserEntity(), vin);
    }

    public void addSearchQuery(String query) {
        searchHistoryService.addSearchQuery(getCurrentUserEntity(), query);
    }
}
