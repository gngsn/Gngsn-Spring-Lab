//package com.gngsn.service;
//
//import com.gngsn.configuration.AsyncConfiguration;
//import com.gngsn.controller.DemoController;
//import com.gngsn.entity.User;
//import org.junit.jupiter.api.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Import;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.ExecutionException;
//
//@WebMvcTest(DemoController.class)
//@Import(AsyncConfiguration.class)
//class GitHubLookupServiceTest {
//
//    private static final Logger logger = LoggerFactory.getLogger(GitHubLookupServiceTest.class);
//
//    @MockBean
//    GitHubLookupService gitHubLookupService;
//
//
//    @MockBean
//    RestTemplate restTemplate;
//
//
//    @Test
//    public void test_asnyc() throws ExecutionException, InterruptedException {
//        long start = System.currentTimeMillis();
//
//        CompletableFuture<User> page1 = gitHubLookupService.findUser("PivotalSoftware");
//        CompletableFuture<User> page2 = gitHubLookupService.findUser("CloudFoundry");
//        CompletableFuture<User> page3 = gitHubLookupService.findUser("Spring-Projects");
//
//        CompletableFuture.allOf(page1, page2, page3).join();
//
//        logger.info("Elapsed time: " + (System.currentTimeMillis() - start));
//        logger.info("--> " + page1.get());
//        logger.info("--> " + page2.get());
//        logger.info("--> " + page3.get());
//    }
//}