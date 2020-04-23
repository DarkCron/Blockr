package com.blockr.ui.components.programblocks;

import an.awesome.pipelinr.Pipeline;
import com.blockr.domain.Palette;
import com.blockr.domain.block.BlockUtilities;
import com.blockr.domain.block.TurnBlock;
import com.blockr.domain.block.interfaces.Block;
import com.blockr.domain.block.interfaces.ReadOnlyBlock;
import com.blockr.domain.block.interfaces.ReadOnlyControlFlowBlock;
import com.blockr.domain.block.interfaces.ReadOnlyStatementBlock;
import com.blockr.domain.block.interfaces.markers.ReadOnlyConditionBlock;
import com.blockr.domain.block.interfaces.markers.ReadOnlyConditionedBlock;
import com.blockr.handlers.actions.record.DoRecord;
import com.blockr.handlers.actions.record.DoRecordHandler;
import com.blockr.handlers.blockprogram.addblock.AddBlock;
import com.blockr.handlers.blockprogram.connectCFandCondition.ConnectControlFlowAndCondition;
import com.blockr.handlers.blockprogram.connectconditionblock.ConnectConditionBlock;
import com.blockr.handlers.blockprogram.connectcontrolflowbody.ConnectControlFlowBody;
import com.blockr.handlers.blockprogram.connectcontrolflowbody.ConnectControlFlowBodyHandler;
import com.blockr.handlers.blockprogram.connectstatementblock.ConnectStatementBlock;
import com.blockr.handlers.blockprogram.getblockprogram.GetBlockProgram;
import com.blockr.handlers.ui.input.GetPaletteSelection;
import com.blockr.handlers.ui.input.SetProgramSelection;
import com.blockr.handlers.ui.input.recordMousePos.GetMouseRecord;
import com.blockr.handlers.ui.input.recordMousePos.SetRecordMouse;
import com.blockr.handlers.ui.input.resetuistate.ResetUIState;
import com.ui.WindowPosition;
import com.ui.mouseevent.MouseEvent;

/**
 * Specialized UI component for drawing blocks in the 'Game Area'
 * Handles particular input based on the selected Game Area Block
 */
public class ProgramBlockComponent extends UIBlockComponent {

    private final ProgramArea programArea;

    public ProgramBlockComponent(ReadOnlyBlock source, Pipeline mediator, WindowPosition rootPosition, ProgramArea parent) {
        super(source, mediator, rootPosition);
        programArea = parent;
    }

    @Override
    public void onMouseEvent(MouseEvent mouseEvent) {
        super.onMouseEvent(mouseEvent);

        switch (mouseEvent.getType()){
            case MOUSE_UP:
                var paletteSelection = mediator.send(new GetPaletteSelection());
                if(paletteSelection!=null) {
                    mediator.send(new DoRecord());
                    var bp = mediator.send(new GetBlockProgram());
                    var copy = Palette.createInstance((Block) paletteSelection.getBlockType().getSource());
                    if(paletteSelection.getBlockType().getSource() instanceof TurnBlock){
                        ((TurnBlock)copy).setDirection(((TurnBlock) paletteSelection.getBlockType().getSource()).getDirection());
                    }

                    var recordedMouse = mediator.send(new GetMouseRecord());
                    if(recordedMouse == null){
                        recordedMouse = (new WindowPosition(50,50));
                    }else{
                        recordedMouse = (mouseEvent.getWindowPosition().minus(recordedMouse));
                    }

                    var info = getSocketAndPlug(recordedMouse,copy);
                    if(info != null){
                        if(info.getPlug() == null){
                            mediator.send(new AddBlock(info.getSocket()));
                            programArea.updateBlockProgram(info.getSocket());
                        }else{
                            if(info.getPlugLocation() == ProgramBlockInsertInfo.PlugLocation.BODY){
                                mediator.send(new ConnectControlFlowBody((ReadOnlyControlFlowBlock) info.getSocket(),(ReadOnlyStatementBlock) info.getPlug()));
                            }else if(info.getPlug() instanceof ReadOnlyConditionBlock && info.getSocket() instanceof ReadOnlyConditionBlock){
                                mediator.send(new ConnectConditionBlock((ReadOnlyConditionedBlock) info.getSocket(), (ReadOnlyConditionBlock) info.getPlug()));
                            }else if(info.getSocket() instanceof ReadOnlyControlFlowBlock && info.getPlug() instanceof ReadOnlyConditionBlock){
                                mediator.send(new ConnectControlFlowAndCondition((ReadOnlyControlFlowBlock) info.getSocket(), (ReadOnlyConditionBlock) info.getPlug()));
                            }else{
                                mediator.send(new ConnectStatementBlock((ReadOnlyStatementBlock) info.getSocket(),(ReadOnlyStatementBlock) info.getPlug()));
                            }
                        }
                        programArea.updateBlockProgram(BlockUtilities.getRootFrom(info.getSocket(),mediator.send(new GetBlockProgram())));
                        //mediator.send(new DoRecord());
                    }
                }
                break;
            case MOUSE_DRAG:
                var recordedMouse = mediator.send(new GetMouseRecord());
                if(recordedMouse == null){
                    mediator.send(new SetRecordMouse(new WindowPosition(mouseEvent.getWindowPosition().getX(),0)));
                }

                programArea.handleProgramMove(mouseEvent);
                break;
            case MOUSE_DOWN:
                System.out.println(BlockData.getName(source));
                mediator.send(new DoRecord());
                mediator.send(new SetProgramSelection(mouseEvent.getWindowPosition(),this));
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

    /**
     * Removes a (part of) a BlockProgram based on the given component in the parent's game area
     */
    public void callForCleanUp() {
        programArea.cleanUp(this);
    }
}
