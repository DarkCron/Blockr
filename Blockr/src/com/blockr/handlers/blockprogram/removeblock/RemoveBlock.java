package com.blockr.handlers.blockprogram.removeblock;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;
import com.blockr.domain.block.interfaces.Block;

public class RemoveBlock implements Command<Voidy> {

    public Block getBlock(){
        return block;
    }

    private final Block block;

    public RemoveBlock(Block block) {
        this.block = block;
    }

}
