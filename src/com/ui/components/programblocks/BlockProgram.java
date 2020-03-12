package com.ui.components.programblocks;

import com.blockr.domain.block.*;
import com.blockr.domain.block.interfaces.Block;
import com.ui.Component;
import com.ui.*;
import com.ui.areas.ProgramArea;
import com.ui.components.textcomponent.HorizontalAlign;
import com.ui.components.textcomponent.TextComponent;
import com.ui.components.textcomponent.VerticalAlign;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BlockProgram extends Component {


    private BlockInformation root;
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
        return blockInformationList.stream().anyMatch(info -> info.contains(windowPosition));
    }

    public Block clickedBlock(WindowPosition windowPosition) {
        for (BlockInformation bi : blockInformationList){
            var result = bi.getDeepestBlock(windowPosition);
            if(result != null){
                BlockInformation.markedBlock = result;
                return result;
            }
        }
        return null;
    }

    public void move(WindowPosition windowPosition) {
        var n = ViewContext.screenSpaceToPercent(windowPosition);
        var diff = new WindowPercentPosition(n.getX() - root.getUpperLeft().getX(), n.getY() - root.getUpperLeft().getY());
        for (BlockInformation info: blockInformationList){
            info.move(diff);
        }
    }
}

class BlockInformation{
    protected static Block markedBlock = null;
    private Block block;
    private WindowPercentPosition upperLeft;
    public  WindowPercentPosition getUpperLeft() { return upperLeft; }
    private WindowPercentPosition drawPosition;
    public WindowPercentPosition getDrawPosition() {
        return drawPosition;
    }

    private int width = 0;
    private int height = 0;

    public BlockInformation(Block block, WindowPosition position) {
        this.block = block;
        upperLeft = ViewContext.screenSpaceToPercent(position);
        drawPosition = new WindowPercentPosition(upperLeft.getX() - ViewContext.screenSpaceToPercent(ProgramArea.programAreaContainerPos).getX(),
                upperLeft.getY() - ViewContext.screenSpaceToPercent(ProgramArea.programAreaContainerPos).getY());

        processBlock(block);
    }

    public BlockInformation(Block block, BlockInformation blockInformation) {
        var pos = blockInformation.getScreenPosition();
        var thisPos = new WindowPosition(pos.getX(),pos.getY() + blockInformation.getHeight());

        this.block = block;
        upperLeft = ViewContext.screenSpaceToPercent(thisPos);
        drawPosition = new WindowPercentPosition(blockInformation.getDrawPosition().getX(),blockInformation.getDrawPosition().getY() + ViewContext.heightToPercent(blockInformation.getHeight()));

        processBlock(block);
    }

    public void updatePos( BlockInformation previous){
        var pos = previous.getScreenPosition();
        var thisPos = new WindowPosition(pos.getX(),pos.getY() + previous.getHeight());
        upperLeft = ViewContext.screenSpaceToPercent(thisPos);
        drawPosition = new WindowPercentPosition(previous.getDrawPosition().getX(),previous.getDrawPosition().getY() + ViewContext.heightToPercent(previous.getHeight()));
    }

    private void processBlock(Block block) {
        width = measureWidth(block);
        height = measureHeight(block);
    }

    private int measureWidth(Block block) {
        if(block == null){
            return 0;
        }
        if(block instanceof ControlFlowBlock){
            int lengthXCondition = BlockData.BLOCK_WIDTH + measureWidth(((ControlFlowBlock) block).getCondition());
            int lengthXBody = BlockData.CONTROL_FLOW_INNER_START + measureWidth(((ControlFlowBlock) block).getBody());
            return Math.max(lengthXBody,lengthXCondition);
        }else if(block instanceof ConditionBlock){
            if(block instanceof NotBlock){
                return BlockData.CONDITION_BLOCK_WIDTH + measureWidth(((NotBlock) block).getCondition());
            }else{
                return BlockData.CONDITION_BLOCK_WIDTH;
            }
        }else if(block instanceof StatementBlock){
            return BlockData.BLOCK_WIDTH + measureWidth(((StatementBlock) block).getNext());
        }
        return 0;
    }

