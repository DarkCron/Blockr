package com.blockr.handlers.blockprogram.removeblock;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;
import com.blockr.domain.block.interfaces.ReadOnlyBlock;

public class RemoveBlock implements Command<Voidy> {

    public ReadOnlyBlock getBlock(){
        return block;
    }

    private final ReadOnlyBlock block;

    public RemoveBlock(ReadOnlyBlock block) {
        this.block = block;
    }

}
