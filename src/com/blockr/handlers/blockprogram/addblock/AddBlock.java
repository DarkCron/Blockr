package com.blockr.handlers.blockprogram.addblock;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;
import com.blockr.domain.block.interfaces.ReadOnlyStatementBlock;

public class AddBlock implements Command<Voidy> {

    public ReadOnlyStatementBlock getStatementBlock(){
        return statementBlock;
    }

    private final ReadOnlyStatementBlock statementBlock;

    public AddBlock(ReadOnlyStatementBlock statementBlock) {
        this.statementBlock = statementBlock;
    }

}
