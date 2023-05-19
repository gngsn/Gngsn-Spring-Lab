package com.gngsn.controller;

import com.gngsn.entity.User;
import com.gngsn.service.GitHubLookupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
public class DemoController {
    private static final Logger logger = LoggerFactory.getLogger(DemoController.class);

    private final GitHubLookupService gitHubLookupService;

    public DemoController(GitHubLookupService gitHubLookupService) {
        this.gitHubLookupService = gitHubLookupService;
    }

    @GetMapping("/sync")
    public void sync() throws Exception {
        long start = System.currentTimeMillis();

        User page1 = gitHubLookupService.findUserSync("PivotalSoftware");
        User page2 = gitHubLookupService.findUserSync("CloudFoundry");
        User page3 = gitHubLookupService.findUserSync("Spring-Projects");

        logger.info("Elapsed time: " + (System.currentTimeMillis() - start));
        logger.info("--> " + page1);
        logger.info("--> " + page2);
        logger.info("--> " + page3);
    }

    @GetMapping("/async")
    public void async() throws Exception {
        long start = System.currentTimeMillis();

        CompletableFuture<User> page1 = gitHubLookupService.invokeFindUser("PivotalSoftware");
        CompletableFuture<User> page2 = gitHubLookupService.invokeFindUser("CloudFoundry");
        CompletableFuture<User> page3 = gitHubLookupService.invokeFindUser("Spring-Projects");

        CompletableFuture.allOf(page1,page2,page3).join();

        logger.info("Elapsed time: " + (System.currentTimeMillis() - start));
        logger.info("--> " + page1.get());
        logger.info("--> " + page2.get());
        logger.info("--> " + page3.get());
    }

}