package com.blockr.handlers.blockprogram.connectcontrolflowbody;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;
import com.blockr.domain.block.interfaces.ReadOnlyControlFlowBlock;
import com.blockr.domain.block.interfaces.ReadOnlyStatementBlock;

public class ConnectControlFlowBody implements Command<Voidy> {

    public ReadOnlyControlFlowBlock getControlFlowBlock(){
        return controlFlowBlock;
    }

    private final ReadOnlyControlFlowBlock controlFlowBlock;

    public ReadOnlyStatementBlock getStatementBlock(){
        return statementBlock;
    }

    private final ReadOnlyStatementBlock statementBlock;

    public ConnectControlFlowBody(ReadOnlyControlFlowBlock controlFlowBlock, ReadOnlyStatementBlock statementBlock) {
        this.controlFlowBlock = controlFlowBlock;
        this.statementBlock = statementBlock;
    }
}
