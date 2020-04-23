package com.blockr.domain.block;

import com.blockr.domain.block.interfaces.markers.ReadOnlyWallInFrontBlock;
import com.gameworld.GameWorld;
import com.gameworld.Predicate;

public class WallInFrontBlock extends ConditionBlock implements ReadOnlyWallInFrontBlock {

    @Override
    public boolean evaluate(GameWorld gameWorld) {
        return gameWorld.evaluate(Predicate.WALL_IN_FRONT);
    }
}
