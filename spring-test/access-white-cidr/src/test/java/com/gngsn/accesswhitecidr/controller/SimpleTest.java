package com.gngsn.accesswhitecidr.controller;

import com.gngsn.accesswhitecidr.service.AllowCidrCheckService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
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

    @Autowired
    private MockHttpServletRequest request;

    @Test
    void accessCidrTest() {
        request = new MockHttpServletRequest("get", "/test");
        String ip = "192.168.0.1";
        request.setRemoteAddr(ip);

        String requestIp = getClientIp(request);
        log.info("ip : " + requestIp);

        boolean isWhiteIp = allowCidrCheckService.isWhiteIp(requestIp);
        Assertions.assertTrue(isWhiteIp);
    }


    @Test
    void accessCidrFalseTest() {
        request = new MockHttpServletRequest("get", "/test");
        String ip = "192.168.121.0";
        request.setRemoteAddr(ip);

        String requestIp = getClientIp(request);
        log.info("ip : " + requestIp);

        boolean isWhiteIp = allowCidrCheckService.isWhiteIp(requestIp);
        Assertions.assertFalse(isWhiteIp);
    }
}