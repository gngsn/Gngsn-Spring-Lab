package com.gngsn.springcore.aop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

//@Component
public class AppRunner9 implements ApplicationRunner {
    @Autowired
    EventService eventService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("======= Spring AOP AppRunner =======");
        eventService.createEvent();
        eventService.publishEvent();
    }
}
