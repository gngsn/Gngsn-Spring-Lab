package com.gngsn.demo.enumStrategy.before;


import lombok.Setter;

@Setter
public class Context {

    private Strategy strategy;

    public void setStrategy(Strategy strategy){
        this.strategy = strategy;
    }

    public void executeStrategy(){
        this.strategy.execute();
    }
}
