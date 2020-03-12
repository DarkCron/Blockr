package com.ui.areas;

import com.blockr.domain.block.MoveForwardBlock;
import com.blockr.domain.block.NotBlock;
import com.blockr.domain.block.WallInFrontBlock;
import com.blockr.domain.block.WhileBlock;
import com.blockr.domain.block.interfaces.Block;
import com.ui.*;
import com.ui.Component;
import com.ui.Container;
import com.ui.components.programblocks.BlockProgram;
import com.ui.mouseevent.MouseEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ProgramArea extends Component {

    public static WindowPosition programAreaContainerPos;

    List<BlockProgram> programs;

    private BlockProgram selectedProgram;

    public ProgramArea(){
        super();
        programs = new ArrayList<>();
    }

    @Override
    public void onMouseEvent(MouseEvent mouseEvent) {
        super.onMouseEvent(mouseEvent);

        switch (mouseEvent.getType()){
            case MOUSE_DOWN:
                if(mouseOnProgram(mouseEvent)){
                    selectedProgram = getProgramFromMouse(mouseEvent);
                    var temp = selectedProgram.clickedBlock(mouseEvent.getWindowPosition());
                    if(temp != null){
                        System.out.println(temp);
                        getViewContext().repaint();
                    }
                    break;
                }
                if(programs.size() == 0){
                    programs.add(new BlockProgram(new MoveForwardBlock(),mouseEvent.getWindowPosition()));
                    //programs.add(new BlockProgram(new WallInFrontBlock(),mouseEvent.getWindowPosition()));
                }else{
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
                    whileBlock.setNext(new MoveForwardBlock());
                    //whileBlock.setBody(new MoveForwardBlock());
                    whileBlock.setBody(nestedWhile);
                    programs.get(0).addBlock(whileBlock);
                    //programs.get(0).addBlock(new MoveForwardBlock());
                }
                getViewContext().repaint();
                break;
            case MOUSE_UP:
                selectedProgram = null;
                break;
            case MOUSE_DRAG:
                if(selectedProgram != null){
                    //selectedProgram.move(mouseEvent.getWindowPosition());
                    getViewContext().repaint();
                }
        }
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

/*    @Override
    public List<BlockProgram> getChildren() {
        return programs;
    }

    @Override
    public WindowRegion getChildRegion(WindowRegion region, Component child) {
        if(!(child instanceof BlockProgram)){
            return null;
        }
        return ((BlockProgram)child).getRegion();
    }*/

    @Override
    protected void draw(Graphics graphics) {
        //var vc = getViewContext();
        //WindowRegion w = WindowRegion.fromGraphics(graphics);
        for (BlockProgram bp : programs){
            bp.draw(graphics);
        }
    }
}
