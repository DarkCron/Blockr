package com.blockr.handlers.actions.reset;

import an.awesome.pipelinr.Voidy;
import com.blockr.State;
import com.blockr.handlers.HandlerBase;

public class DoResetHandler extends HandlerBase<DoReset, Voidy> {

    public DoResetHandler(State state) {
        super(state);
    }

    @Override
    public Voidy handle(DoReset doReset) {
        this.getState().getGameWorld().restoreSnapshot(this.getState().getResetWorldState());
        this.getState().getBlockProgram().reset();
        return new Voidy();
    }


}
