package com.blockr.handlers.blockprogram.connectfunctiondefinitionblock;

import an.awesome.pipelinr.Voidy;
import com.blockr.State;
import com.blockr.handlers.HandlerBase;

public class ConnectFunctionDefinitionBlockHandler extends HandlerBase<ConnectFunctionDefinitionBlock, Voidy> {

    public ConnectFunctionDefinitionBlockHandler(State state) {
        super(state);
    }

    @Override
    public Voidy handle(ConnectFunctionDefinitionBlock connectStatementBlock) {
        getState().getBlockProgram().connectFunctionDefinitionBlock(connectStatementBlock.getSocketBlock(), connectStatementBlock.getPlugBlock());
        return new Voidy();
    }
}
