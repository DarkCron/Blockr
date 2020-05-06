package com.blockr.domain.block.interfaces.markers;

import com.blockr.domain.block.interfaces.ReadOnlyStatementBlock;

public interface ReadOnlyFunctionBlock extends ReadOnlyStatementBlock {

    ReadOnlyStatementBlock getBody();

}
