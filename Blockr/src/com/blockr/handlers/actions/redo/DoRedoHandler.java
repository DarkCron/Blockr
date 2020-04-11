package com.blockr.handlers.actions.redo;

import an.awesome.pipelinr.Voidy;
import com.blockr.State;
import com.blockr.handlers.HandlerBase;

public class DoRedoHandler extends HandlerBase<DoRedo, Voidy> {

    public DoRedoHandler(State state) {
        super(state);
    }

    @Override
    public Voidy handle(DoRedo doRedo) {
        getState().doRedo();
        return new Voidy();
    }


}
