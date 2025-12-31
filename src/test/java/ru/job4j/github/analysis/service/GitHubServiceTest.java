package ru.job4j.github.analysis.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import ru.job4j.github.analysis.model.Commit;
import ru.job4j.github.analysis.model.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class GitHubServiceTest {

    private GitHubService gitHubService;
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        gitHubService = new GitHubService();
        gitHubService.setRestTemplate(restTemplate);
    }

    @Test
    void fetchCommitsReturnsCommitsFromJsonNode() throws Exception {
        Repository repo = new Repository();
        repo.setOwner("user");
        repo.setName("repo");
        repo.setLastCommitSha(null);

        String json = """
        [
          {
            "sha": "abc123",
            "commit": {
              "author": { "name": "Alice", "date": "2025-12-31T12:00:00Z" },
              "message": "Initial commit"
            }
          }
        ]
        """;

        ObjectMapper mapper = new ObjectMapper();
        JsonNode mockJson = mapper.readTree(json);

        ResponseEntity<JsonNode> response = new ResponseEntity<>(mockJson, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), eq(JsonNode.class)))
                .thenReturn(response);

        List<Commit> commits = gitHubService.fetchCommits(repo);

        assertEquals(1, commits.size());
        Commit commit = commits.get(0);
        assertEquals("abc123", commit.getSha());
        assertEquals("Alice", commit.getAuthor());
        assertEquals("Initial commit", commit.getMessage());
        assertEquals(LocalDateTime.of(2025, 12, 31, 12, 0), commit.getDate());
        assertEquals(repo, commit.getRepository());
    }
}