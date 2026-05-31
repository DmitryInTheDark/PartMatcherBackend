package ru.app.partmatcher.mapper;

import ru.app.partmatcher.dto.PartCreateDto;
import ru.app.partmatcher.dto.PartDto;
import ru.app.partmatcher.dto.PartUpdateDto;
import ru.app.partmatcher.entity.Part;

public class PartMapper {

    public static PartDto toDto(Part part) {
        if (part == null) {
            return null;
        }
        return PartDto.builder()
                .id(part.getId())
                .article(part.getArticle())
                .name(part.getName())
                .manufacturer(part.getManufacturer())
                .description(part.getDescription())
                .price(part.getPrice())
                .imageUrl(part.getImageUrl())
                .category(part.getCategory())
                .build();
    }

    public static Part toEntity(PartCreateDto dto) {
        return Part.builder()
                .article(dto.getArticle())
                .name(dto.getName())
                .manufacturer(dto.getManufacturer())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .imageUrl(dto.getImageUrl())
                .category(dto.getCategory())
                .build();
    }

    public static void updateFromDto(PartUpdateDto dto, Part part) {
        part.setName(dto.getName());
        part.setManufacturer(dto.getManufacturer());
        part.setDescription(dto.getDescription());
        part.setPrice(dto.getPrice());
        part.setImageUrl(dto.getImageUrl());
        part.setCategory(dto.getCategory());
    }
}
