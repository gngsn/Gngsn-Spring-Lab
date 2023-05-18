package com.gngsn.controller;

import com.gngsn.service.GitHubLookupService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DemoController.class)
@EnableAsync
@AutoConfigureMockMvc
@Profile("test")
class DemoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    GitHubLookupService gitHubLookupService;

    @MockBean
    RestTemplate restTemplate;

    @Test
    @DisplayName("ss")
    void It_creates_the_product_and_returns_it() throws Exception {
        var result = mockMvc.perform(
                get("/async")
                        .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().is2xxSuccessful());
//        verify(productService).create(any(Product.class));
    }
}
