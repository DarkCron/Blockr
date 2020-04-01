package com.blockr.handlers.ui.input;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;
import com.blockr.ui.components.programblocks.UIBlockComponent;
import com.ui.WindowPosition;

public class SetPaletteSelection implements Command<Voidy> {
    private final PaletteSelection paletteSelection;

    public SetPaletteSelection(WindowPosition selectionPosition, UIBlockComponent blockType) {
        if(selectionPosition == null || blockType == null){
            paletteSelection = null;
        }else{
            paletteSelection = new PaletteSelection(selectionPosition,blockType);
        }
    }

    public PaletteSelection getPaletteSelection() {
        return paletteSelection;
    }
}
