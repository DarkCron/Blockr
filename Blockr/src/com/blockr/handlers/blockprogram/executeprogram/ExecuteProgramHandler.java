package com.blockr.handlers.blockprogram.executeprogram;

import an.awesome.pipelinr.Voidy;
import com.blockr.domain.State;
import com.blockr.domain.block.interfaces.ReadOnlyBlockProgram;
import com.blockr.handlers.HandlerBase;

public class ExecuteProgramHandler extends HandlerBase<ExecuteProgram, ReadOnlyBlockProgram> {

    public ExecuteProgramHandler(State state) {
        super(state);
    }

    @Override
    public ReadOnlyBlockProgram handle(ExecuteProgram executeProgram) {
        return getState().getBlockProgram().startExecute();
    }
}
