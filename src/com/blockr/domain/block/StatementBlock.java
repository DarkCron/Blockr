package com.blockr.domain.block;

import com.blockr.domain.block.interfaces.ReadOnlyStatementBlock;
import com.blockr.domain.gameworld.GameWorld;


public abstract class StatementBlock implements ReadOnlyStatementBlock {

    public StatementBlock getNext() {
        return next;
    }

    public void setNext(StatementBlock block){
        this.next = block;
    }

    private StatementBlock next;

    public StatementBlock getPrevious(){
        return previous;
    }

    public void setPrevious(StatementBlock block){
        this.previous = block;
    }

    private StatementBlock previous;

    public abstract StatementBlock execute(GameWorld gameWorld);

    public ReadOnlyStatementBlock getActive(){
        return this;
    }
}
