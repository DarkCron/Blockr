package com.blockr.handlers.blockprogram.insertBlockInProgram;

import an.awesome.pipelinr.Voidy;
import com.blockr.domain.State;
import com.blockr.domain.block.interfaces.Block;
import com.blockr.handlers.HandlerBase;

public class InsertBlockInProgramHandler extends HandlerBase<InsertBlockInProgram, Block> {

    public InsertBlockInProgramHandler(State state) {
        super(state);
    }

    @Override
    public Block handle(InsertBlockInProgram insertBlockInProgram) {
        return getState().getBlockProgram().processInsertBlock(insertBlockInProgram.getProgramBlockInsertInfo());
    }
}
