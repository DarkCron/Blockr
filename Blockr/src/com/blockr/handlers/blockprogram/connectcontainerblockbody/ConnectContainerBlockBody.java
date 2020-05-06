package com.blockr.handlers.blockprogram.connectcontainerblockbody;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;
import com.blockr.domain.block.interfaces.ReadOnlyContainerBlock;
import com.blockr.domain.block.interfaces.ReadOnlyStatementBlock;

public class ConnectContainerBlockBody implements Command<Voidy> {

    public ReadOnlyContainerBlock getContainerBlock(){
        return containerBlock;
    }

    private final ReadOnlyContainerBlock containerBlock;

    public ReadOnlyStatementBlock getStatementBlock(){
        return statementBlock;
    }

    private final ReadOnlyStatementBlock statementBlock;

    public ConnectContainerBlockBody(ReadOnlyContainerBlock containerBlock, ReadOnlyStatementBlock statementBlock) {
        this.containerBlock = containerBlock;
        this.statementBlock = statementBlock;
    }
}
