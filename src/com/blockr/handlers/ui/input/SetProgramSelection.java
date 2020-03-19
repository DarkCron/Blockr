package com.blockr.handlers.ui.input;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;
import com.blockr.ui.components.programblocks.ProgramBlockComponent;
import com.blockr.ui.components.programblocks.UIBlockComponent;
import com.ui.WindowPosition;

public class SetProgramSelection implements Command<Voidy> {
    private final ProgramAreaSelection programAreaSelection;

    public SetProgramSelection(WindowPosition selectionPosition, ProgramBlockComponent blockType) {
        if(selectionPosition == null || blockType == null){
            programAreaSelection = null;
        }else{
            programAreaSelection = new ProgramAreaSelection(selectionPosition,blockType);
        }
    }

    public ProgramAreaSelection getProgramAreaSelection() {
        return programAreaSelection;
    }
}
