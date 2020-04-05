package com.blockr.handlers.blockprogram.connectstatementblock;

import an.awesome.pipelinr.Voidy;
import com.blockr.domain.State;
import com.blockr.handlers.HandlerBase;

public class ConnectStatementBlockHandler extends HandlerBase<ConnectStatementBlock, Voidy> {

    public ConnectStatementBlockHandler(State state) {
        super(state);
    }

    @Override
    public Voidy handle(ConnectStatementBlock connectStatementBlock) {
        getState().getBlockProgram().connectStatementBlock(connectStatementBlock.getSocketBlock(), connectStatementBlock.getPlugBlock());
        return new Voidy();
    }
}
