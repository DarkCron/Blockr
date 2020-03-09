package com.blockr.domain.block;

import com.blockr.domain.block.interfaces.ReadOnlyStatementBlock;

public abstract class StatementBlock extends Block implements ReadOnlyStatementBlock {

    @Override
    public Block getNext() {
        return next;
    }

    public void setNext(Block block){
        this.next = block;
    }

    private Block next;
}
