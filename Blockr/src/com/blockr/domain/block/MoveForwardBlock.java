package com.blockr.domain.block;
import com.blockr.domain.block.interfaces.Block;
import com.blockr.domain.block.interfaces.markers.ReadOnlyMoveForwardBlock;
import com.gameworld.Action;
import com.gameworld.GameWorld;

public class MoveForwardBlock extends StatementBlock implements ReadOnlyMoveForwardBlock {

    public StatementBlock execute(GameWorld gameWorld) {
        gameWorld.execute(Action.MOVE_FORWARD);
        return getNext();
    }
}
