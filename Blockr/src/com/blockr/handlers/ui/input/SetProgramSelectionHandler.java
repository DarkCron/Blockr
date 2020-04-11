package com.blockr.handlers.ui.input;

import an.awesome.pipelinr.Voidy;
import com.blockr.State;
import com.blockr.handlers.HandlerBase;

public class SetProgramSelectionHandler extends HandlerBase<SetProgramSelection, Voidy> {
    public SetProgramSelectionHandler(State state) {
        super(state);
    }

    @Override
    public Voidy handle(SetProgramSelection programSelection) {
        getState().setProgramAreaSelection(programSelection.getProgramAreaSelection());
        return new Voidy();
    }
}
