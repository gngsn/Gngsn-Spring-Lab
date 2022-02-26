package com.gngsn.springcore.event;

import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AnotherHandler {

    @EventListener
//    @Async
    public void handle(MyEvent myEvent) {
        System.out.println(AnotherHandler.class);
        System.out.println(Thread.currentThread().toString());
        System.out.println("Another " + myEvent.getData());
    }


    @EventListener
    public void handle(ContextRefreshedEvent event) {
        System.out.println(Thread.currentThread().toString());
        System.out.println("[ContextRefreshedEvent]\n" + event.getSource());
    }

    @EventListener
    public void handle(ContextClosedEvent event) {
        System.out.println(Thread.currentThread().toString());
        System.out.println("[ContextClosedEvent]\n" + event.getSource());
    }
}
