package com.blockr.domain.block;

import com.blockr.domain.block.interfaces.Block;
import com.blockr.domain.block.interfaces.markers.ReadOnlyNotBlock;
import com.gameworld.GameWorld;

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
        var result = getCondition().evaluate(gameWorld);
        return !getCondition().evaluate(gameWorld);
    }
}
