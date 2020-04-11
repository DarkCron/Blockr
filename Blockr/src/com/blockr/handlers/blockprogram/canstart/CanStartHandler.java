package com.blockr.handlers.blockprogram.canstart;

import com.blockr.State;
import com.blockr.handlers.HandlerBase;

public class CanStartHandler extends HandlerBase<CanStart, Boolean> {


    public CanStartHandler(State state) {
        super(state);
    }

    @Override
    public Boolean handle(CanStart canStart) {
       return getState().getBlockProgram().canStart();
    }
}
