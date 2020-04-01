package com.blockr.domain.block.interfaces.markers;

import com.blockr.domain.block.TurnBlock;
import com.blockr.domain.block.interfaces.ReadOnlyStatementBlock;

public interface ReadOnlyTurnBlock  extends ReadOnlyStatementBlock {
    TurnBlock.Direction getDirection();
}