    private int measureHeight(Block block) {
        if(block == null){
            return 0;
        }
        if(block instanceof ControlFlowBlock){
            return BlockData.BLOCK_HEIGHT + measureHeight(((ControlFlowBlock) block).getBody()) + measureHeight(((ControlFlowBlock) block).getNext());
        }else if(block instanceof ConditionBlock){
            return BlockData.CONDITION_BLOCK_HEIGHT;
        }else if(block instanceof StatementBlock){
            return BlockData.BLOCK_HEIGHT + measureHeight(((StatementBlock) block).getNext());
        }
        return 0;
    }

    private int measureHeightOfBlock(Block block) {
        if(block == null){
            return 0;
        }
        if(block instanceof ControlFlowBlock){
            return BlockData.BLOCK_HEIGHT + measureHeight(((ControlFlowBlock) block).getBody());
        }else if(block instanceof ConditionBlock){
            return BlockData.CONDITION_BLOCK_HEIGHT;
        }else if(block instanceof StatementBlock){
            return BlockData.BLOCK_HEIGHT;
        }
        return 0;
    }

    public WindowRegion getWindowRegion(){
        return null;
    }

    /*Region of the entire program*/
    public WindowRegion getScreenRegion(){
        return new WindowRegion(
                ViewContext.percentToScreenSpace(upperLeft).getX(),
                ViewContext.percentToScreenSpace(upperLeft).getY(),
                ViewContext.percentToScreenSpace(upperLeft).getX() + getWidth(),
                ViewContext.percentToScreenSpace(upperLeft).getY() + getHeight());
    }

    public int getHeight() {
        return height;
    }
    public int getWidth() {
        return width;
    }

    public int getX2(){
        return ViewContext.percentToScreenSpace(upperLeft).getX() + width;
    }

    public WindowPosition getScreenPosition(){
        return ViewContext.percentToScreenSpace(upperLeft);
    }

    public boolean contains(WindowPosition position){
        return getScreenRegion().contains(position);
    }

    public Block getDeepestBlock(WindowPosition position){
        if(block instanceof ControlFlowBlock){
            ConditionBlock cb = ((ControlFlowBlock) block).getCondition();
            int x = BlockData.BLOCK_WIDTH + getScreenPosition().getX();
            int y = getScreenPosition().getY();
            while (cb != null){
                var r = simulateRegion(cb,x,y);
                if(r.contains(position)){
                    return cb;
                }
                if(cb instanceof NotBlock){
                    cb = ((NotBlock) cb).getCondition();
                }else{
                    break;
                }
                x += BlockData.CONDITION_BLOCK_WIDTH;
            }

            x = getScreenPosition().getX() + BlockData.CONTROL_FLOW_INNER_START;
            y = getScreenPosition().getY() + BlockData.CONDITION_BLOCK_HEIGHT;
            StatementBlock sb = ((ControlFlowBlock) block).getBody();
            while (sb != null){
                if(sb instanceof ControlFlowBlock){
                    var t = getDeepestBlockFlowControl(position,x,y, (ControlFlowBlock) sb);
                    if(t != null){
                        return t;
                    }
                }else if(sb instanceof StatementBlock){
                    var r = simulateRegion(sb,x,y);
                    if(r.contains(position)){
                        return sb;
                    }
                }
                y+=measureHeightOfBlock(sb);
                sb = sb.getNext();
            }

            x = getScreenPosition().getX();
            y = y + BlockData.BLOCK_HEIGHT - BlockData.CONDITION_BLOCK_HEIGHT;
            sb = ((ControlFlowBlock) block).getNext();
            while (sb != null){
                if(sb instanceof ControlFlowBlock){
                    var t = getDeepestBlockFlowControl(position,x,y, (ControlFlowBlock) sb);
                    if(t != null){
                        return t;
                    }
                }else if(sb instanceof StatementBlock){
                    var r = simulateRegion(sb,x,y);
                    if(r.contains(position)){
                        return sb;
                    }
                }
                y+=measureHeightOfBlock(sb);
                sb = sb.getNext();
            }
            return simulateRegion(block,getScreenPosition().getX(),getScreenPosition().getY()).contains(position) ? block : null;
        }else if(block instanceof StatementBlock){
            return simulateRegion(block,getScreenPosition().getX(),getScreenPosition().getY()).contains(position) ? block : null;
        }else if(block instanceof ConditionBlock){
            return simulateRegion(block,getScreenPosition().getX(),getScreenPosition().getY()).contains(position) ? block : null;
        }
        return null;
    }

