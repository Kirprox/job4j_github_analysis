package ru.job4j.github.analysis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class RepositoryCommits {
    private String message;
    private String authorName;
    private LocalDateTime date;
}
