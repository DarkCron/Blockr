package com.blockr.domain.block.interfaces;

public interface ReadOnlyStatementBlock extends Block {

    ReadOnlyStatementBlock getPrevious();

    ReadOnlyStatementBlock getNext();

}
