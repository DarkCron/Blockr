package com.blockr.handlers.blockprogram.getrootblock;

import an.awesome.pipelinr.Command;
import com.blockr.domain.block.interfaces.Block;
import com.blockr.domain.block.interfaces.ReadOnlyBlockProgram;
import com.blockr.domain.block.interfaces.ReadOnlyStatementBlock;

public class GetRootBlock implements Command<ReadOnlyStatementBlock> {
    private ReadOnlyStatementBlock readOnlyStatementBlock;

    public GetRootBlock (ReadOnlyStatementBlock readOnlyStatementBlock){
        this.readOnlyStatementBlock = readOnlyStatementBlock;
    }

    public ReadOnlyStatementBlock getReadOnlyStatementBlock() {
        return readOnlyStatementBlock;
    }
}
