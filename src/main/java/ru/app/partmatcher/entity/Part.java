package ru.app.partmatcher.entity;

import jakarta.persistence.*;
import jakarta.persistence.Index;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "parts", indexes = {@Index(name = "idx_parts_article", columnList = "article")})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Part {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String article;

    private String name;
    private String manufacturer;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private String category;

    @ManyToMany(mappedBy = "compatibleParts")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private Set<Vehicle> compatibleVehicles = new HashSet<>();

    @OneToMany(mappedBy = "originalPart", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private Set<AnalogPart> analogs = new HashSet<>();
}
