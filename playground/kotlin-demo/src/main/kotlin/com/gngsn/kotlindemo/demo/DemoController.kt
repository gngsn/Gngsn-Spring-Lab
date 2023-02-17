package com.gngsn.kotlindemo.demo

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping

@Controller
class DemoController {

    @GetMapping("/demo")
    fun welcome(model: Model): String {
        model["title"] = "This is Demo View!"
        return "demo/index"
    }
}