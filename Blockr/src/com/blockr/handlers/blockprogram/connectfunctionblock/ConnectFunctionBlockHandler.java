package com.blockr.handlers.blockprogram.connectfunctionblock;

import an.awesome.pipelinr.Voidy;
import com.blockr.State;
import com.blockr.handlers.HandlerBase;

public class ConnectFunctionBlockHandler extends HandlerBase<ConnectFunctionBlock, Voidy> {

    public ConnectFunctionBlockHandler(State state) {
        super(state);
    }

    @Override
    public Voidy handle(ConnectFunctionBlock connectStatementBlock) {
        getState().getBlockProgram().connectFunctionBlock(connectStatementBlock.getSocketBlock(), connectStatementBlock.getPlugBlock());
        return new Voidy();
    }
}
