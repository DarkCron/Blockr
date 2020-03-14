package com.blockr.ui;

import an.awesome.pipelinr.Pipeline;
import com.blockr.domain.block.*;
import com.blockr.domain.block.interfaces.Block;
import com.blockr.handlers.ui.GetUIInfo;
import com.ui.Component;
import com.ui.WindowPosition;
import com.blockr.ui.components.programblocks.BlockInformation;
import com.blockr.ui.components.programblocks.BlockProgram;
import com.ui.mouseevent.MouseEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PaletteArea extends Component {

    private static final int BLOCK_OFFSET_X = 50;
    private static final int BLOCK_OFFSET_Y = 50;

    private static BlockProgram selectedProgram;

    public static BlockProgram getSelectedProgram(){ return selectedProgram;}
    private static BlockInformation.BlockClickInfo blockClickInfo;
    public static BlockInformation.BlockClickInfo getBlockClickInfo(){ return blockClickInfo;}

    public static boolean CREATE_PALETTE = true;

    private final Pipeline mediator;

    private static final List<Block> possibleBlocks;
    static {
        possibleBlocks = new ArrayList<>();
        possibleBlocks.add(new MoveForwardBlock());
        TurnBlock left = new TurnBlock();
        left.setDirection(TurnBlock.Direction.LEFT);
        possibleBlocks.add(left);
        TurnBlock right = new TurnBlock();
        right.setDirection(TurnBlock.Direction.RIGHT);
        //right.setNext(new MoveForwardBlock());
        possibleBlocks.add(right);
        possibleBlocks.add(new WhileBlock());
        possibleBlocks.add(new IfBlock());
        possibleBlocks.add(new NotBlock());
        possibleBlocks.add(new WallInFrontBlock());
    }

    List<BlockProgram> programs;

    public PaletteArea(Pipeline mediator) {
        this.mediator = mediator;
    }


    public void init(){
        programs = new ArrayList<>();
        WindowPosition paletteAreaContainerPos = mediator.send(new GetUIInfo()).getPalettePosition();
        WindowPosition position = new WindowPosition(paletteAreaContainerPos.getX() + BLOCK_OFFSET_X,paletteAreaContainerPos.getY() + BLOCK_OFFSET_Y);
        for(Block b : possibleBlocks){
            programs.add(new BlockProgram(b,position, mediator));
            position = new WindowPosition(position.getX(),position.getY() + BLOCK_OFFSET_Y);
        }
        CREATE_PALETTE = false;
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
                break;
            case MOUSE_UP:
                //If true we've selected a program
                if(ProgramArea.getSelectedProgram() != null){
                    ProgramArea.programArea.remove(ProgramArea.getSelectedProgram());
                    getViewContext().repaint();
                }
                break;
            case MOUSE_DRAG:
                break;
        }

        switch (mouseEvent.getType()){
            case MOUSE_DOWN:
                break;
            case MOUSE_UP:
                reset();
                ProgramArea.reset();
                break;
            case MOUSE_DRAG:
                break;
        }
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
        if(CREATE_PALETTE){
            init();
        }

        for (BlockProgram bp : programs){
            bp.draw(graphics);
        }
    }
}
