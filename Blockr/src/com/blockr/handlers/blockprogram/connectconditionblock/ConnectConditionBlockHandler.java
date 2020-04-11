package com.blockr.handlers.blockprogram.connectconditionblock;

import an.awesome.pipelinr.Voidy;
import com.blockr.State;
import com.blockr.handlers.HandlerBase;


public class ConnectConditionBlockHandler extends HandlerBase<ConnectConditionBlock, Voidy> {

    public ConnectConditionBlockHandler(State state) {
        super(state);
    }

    @Override
    public Voidy handle(ConnectConditionBlock connectConditionBlock) {
        getState().getBlockProgram().connectConditionBlock(connectConditionBlock.getConditionedBlock(), connectConditionBlock.getConditionBlock());
        return new Voidy();
    }
}
