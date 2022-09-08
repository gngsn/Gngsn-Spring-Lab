package com.gngsn.accesswhitecidr.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class Test {

    @GetMapping("/test")
    public String get() {
        return "Test Success";
    }
}
