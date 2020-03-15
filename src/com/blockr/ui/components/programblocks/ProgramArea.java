package com.blockr.ui.components.programblocks;

import an.awesome.pipelinr.Pipeline;
import com.blockr.domain.block.*;
import com.blockr.domain.block.interfaces.Block;
import com.blockr.handlers.ui.input.GetPaletteSelection;
import com.blockr.handlers.ui.input.recordMousePos.GetMouseRecord;
import com.blockr.handlers.ui.input.recordMousePos.SetRecordMouse;
import com.blockr.handlers.ui.input.resetuistate.ResetUIState;
import com.ui.Component;
import com.ui.Container;
import com.ui.WindowPosition;
import com.ui.WindowRegion;
import com.ui.mouseevent.MouseEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ProgramArea extends Container {

    private final List<ProgramBlockComponent> programBlockComponents = new ArrayList<>();
    //private static final List<WindowPosition> regionPositions = new ArrayList<>();

    private final Pipeline mediator;

    public ProgramArea(Pipeline mediator) {
        this.mediator = mediator;
    }

    @Override
    public List<? extends Component> getChildren() {
        return programBlockComponents;
    }

    @Override
    public WindowRegion getChildRegion(WindowRegion region, Component child) {
        if(!(child instanceof ProgramBlockComponent)){
            return null;
        }
        int index = programBlockComponents.indexOf(child);

        if(index == -1){
            return null;
        }

        WindowPosition blockPosition = ((ProgramBlockComponent) child).getUpperLeft();
        blockPosition = new WindowPosition(blockPosition.getX() + region.getMinX(), blockPosition.getY() + region.getMinY());
        WindowRegion childRegion = new WindowRegion(blockPosition.getX(), blockPosition.getY(),blockPosition.getX()+ UIBlockComponent.getWidth(((ProgramBlockComponent) child).getSource()), blockPosition.getY()+ UIBlockComponent.getHeight(((ProgramBlockComponent) child).getSource()));

        return new WindowRegion(Math.max(region.getMinX(),childRegion.getMinX()),Math.max(region.getMinY(),childRegion.getMinY()),Math.min(region.getMaxX(),childRegion.getMaxX()),Math.min(region.getMaxY(),childRegion.getMaxY()));
    }

    private void removeProgramBlockComponentsBaseOnRoot(Block root){
        if(root == null){
            return;
        }
        if(root instanceof ControlFlowBlock){
            programBlockComponents.removeIf(pbc -> pbc.getSource() == root);
            removeProgramBlockComponentsBaseOnRoot(((ControlFlowBlock) root).getCondition());
            removeProgramBlockComponentsBaseOnRoot(((ControlFlowBlock) root).getBody());
            removeProgramBlockComponentsBaseOnRoot(((ControlFlowBlock) root).getNext());
        }else if(root instanceof StatementBlock){
            programBlockComponents.removeIf(pbc -> pbc.getSource() == root);
            removeProgramBlockComponentsBaseOnRoot(((StatementBlock) root).getNext());
        }else if(root instanceof ConditionBlock){
            if(root instanceof WallInFrontBlock){
                programBlockComponents.removeIf(pbc -> pbc.getSource() == root);
            }else if(root instanceof NotBlock){
                programBlockComponents.removeIf(pbc -> pbc.getSource() == root);
                removeProgramBlockComponentsBaseOnRoot(((NotBlock) root).getCondition());
            }
        }
    }

    private void buildProgramBlockComponentFromRoot(Block root, WindowPosition rootPosition){
        if(root == null){
            return;
        }
        if(root instanceof ControlFlowBlock){
            buildProgramBlockComponentFromRoot(((ControlFlowBlock) root).getBody(),rootPosition.plus(new WindowPosition(BlockData.CONTROL_FLOW_INNER_START,BlockData.CONDITION_BLOCK_HEIGHT)));
            buildProgramBlockComponentFromRoot(((ControlFlowBlock) root).getCondition(),rootPosition.plus(new WindowPosition(BlockData.BLOCK_WIDTH,0)));
            buildProgramBlockComponentFromRoot(((ControlFlowBlock) root).getNext(),rootPosition.plus(new WindowPosition(0,ProgramBlockComponent.getHeight(root))));
            programBlockComponents.add(new ProgramBlockComponent(root,mediator,rootPosition));
        }else if(root instanceof StatementBlock){
            programBlockComponents.add(new ProgramBlockComponent(root,mediator,rootPosition));
            buildProgramBlockComponentFromRoot(((StatementBlock)root).getNext(),rootPosition.plus(new WindowPosition(0,ProgramBlockComponent.getHeight(root))));
        }else if(root instanceof ConditionBlock){
            if(root instanceof WallInFrontBlock){
                programBlockComponents.add(new ProgramBlockComponent(root,mediator,rootPosition));
            }else if(root instanceof NotBlock){
                programBlockComponents.add(new ProgramBlockComponent(root,mediator,rootPosition));
                buildProgramBlockComponentFromRoot(((NotBlock) root).getCondition(),rootPosition.plus(new WindowPosition(BlockData.CONDITION_BLOCK_WIDTH,0)));
            }
        }
    }

    @Override
    public void onMouseEvent(MouseEvent mouseEvent) {
        switch (mouseEvent.getType()){
            case MOUSE_UP:
                var paletteSelection = mediator.send(new GetPaletteSelection());
                if(paletteSelection!=null){
                    var copy = paletteSelection.getBlockType().getSource().getEmptyCopy();
                    //mediator.send(new AddBlock(copy));
                   // var rootBlock = mediator.send(new GetRootBlock(copy));
                    var rootBlock = copy;

                    var recordedMouse = mediator.send(new GetMouseRecord());
                    if(recordedMouse == null){
                        recordedMouse = (new WindowPosition(50,50));
                    }else{
                        recordedMouse = (mouseEvent.getWindowPosition().minus(recordedMouse));
                    }
                    programBlockComponents.add(new ProgramBlockComponent(rootBlock,mediator, recordedMouse));
                    this.getViewContext().repaint();
                }
                break;
            case MOUSE_DRAG:
                var recordedMouse = mediator.send(new GetMouseRecord());
                if(recordedMouse == null){
                    mediator.send(new SetRecordMouse(new WindowPosition(mouseEvent.getWindowPosition().getX(),0)));
                }
                break;
            case MOUSE_DOWN:
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

    //TODO: Add unit tests
    public Block getRootFrom(Block b){
        if(b instanceof ControlFlowBlock){
            //If we can travel up, do so
            if(((ControlFlowBlock) b).getPrevious() != null){
                return getRootFrom(((ControlFlowBlock) b).getPrevious());
            }
            //If we can't travel up check if we're inside the body of another CFB
            for(ProgramBlockComponent pbc : programBlockComponents){
                if(pbc.getSource() instanceof ControlFlowBlock){
                    if(((ControlFlowBlock) pbc.getSource()).getBody() == b){
                        return getRootFrom(((ControlFlowBlock) pbc.getSource()).getPrevious());
                    }
                }
            }
            //If we can't travel up and this block isn't part of another CFB body, then this is root
            return b;
        }else if(b instanceof StatementBlock){
            if(((StatementBlock) b).getPrevious() != null){
                return getRootFrom(((StatementBlock) b).getPrevious());
            }else{
                return b;
            }
        }else if(b instanceof ConditionBlock){
            //First check if this condition is connected to a CFB, if so, then CFB is the start of root chain
            for(ProgramBlockComponent pbc : programBlockComponents){
                if(pbc.getSource() instanceof ControlFlowBlock){
                    if(((ControlFlowBlock) pbc.getSource()).getCondition() == b){
                        return getRootFrom(pbc.getSource());
                    }
                }
            }

            if(b instanceof WallInFrontBlock){
                //If the given block is a WallInFront block. search for a possible NOT connection, else this is the root
                for(ProgramBlockComponent pbc : programBlockComponents){
                    if(pbc.getSource() instanceof NotBlock){
                        if(((NotBlock) pbc.getSource()).getCondition() == b){
                            return getRootFrom(((NotBlock) pbc.getSource()).getCondition());
                        }
                    }
                }
                return b;
            }else if(b instanceof NotBlock){
                if(((NotBlock) b).getCondition() != null){
                    return getRootFrom(((NotBlock) b).getCondition());
                }
                return b;
            }
        }
        return null;
    }

    @Override
    protected void draw(Graphics graphics) {

    }
}
