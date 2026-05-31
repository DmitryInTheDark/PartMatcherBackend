package ru.app.partmatcher.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PartDto {
    private Long id;
    private String article;
    private String name;
    private String manufacturer;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private String category;
}
