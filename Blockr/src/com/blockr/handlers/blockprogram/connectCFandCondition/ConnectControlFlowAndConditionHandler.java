package com.blockr.handlers.blockprogram.connectCFandCondition;

import an.awesome.pipelinr.Voidy;
import com.blockr.State;
import com.blockr.handlers.HandlerBase;

public class ConnectControlFlowAndConditionHandler extends HandlerBase<ConnectControlFlowAndCondition, Voidy> {

    public ConnectControlFlowAndConditionHandler(State state) {
        super(state);
    }

    @Override
    public Voidy handle(ConnectControlFlowAndCondition connectControlFlowBody) {
        getState().getBlockProgram().connectControlFlowBodyAndCondition(connectControlFlowBody.getControlFlowBlock(), connectControlFlowBody.getStatementBlock());
        return new Voidy();
    }
}
