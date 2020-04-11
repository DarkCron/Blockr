package com.blockr.handlers.blockprogram.getblockprogram;

import com.blockr.State;
import com.blockr.domain.block.interfaces.ReadOnlyBlockProgram;
import com.blockr.handlers.HandlerBase;

public class GetBlockProgramHandler extends HandlerBase<GetBlockProgram, ReadOnlyBlockProgram> {

    public GetBlockProgramHandler(State state) {
        super(state);
    }

    @Override
    public ReadOnlyBlockProgram handle(GetBlockProgram getBlockProgram) {
        return getState().getBlockProgram();
    }
}
