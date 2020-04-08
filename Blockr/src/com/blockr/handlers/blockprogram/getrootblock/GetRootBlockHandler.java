package com.blockr.handlers.blockprogram.getrootblock;

import com.blockr.domain.State;
import com.blockr.domain.block.interfaces.ReadOnlyBlockProgram;
import com.blockr.domain.block.interfaces.ReadOnlyStatementBlock;
import com.blockr.handlers.HandlerBase;

public class GetRootBlockHandler extends HandlerBase<GetRootBlock, ReadOnlyStatementBlock> {

    public GetRootBlockHandler(State state) {
        super(state);
    }

    @Override
    public ReadOnlyStatementBlock handle(GetRootBlock getBlockProgram) {
        //return getState().getBlockProgram().getRootBlock(getBlockProgram.getReadOnlyStatementBlock());
        return null;
    }
}
