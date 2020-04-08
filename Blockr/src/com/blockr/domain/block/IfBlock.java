package com.blockr.domain.block;

import com.blockr.domain.block.interfaces.ReadOnlyStatementBlock;
import com.blockr.domain.block.interfaces.markers.ReadOnlyIfBlock;
import com.gameworld.GameWorld;

public class IfBlock extends ControlFlowBlock implements ReadOnlyIfBlock {

    @Override
    public StatementBlock execute(GameWorld gameWorld) {

        if(getCurrent() == null){

            if(!getCondition().evaluate(gameWorld))
                return getNext();

            setCurrent(getBody());

            return this;
        }

/*
        if(!getCondition().evaluate(gameWorld)) {
            setCurrent(null);
            return getNext();
        }
*/

        var nextBodyStatement = getBody().execute(gameWorld);

        if(nextBodyStatement == null){
            setCurrent(null);
            return getNext();
        }

        setCurrent(nextBodyStatement);
        return this;
    }

    @Override
    public ReadOnlyStatementBlock getActive() {
        return getCurrent() == null ? this : getCurrent();
    }
}
