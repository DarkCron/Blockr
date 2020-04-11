package com.blockr.handlers.blockprogram.disconnectconditionblock;

import an.awesome.pipelinr.Voidy;
import com.blockr.State;
import com.blockr.handlers.HandlerBase;

public class DisconnectConditionBlockHandler extends HandlerBase<DisconnectConditionBlock, Voidy> {

    public DisconnectConditionBlockHandler(State state) {
        super(state);
    }

    @Override
    public Voidy handle(DisconnectConditionBlock disconnectConditionBlock) {
        getState().getBlockProgram().disconnectConditionBlock(disconnectConditionBlock.getConditionBlock());
        return new Voidy();
    }
}
