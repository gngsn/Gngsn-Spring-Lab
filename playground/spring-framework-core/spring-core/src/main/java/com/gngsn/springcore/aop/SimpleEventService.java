package com.gngsn.springcore.aop;

import org.springframework.stereotype.Service;

import javax.sound.midi.Soundbank;

@Service
public class SimpleEventService implements EventService {
    @Override
    @PerfLogging
    public void createEvent() {
//        long begin = System.currentTimeMillis();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Create an event");

//        System.out.println(System.currentTimeMillis() - begin);
        // cross cutting concern
    }

    @Override
    public void publishEvent() {
        long begin = System.currentTimeMillis();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Publish an event");
    }
}
