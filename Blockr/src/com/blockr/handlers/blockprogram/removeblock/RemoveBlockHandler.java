package com.blockr.handlers.blockprogram.removeblock;

import an.awesome.pipelinr.Voidy;
import com.blockr.domain.State;
import com.blockr.handlers.HandlerBase;

public class RemoveBlockHandler extends HandlerBase<RemoveBlock, Voidy> {

    public RemoveBlockHandler(State state) {
        super(state);
    }

    @Override
    public Voidy handle(RemoveBlock removeBlock) {
        getState().getBlockProgram().removeBlock(removeBlock.getBlock());
        return new Voidy();
    }


}
