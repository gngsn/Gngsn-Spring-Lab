package com.gngsn.kotlindemo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.PropertySource
import org.springframework.context.annotation.PropertySources


@PropertySources(*[PropertySource("classpath:jdbc.properties")])
@SpringBootApplication
class KotlinDemoApplication

fun main(args: Array<String>) {
    println("Kotlin Application is RUNNING!")
    runApplication<KotlinDemoApplication>(*args)
}
