package com.gngsn.demo.EnumStrategy.before;

class StrategyB implements Strategy {
    @Override
    public void execute() {
        System.out.println("Executing strategy B");
    }
}
