package com.blockr.domain.block.interfaces;

import com.blockr.domain.block.TurnBlock;

public interface ReadOnlyTurnBlock {
    TurnBlock.Direction getDirection();
}