    private Block getDeepestBlockFlowControl(WindowPosition position, int x, int y, ControlFlowBlock cfb) {
        ConditionBlock cb = cfb.getCondition();
        int og_x = x;
        int og_y = y;
        x = x + BlockData.BLOCK_WIDTH;
        y = y;
        while (cb != null){
            var r = simulateRegion(cb,x,y);
            if(r.contains(position)){
                return cb;
            }
            if(cb instanceof NotBlock){
                cb = ((NotBlock) cb).getCondition();
            }else{
                break;
            }
            x += BlockData.CONDITION_BLOCK_WIDTH;
        }

        x = og_x + BlockData.CONTROL_FLOW_INNER_START;
        y = y + BlockData.CONDITION_BLOCK_HEIGHT;
        StatementBlock sb = cfb.getBody();
        while (sb != null){
            if(sb instanceof ControlFlowBlock){
                var t = getDeepestBlockFlowControl(position,x,y, (ControlFlowBlock) sb);
                if(t != null){
                    return t;
                }
            }else if(sb instanceof StatementBlock){
                var r = simulateRegion(sb,x,y);
                if(r.contains(position)){
                    return sb;
                }
            }
            y+=measureHeightOfBlock(sb);
            sb = sb.getNext();
        }

        x = og_x;
        y = y + BlockData.BLOCK_HEIGHT - BlockData.CONDITION_BLOCK_HEIGHT;
        y = og_y + measureHeightOfBlock(cfb);
        sb = ((ControlFlowBlock) cfb).getNext();
        while (sb != null){
            if(sb instanceof ControlFlowBlock){
                var t = getDeepestBlockFlowControl(position,x,y, (ControlFlowBlock) sb);
                if(t != null){
                    return t;
                }
            }else if(sb instanceof StatementBlock){
                var r = simulateRegion(sb,x,y);
                if(r.contains(position)){
                    return sb;
                }
            }
            y+=measureHeightOfBlock(sb);
            sb = sb.getNext();
        }
        //return simulateRegion(cfb,getScreenPosition().getX(),getScreenPosition().getY()).contains(position) ? cfb : null;
        return simulateRegion(cfb,og_x,og_y).contains(position) ? cfb : null;
    }

    public WindowRegion simulateRegion(Block block, int startX, int startY){
        if(block instanceof ControlFlowBlock){
            return new WindowRegion(startX,startY, startX+measureWidth(block),startY + measureHeightOfBlock(block));
        }else if(block instanceof StatementBlock){
            return new WindowRegion(startX,startY,startX + BlockData.BLOCK_WIDTH, startY + BlockData.BLOCK_HEIGHT);
        }else if(block instanceof ConditionBlock){
            return new WindowRegion(startX,startY,startX + BlockData.CONDITION_BLOCK_WIDTH, startY + BlockData.CONDITION_BLOCK_HEIGHT);
        }
        return null;
    }

    public void move(WindowPercentPosition diff) {
        this.upperLeft = new WindowPercentPosition(upperLeft.getX() + diff.getX(), upperLeft.getY() + diff.getY());
        drawPosition = new WindowPercentPosition(upperLeft.getX() - ViewContext.widthToPercent(ProgramArea.programAreaContainerPos.getX()), upperLeft.getY() - ViewContext.heightToPercent(ProgramArea.programAreaContainerPos.getY()));
    }

