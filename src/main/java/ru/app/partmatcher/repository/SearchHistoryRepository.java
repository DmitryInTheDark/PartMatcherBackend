package ru.app.partmatcher.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.app.partmatcher.entity.SearchHistory;
import ru.app.partmatcher.entity.User;

import java.util.List;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {
    List<SearchHistory> findByUserOrderBySearchedAtDesc(User user);
}
