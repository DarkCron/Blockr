package com.blockr.domain.block;

import com.blockr.domain.gameworld.GameWorld;

public class TurnBlock extends StatementBlock {

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

    @Override
    public Block execute(GameWorld gameWorld) {
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
