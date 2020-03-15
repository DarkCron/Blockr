package com.blockr.ui.components.programblocks;

import an.awesome.pipelinr.Pipeline;
import com.blockr.domain.block.ControlFlowBlock;
import com.blockr.domain.block.StatementBlock;
import com.blockr.domain.block.interfaces.Block;
import com.blockr.domain.block.interfaces.ReadOnlyStatementBlock;
import com.blockr.handlers.blockprogram.addblock.AddBlock;
import com.blockr.handlers.blockprogram.getrootblock.GetRootBlock;
import com.blockr.handlers.blockprogram.insertBlockInProgram.InsertBlockInProgram;
import com.blockr.handlers.ui.input.GetPaletteSelection;
import com.blockr.handlers.ui.input.SetPaletteSelection;
import com.blockr.handlers.ui.input.recordMousePos.GetMouseRecord;
import com.blockr.handlers.ui.input.recordMousePos.SetRecordMouse;
import com.blockr.handlers.ui.input.resetuistate.ResetUIState;
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
                var paletteSelection = mediator.send(new GetPaletteSelection());
                if(paletteSelection!=null) {
                    var copy = paletteSelection.getBlockType().getSource().getEmptyCopy();

                    var recordedMouse = mediator.send(new GetMouseRecord());
                    if(recordedMouse == null){
                        recordedMouse = (new WindowPosition(50,50));
                    }else{
                        recordedMouse = (mouseEvent.getWindowPosition().minus(recordedMouse));
                    }

                    var info = getSocketAndPlug(recordedMouse,copy);
                    if(info != null){
                        var newRoot = mediator.send(new InsertBlockInProgram(info));
                    }
                }
                break;
            case MOUSE_DRAG:
                var recordedMouse = mediator.send(new GetMouseRecord());
                if(recordedMouse == null){
                    mediator.send(new SetRecordMouse(new WindowPosition(mouseEvent.getWindowPosition().getX(),0)));
                }
                break;
            case MOUSE_DOWN:
                System.out.println(BlockData.getName(source));
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

    private void handleMouseEventControlFlow(MouseEvent mouseEvent) {
    }
}
