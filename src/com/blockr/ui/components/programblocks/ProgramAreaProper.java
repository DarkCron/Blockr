package com.blockr.ui.components.programblocks;

import an.awesome.pipelinr.Pipeline;
import com.blockr.domain.block.*;
import com.blockr.domain.block.interfaces.Block;
import com.blockr.domain.block.interfaces.ReadOnlyStatementBlock;
import com.blockr.handlers.blockprogram.addblock.AddBlock;
import com.blockr.handlers.blockprogram.getrootblock.GetRootBlock;
import com.blockr.handlers.ui.input.GetPaletteSelection;
import com.blockr.handlers.ui.input.SetPaletteSelection;
import com.blockr.handlers.ui.input.recordMousePos.GetMouseRecord;
import com.blockr.handlers.ui.input.recordMousePos.SetRecordMouse;
import com.blockr.ui.components.programblocks.ProgramBlockComponent;
import com.blockr.ui.components.programblocks.UIBlockComponent;
import com.ui.Component;
import com.ui.Container;
import com.ui.WindowPosition;
import com.ui.WindowRegion;
import com.ui.mouseevent.MouseEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ProgramAreaProper extends Container {

    private final List<ProgramBlockComponent> programBlockComponents = new ArrayList<>();
    //private static final List<WindowPosition> regionPositions = new ArrayList<>();

    private final Pipeline mediator;

    public ProgramAreaProper(Pipeline mediator) {
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
            programBlockComponents.remove(root);
            removeProgramBlockComponentsBaseOnRoot(((ControlFlowBlock) root).getCondition());
            removeProgramBlockComponentsBaseOnRoot(((ControlFlowBlock) root).getBody());
            removeProgramBlockComponentsBaseOnRoot(((ControlFlowBlock) root).getNext());
        }else if(root instanceof StatementBlock){
            programBlockComponents.remove(root);
            removeProgramBlockComponentsBaseOnRoot(((StatementBlock) root).getNext());
        }else if(root instanceof ConditionBlock){
            if(root instanceof WallInFrontBlock){
                programBlockComponents.remove(root);
            }else if(root instanceof NotBlock){
                programBlockComponents.remove(root);
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
                    var copy = (ReadOnlyStatementBlock) paletteSelection.getBlockType().getSource().getEmptyCopy();
                    mediator.send(new AddBlock(copy));
                    var rootBlock = mediator.send(new GetRootBlock(copy));

                    var recordedMouse = mediator.send(new GetMouseRecord());
                    if(recordedMouse == null){
                        recordedMouse = (new WindowPosition(50,50));
                    }else{
                        recordedMouse = (mouseEvent.getWindowPosition().minus(recordedMouse));
                    }
                    programBlockComponents.add(new ProgramBlockComponent(rootBlock,mediator, recordedMouse));
                    this.getViewContext().repaint();

                    mediator.send(new SetPaletteSelection(null,null));
                    mediator.send(new SetRecordMouse(null));
                }else{
                    MoveForwardBlock root = new MoveForwardBlock();
                    WhileBlock whileBlock = new WhileBlock();
                    whileBlock.setNext(new MoveForwardBlock());
                    whileBlock.setBody(new MoveForwardBlock());
                    IfBlock ifBlock = new IfBlock();
                    ifBlock.setBody(new MoveForwardBlock());
                    ifBlock.getBody().setNext(new TurnBlock());
                    ifBlock.setNext(new WhileBlock());
                    ((ControlFlowBlock)ifBlock.getNext()).setCondition(new NotBlock());
                    whileBlock.getBody().setNext(ifBlock);
                    NotBlock notBlock = new NotBlock();
                    notBlock.setCondition(new WallInFrontBlock());
                    ifBlock.setCondition(notBlock);
                    root.setNext(whileBlock);
                    buildProgramBlockComponentFromRoot(root, new WindowPosition(20,20));
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
                break;
            case MOUSE_DRAG:
                break;
            case MOUSE_DOWN:
                break;
        }
    }

    @Override
    protected void draw(Graphics graphics) {

    }
}
