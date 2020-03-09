package com.blockr.domain.block;

import com.blockr.domain.block.interfaces.ReadOnlyMoveForwardBlock;
import com.blockr.domain.gameworld.GameWorld;

public class MoveForwardBlock extends StatementBlock implements ReadOnlyMoveForwardBlock {

    @Override
    public Block execute(GameWorld gameWorld) {
        gameWorld.moveForward();
        return getNext();
    }
}
