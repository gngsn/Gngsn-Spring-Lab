package com.gngsn.demo.EnumStrategy.before;

class StrategyA implements Strategy {
    @Override
    public void execute() {
        System.out.println("Executing strategy A");
    }
}
