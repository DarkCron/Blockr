package com.blockr.domain.block.interfaces;

import com.blockr.domain.block.StatementBlock;

public interface ContainerBlock extends ReadOnlyContainerBlock {

    void setBody(StatementBlock statementBlock);

}
