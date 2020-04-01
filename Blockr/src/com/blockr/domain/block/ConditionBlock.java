package com.blockr.domain.block;

import com.blockr.domain.block.interfaces.Block;
import com.blockr.domain.block.interfaces.markers.ReadOnlyConditionBlock;
import com.blockr.domain.gameworld.GameWorld;

public abstract class ConditionBlock implements ReadOnlyConditionBlock {

    public abstract boolean evaluate(GameWorld gameWorld);

}
