package com.blockr.handlers.blockprogram.getavailableblocks;

import com.blockr.State;
import com.blockr.domain.Palette;
import com.blockr.domain.block.interfaces.Block;
import com.blockr.handlers.HandlerBase;

import java.util.List;

public class GetAvailableBlocksHandler extends HandlerBase<GetAvailableBlocks, List<? extends Block>> {

    public GetAvailableBlocksHandler(State state) {
        super(state);
    }

    @Override
    public List<? extends Block> handle(GetAvailableBlocks getAvailableBlocks) {
        return Palette.getAvailableBlocks(getState().getBlockProgram());
    }
}
