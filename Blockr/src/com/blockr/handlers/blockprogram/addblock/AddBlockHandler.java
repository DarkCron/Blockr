package com.blockr.handlers.blockprogram.addblock;

import an.awesome.pipelinr.Voidy;
import com.blockr.State;
import com.blockr.handlers.HandlerBase;

public class AddBlockHandler extends HandlerBase<AddBlock, Voidy> {

    public AddBlockHandler(State state) {
        super(state);
    }

    @Override
    public Voidy handle(AddBlock addBlock) {
        getState().getBlockProgram().addBlock(addBlock.getBlock());
        return new Voidy();
    }


}
