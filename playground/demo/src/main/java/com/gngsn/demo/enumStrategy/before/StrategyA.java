package com.gngsn.demo.enumStrategy.before;

class StrategyA implements Strategy {
    @Override
    public void execute() {
        System.out.println("Executing strategy A");
    }
}
