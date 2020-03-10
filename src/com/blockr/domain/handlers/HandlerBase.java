package com.blockr.domain.handlers;

import an.awesome.pipelinr.Command;
import com.blockr.domain.State;

public abstract class HandlerBase<C extends Command<R>, R> implements Command.Handler<C, R> {

    protected State getState(){
        return state;
    }

    private final State state;

    public HandlerBase(State state){
        this.state = state;
    }

}
