package com.gngsn.service;

import com.gngsn.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Service
public class GitHubLookupService {

    private static final Logger logger = LoggerFactory.getLogger(GitHubLookupService.class);

    private final RestTemplate restTemplate;

    public GitHubLookupService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * CASE 1-1. Synchronous
     */
    public User findUserSync(String user) {
        logger.info("[Sync - findUser] Looking up " + user + ", thread: " + Thread.currentThread());
        return restTemplate.getForObject(String.format("https://api.github.com/users/%s", user), User.class);
    }

    /**
     * CASE 1-2. Asynchronous
     */
    @Async
    public CompletableFuture<User> findUser(String user) {
        logger.info("[Async - findUser] Looking up " + user + ", thread: " + Thread.currentThread());
        User results = restTemplate.getForObject(String.format("https://api.github.com/users/%s", user), User.class);

        sleep();
        logger.info("--> " + results);
        return CompletableFuture.completedFuture(results);
    }

    private static void sleep() {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * CASE 2. invoke private self-contained method
     */
    public CompletableFuture<User> invokeFindUser(String user) throws InterruptedException {
        return this.privateFindUser(user);
    }

    @Async
    private CompletableFuture<User> privateFindUser(String user) throws InterruptedException {
        logger.info("Looking up " + user);
        User results = restTemplate.getForObject(String.format("https://api.github.com/users/%s", user), User.class);

        sleep();
        return CompletableFuture.completedFuture(results);
    }
}