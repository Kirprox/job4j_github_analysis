package ru.job4j.github.analysis.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.job4j.github.analysis.model.Repository;

@Service
public class RepositoryService {
    @Async
    public void create(Repository repository) {

    }
}
