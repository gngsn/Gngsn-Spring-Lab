package com.gngsn.demo.enumStrategy.before;

class StrategyB implements Strategy {
    @Override
    public void execute() {
        System.out.println("Executing strategy B");
    }
}
