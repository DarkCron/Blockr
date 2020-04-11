package com.blockr.handlers.ui.input;

import an.awesome.pipelinr.Voidy;
import com.blockr.State;
import com.blockr.handlers.HandlerBase;

public class SetPaletteSelectionHandler extends HandlerBase<SetPaletteSelection, Voidy> {
    public SetPaletteSelectionHandler(State state) {
        super(state);
    }

    @Override
    public Voidy handle(SetPaletteSelection paletteSelection) {
        getState().setPaletteSelection(paletteSelection.getPaletteSelection());
        return new Voidy();
    }
}
