package com.gngsn.demo.enumStrategy;

enum Strategy {
    STRATEGY_A {
        @Override
        void execute() {
            System.out.println("Executing strategy A");
        }

    },
    STRATEGY_B {
        @Override
        void execute() {
            System.out.println("Executing strategy B");
        }
    };

    abstract void execute();
}
