package com.blockr.handlers.ui.input.recordMousePos;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;
import com.blockr.ui.components.programblocks.UIBlockComponent;
import com.ui.WindowPosition;

public class SetRecordMouse implements Command<Voidy> {

    private final WindowPosition mousePosition;

    public SetRecordMouse(WindowPosition mousePosition) {
        this.mousePosition = mousePosition;
    }

    public WindowPosition getMousePosition() {
        return mousePosition;
    }
}
