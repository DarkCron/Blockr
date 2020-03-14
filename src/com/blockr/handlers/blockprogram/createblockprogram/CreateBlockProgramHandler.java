package com.blockr.handlers.blockprogram.createblockprogram;

import an.awesome.pipelinr.Voidy;
import com.blockr.domain.State;
import com.blockr.handlers.HandlerBase;
import com.blockr.handlers.blockprogram.disconnectstatementblock.DisconnectStatementBlock;

public class CreateBlockProgramHandler extends HandlerBase<CreateBlockProgram,Voidy> {
    public CreateBlockProgramHandler(State state) {
        super(state);
    }

    @Override
    public Voidy handle(CreateBlockProgram createBlockProgram) {
        this.getState().createBlockProgram();
        return new Voidy();
    }
}
