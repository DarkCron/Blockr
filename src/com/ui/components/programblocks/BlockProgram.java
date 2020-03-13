package com.ui.components.programblocks;

import com.blockr.domain.block.StatementBlock;
import com.blockr.domain.block.interfaces.Block;
import com.ui.Component;
import com.ui.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BlockProgram extends Component {


    private BlockInformation root;
    public BlockInformation getRoot(){return root;}

    private List<BlockInformation> blockInformationList;

    public BlockProgram(Block block, WindowPosition position){
        blockInformationList = new ArrayList<>();
        root = new BlockInformation(block,position);
        blockInformationList.add(root);
    }

    public void addBlock(Block block){
        blockInformationList.add(new BlockInformation(block, blockInformationList.get(blockInformationList.size()-1)));
    }

    @Override
    public void draw(Graphics graphics) {
        var w = WindowRegion.fromGraphics(graphics);

       for (BlockInformation info : blockInformationList){
           if(blockInformationList.indexOf(info) != 0){
               info.updatePos(blockInformationList.get(blockInformationList.indexOf(info)-1));
           }
           info.draw(graphics);
       }
    }

    public WindowRegion getRegion() {
        return new WindowRegion(
                root.getScreenPosition().getX(),
                root.getScreenPosition().getY(),
                getTotalWidth() + root.getScreenPosition().getX(),
                getTotalHeight() + root.getScreenPosition().getY());
    }

    private int getTotalHeight(){
        return blockInformationList.stream().mapToInt(BlockInformation::getHeight).sum();
    }

    private int getTotalWidth(){
        int furthestX = 0;
        for (BlockInformation info : blockInformationList){
            furthestX = Math.max(furthestX,info.getX2() - root.getScreenPosition().getX());
        }
        return furthestX;
    }

    public boolean contains(WindowPosition windowPosition) {
        //TODO: debug why, but offset y of +/-10 pixels gives more accurate results, may be because of the "divcomponent" padding
        WindowPosition pos = new WindowPosition(windowPosition.getX()-5,windowPosition.getY()-8);

        return blockInformationList.stream().anyMatch(info -> info.contains(pos));
    }

    public BlockInformation.BlockClickInfo clickedBlock(WindowPosition windowPosition) {
        //TODO: debug why, but offset y of +/-10 pixels gives more accurate results, may be because of the "divcomponent" padding
        windowPosition = new WindowPosition(windowPosition.getX()-5,windowPosition.getY()-8);
        for (BlockInformation bi : blockInformationList){
            var result = bi.getDeepestBlock(windowPosition);
            if(result != null){
                return result;
            }
        }
        return null;
    }

    public void move(WindowPosition windowPosition) {
        //TODO: debug why, but offset y of +/-10 pixels gives more accurate results, may be because of the "divcomponent" padding
        windowPosition = new WindowPosition(windowPosition.getX() -5,windowPosition.getY()-8);

        var n = ViewContext.screenSpaceToPercent(windowPosition);
        var diff = new WindowPercentPosition(n.getX() - root.getUpperLeft().getX(), n.getY() - root.getUpperLeft().getY());
        for (BlockInformation info: blockInformationList){
            info.move(diff);
        }
    }

    public boolean isRoot(BlockInformation b){
        return root == b;
    }

    public boolean isRoot(BlockInformation.BlockClickInfo blockClickInfo) {
        return root.getBlock() == blockClickInfo.getBlock();
    }

    public void createFromGraph(Block newRoot) {
        blockInformationList.clear();
        root = new BlockInformation(newRoot,root.getScreenPosition());
        blockInformationList.add(root);

        if(newRoot instanceof StatementBlock){
            Block next = ((StatementBlock) newRoot).getNext();
            while (next!=null){
                addBlock(next);
                next = ((StatementBlock) next).getNext();
            }
        }
    }
}
