package com.gngsn.kotlindemo.ch5;

public class LambdaJava {
    static public void postponeComputation(int delay, Runnable computation) {
        try {
            Thread.sleep(delay);
        } catch (Exception e) {
            System.out.println("Error occurs during thread sleep");
        }

        computation.run();
    }
}