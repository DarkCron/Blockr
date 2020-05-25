package com.blockr.handlers.blockprogram.addblock;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;
import com.blockr.domain.block.interfaces.Block;

public class AddBlock implements Command<Voidy> {

    public Block getBlock(){
        return block;
    }

    private final Block block;

    public AddBlock(Block block) {
        this.block = block;
    }

}
