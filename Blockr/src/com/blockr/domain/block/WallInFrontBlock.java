package com.blockr.domain.block;

import com.gameworld.GameWorld;
import com.gameworld.Predicate;

public class WallInFrontBlock extends ConditionBlock {

    @Override
    public boolean evaluate(GameWorld gameWorld) {
        return gameWorld.evaluate(Predicate.WALL_IN_FRONT);
    }
}
