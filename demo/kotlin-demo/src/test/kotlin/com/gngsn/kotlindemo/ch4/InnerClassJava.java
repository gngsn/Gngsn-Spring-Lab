package com.gngsn.kotlindemo.ch4;

public class InnerClassJava implements View {
    @Override
    public State getCurrentState() {
        return new ButtonState();
    }

    @Override
    public void restoreState(State state) {
        /*...*/
    }

    public class ButtonState implements State {
        /*...*/
    }
}