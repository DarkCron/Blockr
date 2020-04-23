package com.blockr.handlers.blockprogram.connectCFandCondition;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;
import com.blockr.domain.block.interfaces.ReadOnlyControlFlowBlock;
import com.blockr.domain.block.interfaces.ReadOnlyStatementBlock;
import com.blockr.domain.block.interfaces.markers.ReadOnlyConditionBlock;

public class ConnectControlFlowAndCondition implements Command<Voidy> {

    public ReadOnlyControlFlowBlock getControlFlowBlock(){
        return controlFlowBlock;
    }

    private final ReadOnlyControlFlowBlock controlFlowBlock;

    public ReadOnlyConditionBlock getStatementBlock(){
        return statementBlock;
    }

    private final ReadOnlyConditionBlock statementBlock;

    public ConnectControlFlowAndCondition(ReadOnlyControlFlowBlock controlFlowBlock, ReadOnlyConditionBlock statementBlock) {
        this.controlFlowBlock = controlFlowBlock;
        this.statementBlock = statementBlock;
    }
}
