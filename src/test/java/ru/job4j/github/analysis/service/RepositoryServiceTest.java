package ru.job4j.github.analysis.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.github.analysis.model.Commit;
import ru.job4j.github.analysis.model.Repository;
import ru.job4j.github.analysis.repository.CommitRepository;
import ru.job4j.github.analysis.repository.RepositoryRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class RepositoryServiceTest {

    private GitHubService gitHubService;
    private CommitRepository commitRepository;
    private RepositoryRepository repositoryRepository;
    private RepositoryService repositoryService;

    @BeforeEach
    void setUp() {
        gitHubService = mock(GitHubService.class);
        commitRepository = mock(CommitRepository.class);
        repositoryRepository = mock(RepositoryRepository.class);
        repositoryService = new RepositoryService(gitHubService, commitRepository, repositoryRepository);
    }

    @Test
    void fetchCommitsSavesNewCommitsAndUpdatesRepository() {
        Repository repo = new Repository();
        repo.setName("test-repo");

        Commit commit1 = new Commit("sha1", "message1", "author1", LocalDateTime.now(), repo);
        Commit commit2 = new Commit("sha2", "message2", "author2", LocalDateTime.now(), repo);
        List<Commit> commits = List.of(commit1, commit2);

        // Моки
        when(repositoryRepository.findAll()).thenReturn(List.of(repo));
        when(gitHubService.fetchCommits(repo)).thenReturn(commits);

        // Вызов
        repositoryService.fetchCommits();

        // Проверка
        verify(commitRepository, times(1)).saveAll(commits);
        assertEquals("sha1", repo.getLastCommitSha());
        verify(repositoryRepository, times(1)).save(repo);
    }
}