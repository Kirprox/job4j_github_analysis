package ru.job4j.github.analysis.service;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.job4j.github.analysis.dto.RepositoryCommits;
import ru.job4j.github.analysis.model.Commit;
import ru.job4j.github.analysis.model.Repository;
import ru.job4j.github.analysis.repository.CommitRepository;
import ru.job4j.github.analysis.repository.RepositoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class RepositoryService {
    private final GitHubService gitHubService;
    private final CommitRepository commitRepository;
    private final RepositoryRepository repositoryRepository;

    @Async
    public void saveRepository(Repository repository) {
        repositoryRepository.save(repository);
    }

    public void createCommits(List<Commit> commits) {
        commitRepository.saveAll(commits);
    }

    public List<Repository> getAllRepositories() {
        return repositoryRepository.findAll();
    }

    public List<RepositoryCommits> getAllCommitsByRepository(String repoName) {
        return commitRepository.findByRepositoryNameOrderByDateDesc(repoName).stream()
                .map(commit -> new RepositoryCommits(commit.getMessage(),
                        commit.getAuthor(), commit.getDate())).collect(Collectors.toList());
    }

    public void fetchCommits() {
        List<Repository> repositories = getAllRepositories();
        for (Repository repository : repositories) {
            List<Commit> commits = gitHubService.fetchCommits(repository);
            createCommits(commits);
            if (!commits.isEmpty()) {
                repository.setLastCommitSha(commits.get(0).getSha());
                saveRepository(repository);
            }
        }
    }
}
