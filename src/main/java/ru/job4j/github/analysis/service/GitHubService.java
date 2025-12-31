package ru.job4j.github.analysis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.job4j.github.analysis.model.Commit;
import ru.job4j.github.analysis.model.Repository;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class GitHubService {

    @Autowired
    private RestTemplate restTemplate;

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Repository> fetchRepositories(String username) {
        String url = "https://api.github.com/users/" + username + "/repos";
        ResponseEntity<List<Repository>> response = restTemplate.exchange(
                url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Repository>>() {
                });
        return response.getBody();
    }

    public List<Commit> fetchCommits(Repository repository) {
        String lastSha = repository.getLastCommitSha();
        String url = String.format("https://api.github.com/repos/%s/%s/commits",
                repository.getOwner(), repository.getName());
        List<Commit> commits = new ArrayList<>();
        ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.GET,
                null, JsonNode.class);
        JsonNode root = response.getBody();
        for (JsonNode node : root) {
            String sha = node.path("sha").asText();
            if (sha != null && sha.equals(lastSha)) {
                break;
            }
            String message = node.path("commit").path("message").asText();
            String author = node.path("commit").path("author").path("name").asText();
            LocalDateTime date = OffsetDateTime
                    .parse(node.path("commit").path("author").path("date").asText())
                    .toLocalDateTime();
            commits.add(new Commit(sha, message, author, date, repository));
        }
        return commits;
    }
}