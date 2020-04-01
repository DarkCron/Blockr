package com.blockr.handlers.blockprogram.insertBlockInProgram;

import an.awesome.pipelinr.Command;
import com.blockr.domain.block.interfaces.Block;
import com.blockr.ui.components.programblocks.ProgramBlockInsertInfo;

public class InsertBlockInProgram implements Command<Block> {

    public ProgramBlockInsertInfo getProgramBlockInsertInfo() {
        return programBlockInsertInfo;
    }

    private final ProgramBlockInsertInfo programBlockInsertInfo;

    public InsertBlockInProgram(ProgramBlockInsertInfo programBlockInsertInfo) {
        this.programBlockInsertInfo = programBlockInsertInfo;
    }
}