    public WindowRegion getChildRegion(WindowRegion region, Block child, int currentX, int currentY){
        if(child instanceof ConditionBlock){
            return new WindowRegion(region.getMinX() + currentX,
                    region.getMinY() + currentY,
                    region.getMinX() + currentX + BlockData.CONDITION_BLOCK_WIDTH,
                    region.getMinY() + currentY + BlockData.CONDITION_BLOCK_HEIGHT);
        }else if(child instanceof ControlFlowBlock){
            return new WindowRegion(region.getMinX() + currentX,
                    region.getMinY() + currentY,
                    region.getMinX() + currentX + measureWidth(child),
                    region.getMinY() + currentY + measureHeight(child));
        }else if(child instanceof StatementBlock){
            return new WindowRegion(region.getMinX() + currentX,
                    region.getMinY() + currentY,
                    region.getMinX() + currentX + BlockData.BLOCK_WIDTH,
                    region.getMinY() + currentY + BlockData.BLOCK_HEIGHT);
        }
        return null;
    }

    public void draw(Graphics graphics) {
        var cw = new WindowRegion(
                ViewContext.percentToWidth(getDrawPosition().getX()),
                ViewContext.percentToHeight(getDrawPosition().getY()),
                ViewContext.percentToWidth(getDrawPosition().getX())+getWidth(),
                ViewContext.percentToHeight(getDrawPosition().getY()) + getHeight());
        graphics.setColor(Color.green);
        graphics.fillRect(cw.getMinX(),cw.getMinY(),cw.getWidth(),cw.getHeight());
        graphics.setColor(Color.BLUE);
        graphics.drawRect(cw.getMinX(),cw.getMinY(),cw.getWidth(),cw.getHeight());
        graphics.setColor(BlockData.FONT_COLOR);
        new TextComponent(getName(block),BlockData.FONT_SIZE, HorizontalAlign.Left, VerticalAlign.Top).draw(graphics.create(cw.getMinX(),cw.getMinY(),cw.getWidth(),cw.getHeight()));

        if(block instanceof ControlFlowBlock){
            Block b = ((ControlFlowBlock) block).getCondition();
            int currentX = BlockData.BLOCK_WIDTH;
            int currentY = 0;
            while (b != null){
                var childWindow = getChildRegion(cw,b,currentX,currentY);
                graphics.setColor(Color.PINK);
                graphics.fillRect(childWindow.getMinX(),childWindow.getMinY(),childWindow.getWidth(),childWindow.getHeight());
                graphics.setColor(Color.RED);
                graphics.drawRect(childWindow.getMinX(),childWindow.getMinY(),childWindow.getWidth(),childWindow.getHeight());
                graphics.setColor(BlockData.FONT_COLOR);
                new TextComponent(getName(b),BlockData.FONT_SIZE, HorizontalAlign.Left, VerticalAlign.Top).draw(graphics.create(childWindow.getMinX(),childWindow.getMinY(),childWindow.getWidth(),childWindow.getHeight()));
                if(b instanceof NotBlock){
                    b = ((NotBlock)b).getCondition();
                }else{
                    b = null;
                    graphics.setColor(BlockData.BG_COLOR);
                    graphics.fillRect(childWindow.getMaxX()+1 , childWindow.getMinY(),500,BlockData.CONDITION_BLOCK_HEIGHT);
                }
                currentX += BlockData.CONDITION_BLOCK_WIDTH;
            }
            StatementBlock sb = ((ControlFlowBlock) block).getBody();
            currentX = BlockData.CONTROL_FLOW_INNER_START;
            currentY = BlockData.CONDITION_BLOCK_HEIGHT;
            while (sb != null){
                if(sb instanceof ControlFlowBlock){
                    drawFlowControl(graphics,cw,sb,currentX,currentY);
                }else {
                    var childWindow = getChildRegion(cw, sb, currentX, currentY);
                    graphics.setColor(Color.GREEN);
                    graphics.fillRect(childWindow.getMinX(), childWindow.getMinY(), childWindow.getWidth(), childWindow.getHeight());
                    graphics.setColor(Color.RED);
                    graphics.drawRect(childWindow.getMinX(), childWindow.getMinY(), childWindow.getWidth(), childWindow.getHeight());
                    graphics.setColor(BlockData.FONT_COLOR);
                    new TextComponent(getName(sb),BlockData.FONT_SIZE, HorizontalAlign.Left, VerticalAlign.Top).draw(graphics.create(childWindow.getMinX(),childWindow.getMinY(),childWindow.getWidth(),childWindow.getHeight()));

                    //Cut off
                    graphics.setColor(BlockData.BG_COLOR);
                    graphics.fillRect(childWindow.getMaxX() +1, childWindow.getMinY(),500,childWindow.getMaxY());
                }
                currentY += measureHeightOfBlock(sb);
                sb = sb.getNext();
            }

            sb = ((ControlFlowBlock) block).getNext();
            currentY += BlockData.BLOCK_HEIGHT - BlockData.CONDITION_BLOCK_HEIGHT;
            currentX = 0;
            while (sb != null){
                if(sb instanceof ControlFlowBlock){
                    drawFlowControl(graphics,cw,sb,currentX,currentY);
                }else {
                    var childWindow = getChildRegion(cw, sb, currentX, currentY);
                    graphics.setColor(Color.GREEN);
                    if(sb == markedBlock){
                        graphics.setColor(Color.BLACK);
                    }
                    graphics.fillRect(childWindow.getMinX(), childWindow.getMinY(), childWindow.getWidth(), childWindow.getHeight());
                    graphics.setColor(Color.RED);
                    graphics.drawRect(childWindow.getMinX(), childWindow.getMinY(), childWindow.getWidth(), childWindow.getHeight());
                    graphics.setColor(BlockData.FONT_COLOR);
                    new TextComponent(getName(sb),BlockData.FONT_SIZE, HorizontalAlign.Left, VerticalAlign.Top).draw(graphics.create(childWindow.getMinX(),childWindow.getMinY(),childWindow.getWidth(),childWindow.getHeight()));

                    //Cut off
                    graphics.setColor(BlockData.BG_COLOR);
                    graphics.fillRect(childWindow.getMaxX() +1, childWindow.getMinY(),500,childWindow.getMaxY());
                }
                currentY += measureHeightOfBlock(sb);
                sb = sb.getNext();
            }
        }
    }

