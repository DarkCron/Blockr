package com.blockr.ui.components.programblocks;

import an.awesome.pipelinr.Pipeline;
import com.blockr.domain.block.ControlFlowBlock;
import com.blockr.domain.block.StatementBlock;
import com.blockr.domain.block.interfaces.Block;
import com.blockr.handlers.ui.input.SetPaletteSelection;
import com.ui.WindowPosition;
import com.ui.mouseevent.MouseEvent;

public class ProgramBlockComponent extends UIBlockComponent {

    public ProgramBlockComponent(Block source, Pipeline mediator, WindowPosition rootPosition) {
        super(source, mediator, rootPosition);
    }

    @Override
    public void onMouseEvent(MouseEvent mouseEvent) {
        super.onMouseEvent(mouseEvent);

        switch (mouseEvent.getType()){
            case MOUSE_UP:
                break;
            case MOUSE_DRAG:
                break;
            case MOUSE_DOWN:
                break;
        }

        switch (mouseEvent.getType()){
            case MOUSE_UP:
                break;
            case MOUSE_DRAG:
                break;
            case MOUSE_DOWN:
                break;
        }
    }

    private void handleMouseEventControlFlow(MouseEvent mouseEvent) {
    }
}
