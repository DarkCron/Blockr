package com.blockr.ui;

import com.blockr.domain.block.*;
import com.blockr.domain.block.interfaces.Block;
import com.ui.Component;
import com.ui.WindowPosition;
import com.blockr.ui.components.programblocks.BlockInformation;
import com.blockr.ui.components.programblocks.BlockProgram;
import com.ui.mouseevent.MouseEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ProgramArea extends Component {

    public static ProgramArea programArea;
    public static WindowPosition programAreaContainerPos;

    List<BlockProgram> programs;

    private static BlockProgram selectedProgram;
    public static BlockProgram getSelectedProgram(){ return selectedProgram;}
    private static BlockInformation.BlockClickInfo blockClickInfo;
    public static BlockInformation.BlockClickInfo getBlockClickInfo(){ return blockClickInfo;}

    //Temp logic for mediator
    private static TempLogic blackBox = new TempLogic();

    public ProgramArea(){
        super();
        programs = new ArrayList<>();
        programArea = this;
    }

    public void remove(BlockProgram selectedProgram) {
        if(selectedProgram != null){
            programs.remove(selectedProgram);
        }
    }

    @Override
    public void onMouseEvent(MouseEvent mouseEvent) {
        super.onMouseEvent(mouseEvent);

        switch (mouseEvent.getType()){
            case MOUSE_DOWN:
                if(mouseOnProgram(mouseEvent)){
                    selectedProgram = getProgramFromMouse(mouseEvent);
                    blockClickInfo = selectedProgram.clickedBlock(mouseEvent.getWindowPosition());
                    break;
                }
                if(programs.size() == 0){
                    programs.add(new BlockProgram(new MoveForwardBlock(),mouseEvent.getWindowPosition()));
                    //programs.add(new BlockProgram(new WallInFrontBlock(),mouseEvent.getWindowPosition()));
                }else{
                    /*{
                        WhileBlock whileBlock = new WhileBlock();
                        NotBlock nb = new NotBlock();
                        nb.setCondition(new WallInFrontBlock());
                        whileBlock.setCondition(nb);
                        WhileBlock nestedWhile = new WhileBlock();
                        nestedWhile.setCondition(new NotBlock());
                        WhileBlock nestedNestedWhile = new WhileBlock();
                        nestedNestedWhile.setCondition(nb);
                        nestedNestedWhile.setBody(new MoveForwardBlock());
                        nestedNestedWhile.setNext(new MoveForwardBlock());
                        {
                            WhileBlock nestedNestedNestedWhile = new WhileBlock();
                            MoveForwardBlock mb = new MoveForwardBlock();
                            mb.setNext(new MoveForwardBlock());;
                            nestedNestedNestedWhile.setNext(mb);
                            nestedNestedNestedWhile.setBody(new MoveForwardBlock());
                            nestedNestedWhile.setNext(nestedNestedNestedWhile);
                        }
                        nestedWhile.setBody(nestedNestedWhile);
                        MoveForwardBlock mb2 = new MoveForwardBlock();
                        mb2.setNext(new MoveForwardBlock());
                        nestedWhile.setNext(mb2);
                        whileBlock.setNext(new WhileBlock());
                        //whileBlock.setBody(new MoveForwardBlock());
                        whileBlock.setBody(nestedWhile);

                        programs.get(0).addBlock(whileBlock);
                        //programs.get(0).addBlock(new MoveForwardBlock());
                    }*/
                }
                getViewContext().repaint();
                break;
            case MOUSE_UP:
                if(PaletteArea.getSelectedProgram() != null){
                    //Check if we're on a existing program
                    if(mouseOnProgram(mouseEvent)){
                        selectedProgram = getProgramFromMouse(mouseEvent);
                        blockClickInfo = selectedProgram.clickedBlock(mouseEvent.getWindowPosition());
                    }
                    //If we are on a existing program
                    if(selectedProgram != null && blockClickInfo != null){
                        Block copy = createCopy(PaletteArea.getSelectedProgram().getRoot().getBlock());
                        var newRoot = blackBox.addToBlockGraph(selectedProgram.getRoot().getBlock(), blockClickInfo.getBlock() ,copy,blockClickInfo.getClickLocation());
                        newRoot = selectedProgram.getRoot().getBlock();
                        if(newRoot != null){
                            if(newRoot instanceof StatementBlock){
                                var temp = ((StatementBlock) selectedProgram.getRoot().getBlock()).getPrevious();
                                while (temp != null){
                                    newRoot = temp;
                                    temp = temp.getPrevious();
                                }
                            }
                            selectedProgram.createFromGraph(newRoot);
                            getViewContext().repaint();
                            break;
                        }
                    }
                    //If we are on a blank spot, or the spot was not compatible
                    programs.add(new BlockProgram(createCopy(PaletteArea.getSelectedProgram().getRoot().getBlock()),mouseEvent.getWindowPosition()));
                    getViewContext().repaint();
                    break;
                }
                break;
            case MOUSE_DRAG:
                if(selectedProgram != null){
                    if(blockClickInfo != null && selectedProgram.isRoot(blockClickInfo)){
                        selectedProgram.move(mouseEvent.getWindowPosition());
                    }
                    getViewContext().repaint();
                }
        }

        switch (mouseEvent.getType()){
            case MOUSE_UP:
                reset();
                PaletteArea.reset();
                break;
        }
    }

    private Block createCopy(Block block) {
        if(block instanceof NotBlock){
            return new NotBlock();
        }else if(block instanceof WallInFrontBlock){
            return new WallInFrontBlock();
        }else if(block instanceof WhileBlock){
            return new WhileBlock();
        }else if(block instanceof IfBlock){
            return new IfBlock();
        }else if(block instanceof MoveForwardBlock){
            return new MoveForwardBlock();
        }else if(block instanceof TurnBlock){
            TurnBlock b = new TurnBlock();
            b.setDirection(((TurnBlock) block).getDirection());
            return b;
        }
        return null;
    }

    public static void reset(){
        selectedProgram = null;
        blockClickInfo = null;
    }

    private BlockProgram getProgramFromMouse(MouseEvent mouseEvent) {
        for (BlockProgram blockProgram : programs){
            if(blockProgram.contains(mouseEvent.getWindowPosition())){
                return blockProgram;
            }
        }
        return null;
    }

    private boolean mouseOnProgram(MouseEvent mouseEvent) {
        for (BlockProgram blockProgram : programs){
            if(blockProgram.contains(mouseEvent.getWindowPosition())){
                return true;
            }
        }
        return false;
    }

    @Override
    protected void draw(Graphics graphics) {
        //var vc = getViewContext();
        //WindowRegion w = WindowRegion.fromGraphics(graphics);
        for (BlockProgram bp : programs){
            bp.draw(graphics);
        }
    }
}
