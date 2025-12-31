package ru.job4j.github.analysis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.job4j.github.analysis.model.Repository;

public interface RepositoryRepository extends JpaRepository<Repository, Long> {
}
