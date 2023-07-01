package com.gngsn.controller;

import com.gngsn.configuration.AsyncConfiguration;
import com.gngsn.service.GitHubLookupService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DemoController.class)
@Import(AsyncConfiguration.class)
class DemoControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(DemoControllerTest.class);

    @Autowired
    MockMvc mockMvc;

    @MockBean
    GitHubLookupService gitHubLookupService;

    @MockBean
    RestTemplate restTemplate;

    @Test
    @DisplayName("Sync test")
    void Sync_test() throws Exception {
        var result = mockMvc.perform(
                get("/sync")
                        .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("Async test")
    void Async_test() throws Exception {

        MvcResult result = mockMvc.perform(
                get("/async?names=PivotalSoftware&names=CloudFoundry&names=Spring-Projects").contentType(MediaType.APPLICATION_JSON))
                .andExpect(request().asyncStarted())
                .andDo(MockMvcResultHandlers.log())
                .andReturn();


        mockMvc.perform(asyncDispatch(result))
                .andExpect(status().isOk());
//                .andExpect(content().contentTypeCompatibleWith("text/plain"))
//                .andExpect(content().string("Hello World !!"));
//        result.andExpect(status().is2xxSuccessful());
//        verify(productService).create(any(Product.class));
    }
}