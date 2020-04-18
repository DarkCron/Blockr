package com.blockr.handlers.actions.undo;

import an.awesome.pipelinr.Voidy;
import com.blockr.State;
import com.blockr.handlers.HandlerBase;

public class DoUndoHandler extends HandlerBase<DoUndo, Voidy> {

    public DoUndoHandler(State state) {
        super(state);
    }

    @Override
    public Voidy handle(DoUndo doRedo) {
        return new Voidy();
    }


}
