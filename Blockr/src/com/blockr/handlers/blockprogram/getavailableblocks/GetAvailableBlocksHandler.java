package com.blockr.handlers.blockprogram.getavailableblocks;

import com.blockr.domain.Palette;
import com.blockr.domain.State;
import com.blockr.domain.block.interfaces.ReadOnlyBlock;
import com.blockr.handlers.HandlerBase;

import java.util.List;

public class GetAvailableBlocksHandler extends HandlerBase<GetAvailableBlocks, List<? extends ReadOnlyBlock>> {

    public GetAvailableBlocksHandler(State state) {
        super(state);
    }

    @Override
    public List<? extends ReadOnlyBlock> handle(GetAvailableBlocks getAvailableBlocks) {
        return Palette.getAvailableBlocks(getState().getBlockProgram());
    }
}
