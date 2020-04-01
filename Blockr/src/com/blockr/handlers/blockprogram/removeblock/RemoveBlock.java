package com.blockr.handlers.blockprogram.removeblock;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;
import com.blockr.domain.block.interfaces.ReadOnlyStatementBlock;

public class RemoveBlock implements Command<Voidy> {

    public ReadOnlyStatementBlock getStatementBlock(){
        return statementBlock;
    }

    private final ReadOnlyStatementBlock statementBlock;

    public RemoveBlock(ReadOnlyStatementBlock statementBlock) {
        this.statementBlock = statementBlock;
    }

}
