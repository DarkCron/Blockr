package com.blockr.handlers.blockprogram.connectcontrolflowbody;

import an.awesome.pipelinr.Voidy;
import com.blockr.State;
import com.blockr.handlers.HandlerBase;

public class ConnectControlFlowBodyHandler extends HandlerBase<ConnectControlFlowBody, Voidy> {

    public ConnectControlFlowBodyHandler(State state) {
        super(state);
    }

    @Override
    public Voidy handle(ConnectControlFlowBody connectControlFlowBody) {
        getState().getBlockProgram().connectControlFlowBody(connectControlFlowBody.getControlFlowBlock(), connectControlFlowBody.getStatementBlock());
        return new Voidy();
    }
}
