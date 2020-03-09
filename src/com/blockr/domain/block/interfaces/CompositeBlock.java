package com.blockr.domain.block.interfaces;

public interface CompositeBlock {

    void reset();

    ReadOnlyStatementBlock getActive();
}
