package com.gngsn.kotlindemo.movie

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class MovieController {

    @RequestMapping("/movies")
    fun welcome(model: Model): String {
        return "demo/index"
    }
}
