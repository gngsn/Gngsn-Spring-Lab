package com.gngsn.accesswhitecidr.controller;

import com.gngsn.accesswhitecidr.service.AllowCidrCheckService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;

import static com.gngsn.accesswhitecidr.utils.IpUtil.getClientIp;

@Slf4j
@SpringBootTest
class SimpleTest {

    @Autowired
    protected AllowCidrCheckService allowCidrCheckService;

    static private MockHttpServletRequest request;

    @BeforeAll
    static void setUp() {
        request = new MockHttpServletRequest();
    }

    @Test
    void given_ValidIP__when_WhiteCidrCheck__then_True() {

        // GIVEN
        String CLIENT_IP = "192.168.0.1";

        request.setMethod("get");
        request.setRequestURI("/test");
        request.setRemoteAddr(CLIENT_IP);

        String ip = getClientIp(request);
        log.info("ip : {}", ip);

        // WHEN
        boolean isWhiteIp = allowCidrCheckService.isWhiteIp(ip);

        // THEN
        log.info("isWhiteIp : {}", isWhiteIp);
        Assertions.assertTrue(isWhiteIp);
    }


    @Test
    void given_InvalidIP__when_WhiteCidrCheck__then_False() {

        // GIVEN
        String CLIENT_IP = "192.168.121.0";

        request.setMethod("get");
        request.setRequestURI("/test");
        request.setRemoteAddr(CLIENT_IP);

        String ip = getClientIp(request);
        log.info("ip : {}", ip);

        // WHEN
        boolean isWhiteIp = allowCidrCheckService.isWhiteIp(ip);

        // THEN
        log.info("isWhiteIp : {}", isWhiteIp);
        Assertions.assertFalse(isWhiteIp);
    }


    @BeforeAll
    static void before() {
        log.info("\n\n\n\n\n\n\n\n\n\n\n\n");
    }

    @AfterAll
    static void after() {
        log.info("\n\n\n\n\n\n\n\n\n\n\n\n");
    }
}