    public void drawFlowControl(Graphics graphics, WindowRegion cw,Block block, int x, int y){
        var childRegion = getChildRegion(cw,block,x,y);

        //graphics.setColor(BlockData.STANDARD_COLOR);
        graphics.setColor(Color.yellow);
        graphics.fillRect(childRegion.getMinX(),childRegion.getMinY(),childRegion.getWidth(),childRegion.getHeight());
        graphics.setColor(Color.BLUE);
        graphics.drawRect(childRegion.getMinX(),childRegion.getMinY(),childRegion.getWidth(),childRegion.getHeight());
        graphics.setColor(BlockData.FONT_COLOR);
        new TextComponent(getName(block),BlockData.FONT_SIZE, HorizontalAlign.Left, VerticalAlign.Top).draw(graphics.create(childRegion.getMinX(),childRegion.getMinY(),childRegion.getWidth(),childRegion.getHeight()));

        Block b = ((ControlFlowBlock) block).getCondition();
        int currentX = x + BlockData.BLOCK_WIDTH;
        int currentY = y;
        while (b != null){
            var childWindow = getChildRegion(cw,b,currentX,currentY);
            graphics.setColor(Color.PINK);
            graphics.fillRect(childWindow.getMinX(),childWindow.getMinY(),childWindow.getWidth(),childWindow.getHeight());
            graphics.setColor(Color.RED);
            graphics.drawRect(childWindow.getMinX(),childWindow.getMinY(),childWindow.getWidth(),childWindow.getHeight());
            graphics.setColor(BlockData.FONT_COLOR);
            new TextComponent(getName(b),BlockData.FONT_SIZE, HorizontalAlign.Left, VerticalAlign.Top).draw(graphics.create(childWindow.getMinX(),childWindow.getMinY(),childWindow.getWidth(),childWindow.getHeight()));
            if(b instanceof NotBlock){
                b = ((NotBlock)b).getCondition();
                if(b == null){
                    graphics.setColor(BlockData.BG_COLOR);
                    graphics.fillRect(childWindow.getMaxX() +1, childWindow.getMinY(),500,BlockData.CONDITION_BLOCK_HEIGHT);
                }
            }else{
                b = null;
                graphics.setColor(BlockData.BG_COLOR);
                graphics.fillRect(childWindow.getMaxX() +1, childWindow.getMinY(),500,BlockData.CONDITION_BLOCK_HEIGHT);
            }
            currentX += BlockData.CONDITION_BLOCK_WIDTH;
        }
        StatementBlock sb = ((ControlFlowBlock) block).getBody();
        currentX = x + BlockData.CONTROL_FLOW_INNER_START;
        currentY = y + BlockData.CONDITION_BLOCK_HEIGHT;
        while (sb != null){
            if(sb instanceof ControlFlowBlock){
                drawFlowControl(graphics,cw,sb,currentX,currentY);
            }else {
                var childWindow = getChildRegion(cw, sb, currentX, currentY);
                graphics.setColor(Color.GREEN);
                if(sb == markedBlock){
                    graphics.setColor(Color.BLACK);
                }
                graphics.fillRect(childWindow.getMinX(), childWindow.getMinY(), childWindow.getWidth(), childWindow.getHeight());
                graphics.setColor(Color.RED);
                graphics.drawRect(childWindow.getMinX(), childWindow.getMinY(), childWindow.getWidth(), childWindow.getHeight());
                graphics.setColor(BlockData.FONT_COLOR);
                new TextComponent(getName(sb),BlockData.FONT_SIZE, HorizontalAlign.Left, VerticalAlign.Top).draw(graphics.create(childWindow.getMinX(),childWindow.getMinY(),childWindow.getWidth(),childWindow.getHeight()));

                //Cut off
                graphics.setColor(BlockData.BG_COLOR);
                graphics.fillRect(childWindow.getMaxX() +1, childWindow.getMinY(),500,childWindow.getMaxY());
            }
            currentY += measureHeightOfBlock(sb);
            sb = sb.getNext();
        }

        sb = ((ControlFlowBlock) block).getNext();
        currentY += BlockData.BLOCK_HEIGHT - BlockData.CONDITION_BLOCK_HEIGHT;
        currentX = currentX - BlockData.CONTROL_FLOW_INNER_START;
        while (sb != null){
            if(sb instanceof ControlFlowBlock){
                drawFlowControl(graphics,cw,sb,currentX,currentY);
            }else {
                var childWindow = getChildRegion(cw, sb, currentX, currentY);
                graphics.setColor(Color.GREEN);
                graphics.fillRect(childWindow.getMinX(), childWindow.getMinY(), childWindow.getWidth(), childWindow.getHeight());
                graphics.setColor(Color.RED);
                graphics.drawRect(childWindow.getMinX(), childWindow.getMinY(), childWindow.getWidth(), childWindow.getHeight());

                graphics.setColor(BlockData.FONT_COLOR);
                new TextComponent(getName(sb),BlockData.FONT_SIZE, HorizontalAlign.Left, VerticalAlign.Top).draw(graphics.create(childWindow.getMinX(),childWindow.getMinY(),childWindow.getWidth(),childWindow.getHeight()));

                //Cut off
                graphics.setColor(BlockData.BG_COLOR);
                graphics.fillRect(childWindow.getMaxX() +1, childWindow.getMinY(),500,childWindow.getMaxY());
            }
            currentY += measureHeightOfBlock(sb);
            sb = sb.getNext();
        }

    }

    public String getName(Block block){
        if(block instanceof NotBlock){
            return "Not";
        }else if(block instanceof WallInFrontBlock){
            return "Wall in F.";
        }else if(block instanceof MoveForwardBlock){
            return "Move Forward";
        }else if(block instanceof TurnBlock){
            return "Turn "+(((TurnBlock) block).getDirection() == TurnBlock.Direction.LEFT ? "Left" : "Right");
        }else if(block instanceof IfBlock){
            return "If";
        }else if(block instanceof WhileBlock){
            return "While";
        }
        return "";
    }
}
