package com.blockr.handlers.ui.input;

import com.blockr.State;
import com.blockr.handlers.HandlerBase;

public class GetPaletteSelectionHandler extends HandlerBase<GetPaletteSelection, PaletteSelection> {
    public GetPaletteSelectionHandler(State state) {
        super(state);
    }

    @Override
    public PaletteSelection handle(GetPaletteSelection paletteSelection) {
        return getState().getPaletteSelection();
    }
}
