package com.blockr.ui.components.programblocks;

import an.awesome.pipelinr.Pipeline;
import com.blockr.domain.block.interfaces.Block;
import com.blockr.handlers.ui.input.SetPaletteSelection;
import com.blockr.handlers.ui.input.resetuistate.ResetUIState;
import com.ui.WindowPosition;
import com.ui.mouseevent.MouseEvent;

/**
 * Specialized UI component for drawing blocks in the 'Palette Area'
 * Handles particular input based on the selected Palette Block
 */
public class PaletteBlockComponent extends UIBlockComponent {

    public PaletteBlockComponent(Block source, Pipeline mediator, WindowPosition rootPosition) {
        super(source, mediator, rootPosition);
    }

    /**
     * Handles 1 particular input, if the user clicks a block from the palette the game
     * selects this block to add it to another block.
     * @param mouseEvent
     */
    @Override
    public void onMouseEvent(MouseEvent mouseEvent) {
        switch (mouseEvent.getType()){
            case MOUSE_UP:
                break;
            case MOUSE_DRAG:
                break;
            case MOUSE_DOWN:
                mediator.send(new SetPaletteSelection(mouseEvent.getWindowPosition(),this));
                break;
        }

        switch (mouseEvent.getType()){
            case MOUSE_UP:
                mediator.send(new ResetUIState());
                break;
            case MOUSE_DRAG:
                break;
            case MOUSE_DOWN:
                break;
        }
    }
}
