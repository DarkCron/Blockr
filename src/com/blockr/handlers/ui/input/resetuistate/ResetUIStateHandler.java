package com.blockr.handlers.ui.input.resetuistate;

import an.awesome.pipelinr.Voidy;
import com.blockr.domain.State;
import com.blockr.handlers.HandlerBase;

public class ResetUIStateHandler extends HandlerBase<ResetUIState, Voidy> {
    public ResetUIStateHandler(State state) {
        super(state);
    }

    @Override
    public Voidy handle(ResetUIState paletteSelection) {
        getState().resetUIState();
        return new Voidy();
    }
}
