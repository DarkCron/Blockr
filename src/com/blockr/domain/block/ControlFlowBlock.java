package com.blockr.domain.block;

import com.blockr.domain.block.interfaces.CompositeBlock;
import com.blockr.domain.block.interfaces.ReadOnlyControlFlowBlock;
import com.blockr.domain.block.interfaces.ReadOnlyStatementBlock;

public abstract class ControlFlowBlock extends StatementBlock implements ReadOnlyControlFlowBlock, CompositeBlock {

    @Override
    public ConditionBlock getCondition() {
        return condition;
    }

    public void setCondition(ConditionBlock condition){
        this.condition = condition;
    }

    private ConditionBlock condition;

    @Override
    public StatementBlock getBody() {
        return body;
    }

    public void setBody(StatementBlock statementBlock){
        this.body = statementBlock;
    }

    private StatementBlock body;

    protected StatementBlock getCurrent(){
        return current;
    }

    protected void setCurrent(StatementBlock current){
        this.current = current;
    }

    private StatementBlock current;

    @Override
    public ReadOnlyStatementBlock getActive(){
        return getBody().getActive();
    }

    @Override
    public boolean isReady() {
        return getCondition() != null && getBody() != null;
    }
}
