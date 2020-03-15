package com.blockr.handlers.blockprogram.getavailableblocks;

import an.awesome.pipelinr.Command;
import com.blockr.domain.block.interfaces.ReadOnlyBlock;

import java.util.Collections;
import java.util.List;

public class GetAvailableBlocksHandler implements Command.Handler<GetAvailableBlocks, List<ReadOnlyBlock>> {

    @Override
    public List<ReadOnlyBlock> handle(GetAvailableBlocks getAvailableBlocks) {

        //TODO: finish this

        return Collections.emptyList();
    }
}
