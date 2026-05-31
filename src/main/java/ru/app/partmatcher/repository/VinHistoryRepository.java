package ru.app.partmatcher.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.app.partmatcher.entity.User;
import ru.app.partmatcher.entity.VinHistory;

import java.util.List;

public interface VinHistoryRepository extends JpaRepository<VinHistory, Long> {
    List<VinHistory> findByUserOrderBySearchedAtDesc(User user);
}
