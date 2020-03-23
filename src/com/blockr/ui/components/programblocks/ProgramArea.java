package com.blockr.ui.components.programblocks;

import an.awesome.pipelinr.Pipeline;
import com.blockr.domain.block.*;
import com.blockr.domain.block.interfaces.Block;
import com.blockr.domain.block.interfaces.ReadOnlyStatementBlock;
import com.blockr.handlers.blockprogram.addblock.AddBlock;
import com.blockr.handlers.blockprogram.getblockprogram.GetBlockProgram;
import com.blockr.handlers.blockprogram.removeblock.RemoveBlock;
import com.blockr.handlers.blockprogram.removeblock.RemoveBlockHandler;
import com.blockr.handlers.ui.input.GetPaletteSelection;
import com.blockr.handlers.ui.input.GetProgramSelection;
import com.blockr.handlers.ui.input.SetProgramSelection;
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
            programBlockComponents.add(new ProgramBlockComponent(root,mediator,rootPosition,this));
        }else if(root instanceof StatementBlock){
            programBlockComponents.add(new ProgramBlockComponent(root,mediator,rootPosition,this));
            buildProgramBlockComponentFromRoot(((StatementBlock)root).getNext(),rootPosition.plus(new WindowPosition(0,ProgramBlockComponent.getHeight(root))));
        }else if(root instanceof ConditionBlock){
            if(root instanceof WallInFrontBlock){
                programBlockComponents.add(new ProgramBlockComponent(root,mediator,rootPosition,this));
            }else if(root instanceof NotBlock){
                programBlockComponents.add(new ProgramBlockComponent(root,mediator,rootPosition,this));
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
                    var copy = BlockCreator.build(BlockCreator.BlockType.getType(paletteSelection.getBlockType().getSource()));

                    mediator.send(new AddBlock((StatementBlock)copy));
                   // var rootBlock = mediator.send(new GetRootBlock(copy));
                    var rootBlock = copy;

                    var recordedMouse = mediator.send(new GetMouseRecord());
                    if(recordedMouse == null){
                        recordedMouse = (new WindowPosition(50,50));
                    }else{
                        recordedMouse = (mouseEvent.getWindowPosition().minus(recordedMouse));
                    }
                    programBlockComponents.add(new ProgramBlockComponent(rootBlock,mediator, recordedMouse,this));
                    this.getViewContext().repaint();
                }
                break;
            case MOUSE_DRAG:
                var recordedMouse = mediator.send(new GetMouseRecord());
                if(recordedMouse == null){
                    mediator.send(new SetRecordMouse(new WindowPosition(mouseEvent.getWindowPosition().getX(),0)));
                }

                handleProgramMove(mouseEvent);
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

    public void handleProgramMove(MouseEvent mouseEvent){
        //TODO: break up in smaller functions
        var programSelection = mediator.send(new GetProgramSelection());
        if(programSelection != null){
            var recordedMouse = mediator.send(new GetMouseRecord());
            if(recordedMouse == null){
                recordedMouse = (new WindowPosition(50,50));
            }else{
                recordedMouse = (mouseEvent.getWindowPosition().minus(recordedMouse));
            }
            var bp = mediator.send(new GetBlockProgram());
            var selectionRoot = bp.getRootBlock(programSelection.getBlockType().getSource());
            bp.disconnectStatementBlock(selectionRoot, (ReadOnlyStatementBlock) programSelection.getBlockType().getSource());

            programBlockComponents.remove(programSelection.getBlockType());
            removeProgramBlockComponentsBaseOnRoot(programSelection.getBlockType().getSource());
            buildProgramBlockComponentFromRoot(programSelection.getBlockType().getSource(),recordedMouse);
            var temp = programBlockComponents.stream().filter(pbc -> pbc.getSource() == programSelection.getBlockType().getSource()).findFirst().orElse(null);
            mediator.send(new SetProgramSelection(recordedMouse,temp));
        }
    }

    @Override
    protected void draw(Graphics graphics) {
        //TODO: proper place for this
        graphics = getViewContext().getGraphicsDevice();
        var selection = mediator.send(new GetPaletteSelection());
        if(selection != null){
            selection.getBlockType().drawAt(graphics,getViewContext().getMousePosition());
        }
    }

    public void rebuild(Block newRoot) {
        //TODO: keep this for testing purposes for now
        if(newRoot == null || ((StatementBlock)newRoot).getPrevious() != null){
            System.out.println();
        }
        var programBlockComponent = programBlockComponents.stream().filter(pbc -> pbc.getSource() == newRoot).findFirst().orElse(null);
        var pos = programBlockComponent == null? null : programBlockComponent.getUpperLeft();
        var temp = ((StatementBlock) newRoot).getNext();
        while (pos == null && temp != null){
            programBlockComponent = programBlockComponents.stream().filter(pbc -> pbc.getSource() == temp).findFirst().orElse(null);
            pos = programBlockComponent == null? null : programBlockComponent.getUpperLeft();
        }
        removeProgramBlockComponentsBaseOnRoot(newRoot);
        buildProgramBlockComponentFromRoot(newRoot,pos);
        getViewContext().repaint();
    }

    public void cleanUp(ProgramBlockComponent programBlockComponent) {
        removeProgramBlockComponentsBaseOnRoot(programBlockComponent.getSource());
        mediator.send(new RemoveBlock((ReadOnlyStatementBlock) programBlockComponent.getSource()));
    }
}
