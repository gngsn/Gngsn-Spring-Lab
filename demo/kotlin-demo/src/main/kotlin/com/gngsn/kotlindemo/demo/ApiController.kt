package com.gngsn.kotlindemo.demo

import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ApiController {

    @GetMapping("/api")
    fun welcome(model: Model): ResBody {
        return ResBody("api test")
    }

    class ResBody(val message: String) {}
}


