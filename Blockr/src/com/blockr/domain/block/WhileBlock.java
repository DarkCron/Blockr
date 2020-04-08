package com.blockr.domain.block;

import com.blockr.domain.block.interfaces.ReadOnlyStatementBlock;
import com.blockr.domain.block.interfaces.markers.ReadOnlyWhileBlock;
import com.gameworld.GameWorld;

public class WhileBlock extends ControlFlowBlock implements ReadOnlyWhileBlock {

    @Override
    public StatementBlock execute(GameWorld gameWorld) {

        if(getCurrent() == null){

            var result = getCondition().evaluate(gameWorld);

            if(!result)
                return getNext();

            setCurrent(getBody());
            return this;
        }

        setCurrent(getCurrent().execute(gameWorld));

        return this;
    }

    @Override
    public ReadOnlyStatementBlock getActive() {
        return getCurrent() == null ? this : getCurrent();
    }
}
