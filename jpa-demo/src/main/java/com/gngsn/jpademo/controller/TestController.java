package com.gngsn.jpademo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class TestController {

    @RequestMapping("/")
    public String thymeleaf() {
        log.info("thymeleaf index test");
        return "index";
    }
}
