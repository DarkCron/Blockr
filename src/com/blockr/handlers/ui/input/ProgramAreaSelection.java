package com.blockr.handlers.ui.input;

import com.blockr.ui.components.programblocks.ProgramBlockComponent;
import com.blockr.ui.components.programblocks.UIBlockComponent;
import com.ui.WindowPosition;

public class ProgramAreaSelection {
    private final WindowPosition selectionPosition;
    private final ProgramBlockComponent blockType;

    public ProgramAreaSelection(WindowPosition selectionPosition, ProgramBlockComponent blockType) {
        this.selectionPosition = selectionPosition;
        this.blockType = blockType;
    }

    public WindowPosition getSelectionPosition() {
        return selectionPosition;
    }

    public ProgramBlockComponent getBlockType() {
        return blockType;
    }
}
