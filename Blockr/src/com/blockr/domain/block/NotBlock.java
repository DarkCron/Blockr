package com.blockr.domain.block;

import com.blockr.domain.block.interfaces.ConditionedBlock;
import com.blockr.domain.block.interfaces.markers.ReadOnlyNotBlock;
import com.gameworld.GameWorld;

public class NotBlock extends ConditionBlock implements ReadOnlyNotBlock, ConditionedBlock {

    public ConditionBlock getCondition(){
        return condition;
    }

    public void setCondition(ConditionBlock condition){
        this.condition = condition;
    }

    @Override
    public boolean isReady() {
        return getCondition() != null;
    }

    private ConditionBlock condition;

    @Override
    public boolean evaluate(GameWorld gameWorld) {
        return !getCondition().evaluate(gameWorld);
    }
}
