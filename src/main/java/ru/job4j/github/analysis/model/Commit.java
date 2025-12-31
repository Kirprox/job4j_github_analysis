package ru.job4j.github.analysis.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "commits")
public class Commit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sha;
    private String message;
    private String author;
    private LocalDateTime date;
    @ManyToOne
    @JoinColumn(name = "repository_id")
    private Repository repository;

    public Commit(String sha, String message, String author, LocalDateTime date, Repository repository) {
        this.sha = sha;
        this.message = message;
        this.author = author;
        this.date = date;
        this.repository = repository;
    }
}
