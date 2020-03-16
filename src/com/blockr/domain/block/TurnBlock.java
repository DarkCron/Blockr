package com.blockr.domain.block;

import com.blockr.domain.block.interfaces.Block;
import com.blockr.domain.block.interfaces.markers.ReadOnlyTurnBlock;
import com.blockr.domain.gameworld.GameWorld;

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
        if(getDirection() == Direction.LEFT)
            gameWorld.turnLeft();
        else
            gameWorld.turnRight();

        return getNext();
    }

    public enum Direction {
        LEFT,
        RIGHT
    }
}
