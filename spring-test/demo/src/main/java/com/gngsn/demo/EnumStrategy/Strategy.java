package com.gngsn.demo.EnumStrategy;

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
