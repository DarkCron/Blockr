package com.blockr.handlers.blockprogram.addblock;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;
import com.blockr.domain.block.interfaces.ReadOnlyBlock;

public class AddBlock implements Command<Voidy> {

    public ReadOnlyBlock getBlock(){
        return block;
    }

    private final ReadOnlyBlock block;

    public AddBlock(ReadOnlyBlock block) {
        this.block = block;
    }

}
