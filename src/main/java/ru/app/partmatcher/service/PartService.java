package ru.app.partmatcher.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.app.partmatcher.dto.PartCreateDto;
import ru.app.partmatcher.dto.PartDto;
import ru.app.partmatcher.dto.PartUpdateDto;
import ru.app.partmatcher.entity.Part;
import ru.app.partmatcher.entity.Vehicle;
import ru.app.partmatcher.exception.ResourceNotFoundException;
import ru.app.partmatcher.mapper.PartMapper;
import ru.app.partmatcher.repository.PartRepository;
import ru.app.partmatcher.repository.VehicleRepository;
import ru.app.partmatcher.service.UserService;

import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartService {

    private final PartRepository partRepository;
    private final VehicleRepository vehicleRepository;
    private final SearchHistoryService searchHistoryService;
    private final UserService userService;

    @Transactional
    public PartDto create(PartCreateDto dto) {
        if (partRepository.existsByArticleIgnoreCase(dto.getArticle())) {
            throw new IllegalArgumentException("Деталь с таким артикулом уже существует");
        }
        Part part = PartMapper.toEntity(dto);
        return PartMapper.toDto(partRepository.save(part));
    }

    public PartDto update(Long id, PartUpdateDto dto) {
        Part part = partRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Деталь не найдена"));
        PartMapper.updateFromDto(dto, part);
        return PartMapper.toDto(partRepository.save(part));
    }

    public void delete(Long id) {
        if (!partRepository.existsById(id)) {
            throw new ResourceNotFoundException("Деталь не найдена");
        }
        partRepository.deleteById(id);
    }

    public List<PartDto> search(String query) {
        if (query == null || query.isBlank()) {
            return List.of();
        }
        userService.addSearchQuery(query);

        List<String> tokens = Arrays.stream(normalize(query).split("\\s+"))
                .filter(token -> !token.isBlank())
                .distinct()
                .toList();
        if (tokens.isEmpty()) {
            return List.of();
        }

        return partRepository.findAll().stream()
                .map(part -> new PartScore(part, calculateRelevance(part, tokens)))
                .filter(score -> score.getScore() > 0)
                .sorted(Comparator.comparingDouble(PartScore::getScore).reversed())
                .map(PartScore::getPart)
                .map(PartMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<PartDto> findByVehicleVin(String vin) {
        Vehicle vehicle = vehicleRepository.findByVinIgnoreCase(vin)
                .orElseThrow(() -> new ResourceNotFoundException("Автомобиль с VIN не найден"));
        return vehicle.getCompatibleParts().stream()
                .map(PartMapper::toDto)
                .collect(Collectors.toList());
    }

    private String normalize(String value) {
        if (value == null) return "";
        return Normalizer.normalize(value.toLowerCase().trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
    }

    private double calculateRelevance(Part part, List<String> tokens) {
        double score = 0.0;
        String name = normalize(part.getName());
        String article = normalize(part.getArticle());
        String manufacturer = normalize(part.getManufacturer());
        String category = normalize(part.getCategory());

        for (String token : tokens) {
            score += weightedScore(token, name, 100);
            score += weightedScore(token, article, 90);
            score += weightedScore(token, manufacturer, 70);
            score += weightedScore(token, category, 50);
        }
        return score;
    }

    private double weightedScore(String token, String field, int weight) {
        if (field == null || field.isBlank()) {
            return 0.0;
        }
        if (field.equals(token)) {
            return weight;
        }
        if (field.contains(token)) {
            return weight * 0.85;
        }
        double similarity = calculateSimilarity(token, field);
        if (similarity >= 0.35) {
            return weight * similarity * 0.9;
        }
        return 0.0;
    }

    private double calculateSimilarity(String source, String target) {
        if (source == null || source.isBlank() || target == null || target.isBlank()) {
            return 0.0;
        }
        int distance = levenshteinDistance(source, target);
        int maxLen = Math.max(source.length(), target.length());
        if (maxLen == 0) {
            return 1.0;
        }
        return Math.max(0.0, 1.0 - ((double) distance / maxLen));
    }

    private int levenshteinDistance(String s1, String s2) {
        int[] costs = new int[s2.length() + 1];
        for (int j = 0; j < costs.length; j++) {
            costs[j] = j;
        }
        for (int i = 1; i <= s1.length(); i++) {
            costs[0] = i;
            int northwest = i - 1;
            for (int j = 1; j <= s2.length(); j++) {
                int cost = Math.min(1 + Math.min(costs[j], costs[j - 1]), s1.charAt(i - 1) == s2.charAt(j - 1) ? northwest : northwest + 1);
                northwest = costs[j];
                costs[j] = cost;
            }
        }
        return costs[s2.length()];
    }

    private static class PartScore {
        private final Part part;
        private final double score;

        public PartScore(Part part, double score) {
            this.part = part;
            this.score = score;
        }

        public Part getPart() {
            return part;
        }

        public double getScore() {
            return score;
        }
    }
}
