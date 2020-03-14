package com.blockr.handlers.blockprogram.executeprogram;

import an.awesome.pipelinr.Voidy;
import com.blockr.domain.State;
import com.blockr.handlers.HandlerBase;

public class ExecuteProgramHandler extends HandlerBase<ExecuteProgram, Voidy> {

    public ExecuteProgramHandler(State state) {
        super(state);
    }

    @Override
    public Voidy handle(ExecuteProgram executeProgram) {
        getState().getBlockProgram().executeNext();
        return new Voidy();
    }
}
