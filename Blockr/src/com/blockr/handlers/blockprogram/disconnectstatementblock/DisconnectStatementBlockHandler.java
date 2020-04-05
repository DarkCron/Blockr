package com.blockr.handlers.blockprogram.disconnectstatementblock;

import an.awesome.pipelinr.Voidy;
import com.blockr.domain.State;
import com.blockr.handlers.HandlerBase;
import com.blockr.handlers.blockprogram.connectstatementblock.ConnectStatementBlock;

public class DisconnectStatementBlockHandler extends HandlerBase<DisconnectStatementBlock, Voidy> {

    public DisconnectStatementBlockHandler(State state) {
        super(state);
    }

    @Override
    public Voidy handle(DisconnectStatementBlock connectStatementBlock) {
        getState().getBlockProgram().disconnectStatementBlock(connectStatementBlock.getPlugBlock());
        return new Voidy();
    }
}
