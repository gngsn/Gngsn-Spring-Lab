package com.gngsn.demo.EnumStrategy;


import lombok.Getter;
import lombok.Setter;

@Getter
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
