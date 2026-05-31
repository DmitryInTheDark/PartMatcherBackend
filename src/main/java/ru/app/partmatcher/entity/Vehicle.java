package ru.app.partmatcher.entity;

import jakarta.persistence.*;
import jakarta.persistence.Index;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "vehicles", indexes = {@Index(name = "idx_vehicles_vin", columnList = "vin")})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String vin;

    private String brand;
    private String model;
    private Integer year;
    private String engine;
    private String bodyType;

    @ManyToMany
    @JoinTable(
            name = "vehicle_parts",
            joinColumns = @JoinColumn(name = "vehicle_id"),
            inverseJoinColumns = @JoinColumn(name = "part_id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private Set<Part> compatibleParts = new HashSet<>();
}
