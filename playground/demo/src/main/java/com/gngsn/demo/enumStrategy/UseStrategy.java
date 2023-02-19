package com.gngsn.demo.enumStrategy;


public class UseStrategy {

    public static void main(String[] args) {

        Context context = new Context();

        context.setStrategy(Strategy.STRATEGY_A);
        context.executeStrategy();

        context.setStrategy(Strategy.STRATEGY_B);
        context.executeStrategy();
    }

    private void perform(Strategy strategy) {
        strategy.execute();
    }
}

