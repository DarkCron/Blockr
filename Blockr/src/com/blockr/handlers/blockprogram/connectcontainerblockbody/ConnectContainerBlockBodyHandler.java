package com.blockr.handlers.blockprogram.connectcontainerblockbody;

import an.awesome.pipelinr.Voidy;
import com.blockr.State;
import com.blockr.handlers.HandlerBase;

public class ConnectContainerBlockBodyHandler extends HandlerBase<ConnectContainerBlockBody, Voidy> {

    public ConnectContainerBlockBodyHandler(State state) {
        super(state);
    }

    @Override
    public Voidy handle(ConnectContainerBlockBody connectContainerBlockBody) {
        getState().getBlockProgram().connectContainerBlockBody(connectContainerBlockBody.getControlFlowBlock(), connectContainerBlockBody.getStatementBlock());
        return new Voidy();
    }
}
