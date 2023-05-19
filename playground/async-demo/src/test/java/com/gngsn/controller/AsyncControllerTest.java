package com.gngsn.controller;

import com.gngsn.configuration.AsyncConfiguration;
import com.gngsn.configuration.BaseAsyncConfiguration;
import com.gngsn.service.AsyncFailureService;
import com.gngsn.service.AsyncService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
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

import java.util.concurrent.TimeUnit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AsyncController.class)
@Import({AsyncConfiguration.class, BaseAsyncConfiguration.class})
class AsyncControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(DemoControllerTest.class);

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AsyncService asyncService;
    @MockBean
    AsyncFailureService asyncFailureService;

    @Test
    public void Basic_Use_Cases() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/v1/async/work").contentType(MediaType.APPLICATION_JSON))
                .andExpect(request().asyncStarted())
                .andDo(MockMvcResultHandlers.log())
                .andReturn();

        mockMvc.perform(asyncDispatch(result))
                .andExpect(status().isOk());
    }

    @Test
    @Timeout(value = 4_000, unit = TimeUnit.MILLISECONDS)
    public void Calling_methods_in_same_class_Failure() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/v1/async/work/methodsInSameClass"))
                .andExpect(request().asyncStarted())
                .andDo(MockMvcResultHandlers.log())
                .andReturn();

        mockMvc.perform(asyncDispatch(result))
                .andExpect(status().isOk());
    }

    @Test
    @Timeout(value = 4_000, unit = TimeUnit.MILLISECONDS)
    public void Calling_methods_in_same_class_Failure_2() throws Exception {
        mockMvc.perform(get("/v1/async/fail/methodsInSameClass"))
                .andExpect(request().asyncStarted())
                .andDo(MockMvcResultHandlers.log())
                .andExpect(status().isOk());
    }
}