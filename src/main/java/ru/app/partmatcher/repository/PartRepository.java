package ru.app.partmatcher.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.app.partmatcher.entity.Part;

import java.util.List;

public interface PartRepository extends JpaRepository<Part, Long> {
    List<Part> findByNameIgnoreCaseContainingOrArticleIgnoreCaseContainingOrCategoryIgnoreCaseContaining(String name, String article, String category);
    boolean existsByArticleIgnoreCase(String article);
}
