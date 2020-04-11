package com.blockr.handlers.actions.canundo1;

import com.blockr.State;
import com.blockr.handlers.HandlerBase;

public class CanUndoHandler extends HandlerBase<CanUndo, Boolean> {

    public CanUndoHandler(State state) {
        super(state);
    }

    @Override
    public Boolean handle(CanUndo canUndo) {
        return getState().canUndo();
    }


}
