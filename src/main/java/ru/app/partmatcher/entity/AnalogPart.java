package ru.app.partmatcher.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "analog_parts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalogPart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "original_part_id", nullable = false)
    private Part originalPart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "analog_part_id", nullable = false)
    private Part analogPart;
}
