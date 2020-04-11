package com.blockr.handlers.actions.canredo;

import com.blockr.State;
import com.blockr.handlers.HandlerBase;

public class CanRedoHandler extends HandlerBase<CanRedo, Boolean> {

    public CanRedoHandler(State state) {
        super(state);
    }

    @Override
    public Boolean handle(CanRedo canRedo) {
        return getState().canRedo();
    }


}
