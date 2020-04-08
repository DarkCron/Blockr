package com.blockr.domain.block;

import com.blockr.domain.block.interfaces.Block;
import com.blockr.domain.block.interfaces.markers.ReadOnlyTurnBlock;
import com.gameworld.Action;
import com.gameworld.GameWorld;

public class TurnBlock extends StatementBlock implements ReadOnlyTurnBlock {

    public Direction getDirection(){
        return direction;
    }

    public void setDirection(Direction direction){

        if(direction == null){
            throw new IllegalArgumentException("direction must be effective");
        }

        this.direction = direction;
    }

    private Direction direction = Direction.LEFT;

    public StatementBlock execute(GameWorld gameWorld) {

        if (direction == Direction.LEFT) {
            gameWorld.execute(Action.TURN_LEFT);
        } else {
            gameWorld.execute(Action.TURN_RIGHT);
        }

        return getNext();
    }

    public enum Direction {
        LEFT,
        RIGHT
    }
}
