package com.gngsn.demo.enumStrategy.before;

public class UseStrategy {

    public static void main(String[] args){

        Context context = new Context();

        context.setStrategy(new StrategyA());
        context.executeStrategy();

        context.setStrategy(new StrategyB());
        context.executeStrategy();
    }
}

