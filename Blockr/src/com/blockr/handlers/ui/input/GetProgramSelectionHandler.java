package com.blockr.handlers.ui.input;

import com.blockr.State;
import com.blockr.handlers.HandlerBase;

public class GetProgramSelectionHandler extends HandlerBase<GetProgramSelection, ProgramAreaSelection> {
    public GetProgramSelectionHandler(State state) {
        super(state);
    }

    @Override
    public ProgramAreaSelection handle(GetProgramSelection programSelection) {
        return getState().getProgramAreaSelection();
    }
}
