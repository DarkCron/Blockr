package com.blockr.domain.block;

import com.blockr.domain.block.interfaces.ReadOnlyBlock;
import com.blockr.domain.gameworld.GameWorld;

public abstract class Block implements ReadOnlyBlock {

    public Block getPrevious(){
        return previous;
    }

    public void setPrevious(Block block){
        this.previous = block;
    }

    private Block previous;

    public abstract Block execute(GameWorld gameWorld);
}
