package ru.app.partmatcher.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.app.partmatcher.entity.AnalogPart;

import java.util.List;

public interface AnalogPartRepository extends JpaRepository<AnalogPart, Long> {
    List<AnalogPart> findByOriginalPartId(Long originalPartId);
}
