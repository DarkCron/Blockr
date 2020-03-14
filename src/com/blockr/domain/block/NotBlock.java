package com.blockr.domain.block;

import com.blockr.domain.block.interfaces.Block;
import com.blockr.domain.block.interfaces.markers.ReadOnlyNotBlock;
import com.blockr.domain.gameworld.GameWorld;

public class NotBlock extends ConditionBlock implements ReadOnlyNotBlock {

    public ConditionBlock getCondition(){
        return condition;
    }

    public void setCondition(ConditionBlock condition){
        this.condition = condition;
    }

    private ConditionBlock condition;

    @Override
    public boolean evaluate(GameWorld gameWorld) {
        return !getCondition().evaluate(gameWorld);
    }

    @Override
    public Block getEmptyCopy() {
        return new NotBlock();
    }
}
