package com.blockr.domain.block;

import com.blockr.domain.gameworld.GameWorld;
import com.blockr.domain.gameworld.TileType;

public class WallInFrontBlock extends ConditionBlock {

    @Override
    public boolean evaluate(GameWorld gameWorld) {
        return gameWorld.getTileType(gameWorld.getRobotPosition().translate(gameWorld.getRobotOrientation().getOffset())) == TileType.Blocked;
    }
}
