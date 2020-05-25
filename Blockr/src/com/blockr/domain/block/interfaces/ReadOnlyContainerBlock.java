package com.blockr.domain.block.interfaces;

import com.blockr.domain.block.StatementBlock;

public interface ReadOnlyContainerBlock extends Block {

    StatementBlock getBody();

}
