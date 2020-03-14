package com.ui.components.programblocks;


import an.awesome.pipelinr.Pipeline;
import com.blockr.domain.block.*;
import com.blockr.domain.block.interfaces.Block;
import com.blockr.handlers.ui.GetUIInfo;
import com.blockr.handlers.ui.UIInfo;
import com.ui.WindowPercentPosition;
import com.ui.WindowPosition;
import com.ui.WindowRegion;
import com.ui.areas.PaletteArea;
import com.ui.areas.ProgramArea;
import com.ui.components.textcomponent.HorizontalAlign;
import com.ui.components.textcomponent.TextComponent;
import com.ui.components.textcomponent.VerticalAlign;

import java.awt.*;

public class BlockInformation{

    public static class BlockClickInfo{
        private final BlockClickLocation clickLocation;
        private final Block block;

        public BlockClickInfo(BlockClickLocation clickLocation, Block block){
            this.clickLocation = clickLocation;
            this.block = block;
        }

        public BlockClickLocation getClickLocation() {
            return clickLocation;
        }

        public Block getBlock() {
            return block;
        }
    }

    public enum BlockClickLocation {
        UPPER, LOWER, FLOW_CONDITION,FLOW_BODY_UPPER, CONDITION_LEFT, CONDITION_RIGHT, INVALID;
        public static BlockClickLocation getClickLocation(Block block, WindowRegion blockBody, WindowPosition mousePosition){
            BlockClickLocation result = null;
            if(block instanceof ControlFlowBlock){
                result = getClickLocationControlFlowBlock(block,blockBody,mousePosition);
            }else if(block instanceof StatementBlock){
                result = getClickLocationStatementBlock(block,blockBody,mousePosition);
            }else if(block instanceof ConditionBlock){
                result = getClickLocationConditionBlock(block,blockBody,mousePosition);
            }
            if(result != null){
                System.out.println(result);
            }
            return result;
        }

        private static BlockClickLocation getClickLocationControlFlowBlock(Block block, WindowRegion blockBody, WindowPosition mousePosition) {
            WindowRegion upperRegion = new WindowRegion(blockBody.getMinX(),blockBody.getMinY(),blockBody.getMinX() + BlockData.BLOCK_WIDTH - BlockData.CONDITION_BLOCK_HEIGHT/2,blockBody.getMinY() + BlockData.CONDITION_BLOCK_HEIGHT/2);
            if(upperRegion.contains(mousePosition)){
                return UPPER;
            }
            WindowRegion lowerRegion = new WindowRegion(blockBody.getMinX(),blockBody.getMinY() + (blockBody.getHeight() - (BlockData.BLOCK_HEIGHT - BlockData.CONDITION_BLOCK_HEIGHT)),blockBody.getMinX() + BlockData.BLOCK_WIDTH,blockBody.getMinY() + blockBody.getHeight());
            if(lowerRegion.contains(mousePosition)){
                return LOWER;
            }
            WindowRegion conditionRegion  = new WindowRegion(blockBody.getMinX()+ BlockData.BLOCK_WIDTH - BlockData.CONDITION_BLOCK_HEIGHT/2,blockBody.getMinY(),blockBody.getMinX() + BlockData.BLOCK_WIDTH,blockBody.getMinY() + BlockData.CONDITION_BLOCK_HEIGHT);
            if(conditionRegion.contains(mousePosition)){
                return FLOW_CONDITION;
            }
            WindowRegion bodyRegion  = new WindowRegion(blockBody.getMinX()+ BlockData.CONTROL_FLOW_INNER_START,blockBody.getMinY() + BlockData.CONDITION_BLOCK_HEIGHT/2,blockBody.getMinX() + BlockData.BLOCK_WIDTH - BlockData.CONDITION_BLOCK_HEIGHT/2,blockBody.getMinY() + BlockData.CONDITION_BLOCK_HEIGHT);
            if(bodyRegion.contains(mousePosition)){
                return FLOW_BODY_UPPER;
            }
            return INVALID;
        }

        private static BlockClickLocation getClickLocationConditionBlock(Block block, WindowRegion blockBody, WindowPosition mousePosition) {
            WindowRegion leftRegion = new WindowRegion(blockBody.getMinX(),blockBody.getMinY(),blockBody.getMinX() + blockBody.getWidth() /2 ,blockBody.getMaxY());
            WindowRegion rightRegion = new WindowRegion(blockBody.getMinX() + blockBody.getWidth() /2,blockBody.getMinY(),blockBody.getMinX() + blockBody.getWidth() ,blockBody.getMaxY());
            if(leftRegion.contains(mousePosition)){
                return CONDITION_LEFT;
            }else if(block instanceof NotBlock){
                if(rightRegion.contains(mousePosition)){
                    return CONDITION_RIGHT;
                }
            }
            return INVALID;
        }

        private static BlockClickLocation getClickLocationStatementBlock(Block block, WindowRegion blockBody, WindowPosition mousePosition) {
            WindowRegion upperRegion = new WindowRegion(blockBody.getMinX(),blockBody.getMinY(),blockBody.getMaxX(),blockBody.getMinY() + blockBody.getHeight()/2);
            if(upperRegion.contains(mousePosition)){
                return UPPER;
            }
            WindowRegion lowerRegion = new WindowRegion(blockBody.getMinX(),blockBody.getMinY() + blockBody.getHeight()/2,blockBody.getMaxX(),blockBody.getMaxY());
            if(lowerRegion.contains(mousePosition)){
                return LOWER;
            }
            return INVALID;
        }
    }

    private Block block;
    public  Block getBlock(){ return block; }
    private WindowPercentPosition upperLeft;
    public  WindowPercentPosition getUpperLeft() { return upperLeft; }
    private WindowPercentPosition drawPosition;
    public WindowPercentPosition getDrawPosition() {
        return drawPosition;
    }

    private int width = 0;
    private int height = 0;
    private boolean createdInPalette = false;

    final private Pipeline mediator;

    public BlockInformation(Block block, WindowPosition position, Pipeline mediator) {
        this.block = block;
        upperLeft = UIInfo.screenSpaceToPercent(position);
        this.mediator = mediator;

        if(PaletteArea.CREATE_PALETTE){
            createdInPalette = true;
            WindowPosition paletteAreaContainerPos = mediator.send(new GetUIInfo()).getPalettePosition();
            drawPosition = new WindowPercentPosition(upperLeft.getX() - UIInfo.screenSpaceToPercent(paletteAreaContainerPos).getX(),
                    upperLeft.getY() - UIInfo.screenSpaceToPercent(paletteAreaContainerPos).getY());
        }else{
            WindowPosition programAreaContainerPos = mediator.send(new GetUIInfo()).getGameAreaPosition();
            drawPosition = new WindowPercentPosition(upperLeft.getX() - UIInfo.screenSpaceToPercent(programAreaContainerPos).getX(),
                    upperLeft.getY() - UIInfo.screenSpaceToPercent(programAreaContainerPos).getY());
        }

        processBlock(block);
    }

    public BlockInformation(Block block, BlockInformation blockInformation, Pipeline mediator) {
        var pos = blockInformation.getScreenPosition();
        var thisPos = new WindowPosition(pos.getX(),pos.getY() + blockInformation.getHeight());
        this.mediator = mediator;

        this.block = block;
        upperLeft = UIInfo.screenSpaceToPercent(thisPos);
        drawPosition = new WindowPercentPosition(blockInformation.getDrawPosition().getX(),blockInformation.getDrawPosition().getY() + UIInfo.heightToPercent(blockInformation.getHeight()));

        processBlock(block);
    }

    public void updatePos( BlockInformation previous){
        var pos = previous.getScreenPosition();
        var thisPos = new WindowPosition(pos.getX(),pos.getY() + previous.getHeight());
        upperLeft = UIInfo.screenSpaceToPercent(thisPos);
        drawPosition = new WindowPercentPosition(previous.getDrawPosition().getX(),previous.getDrawPosition().getY() + UIInfo.heightToPercent(previous.getHeight()));
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
            return BlockData.BLOCK_WIDTH ;//+ measureWidth(((StatementBlock) block).getNext());
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
            return BlockData.BLOCK_HEIGHT;// + measureHeight(((StatementBlock) block).getNext());
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
                UIInfo.percentToScreenSpace(upperLeft).getX(),
                UIInfo.percentToScreenSpace(upperLeft).getY(),
                UIInfo.percentToScreenSpace(upperLeft).getX() + getWidth(),
                UIInfo.percentToScreenSpace(upperLeft).getY() + getHeight());
    }

    public int getHeight() {
        return height;
    }
    public int getWidth() {
        return width;
    }

    public int getX2(){
        return UIInfo.percentToScreenSpace(upperLeft).getX() + width;
    }

    public WindowPosition getScreenPosition(){
        return UIInfo.percentToScreenSpace(upperLeft);
    }

    public boolean contains(WindowPosition position){
        return getScreenRegion().contains(position);
    }

    public BlockClickInfo getDeepestBlock(WindowPosition position){
        if(block instanceof ControlFlowBlock){
            ConditionBlock cb = ((ControlFlowBlock) block).getCondition();
            int x = BlockData.BLOCK_WIDTH + getScreenPosition().getX();
            int y = getScreenPosition().getY();
            while (cb != null){
                var r = simulateRegion(cb,x,y);
                if(r.contains(position)){
                    return new BlockClickInfo(BlockClickLocation.getClickLocation(cb, r, position), cb);
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
                        return new BlockClickInfo(BlockClickLocation.getClickLocation(sb, r, position), sb);
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
                        return new BlockClickInfo(BlockClickLocation.getClickLocation(sb, r, position), sb);
                    }
                }
                y+=measureHeightOfBlock(sb);
                sb = sb.getNext();
            }
            return simulateRegion(block,getScreenPosition().getX(),getScreenPosition().getY()).contains(position) ?
                    new BlockClickInfo(BlockClickLocation.getClickLocation(block, simulateRegion(block, getScreenPosition().getX(), getScreenPosition().getY()), position), block) : null;
        }else if(block instanceof StatementBlock){
            return simulateRegion(block,getScreenPosition().getX(),getScreenPosition().getY()).contains(position) ?
                    new BlockClickInfo(BlockClickLocation.getClickLocation(block, simulateRegion(block, getScreenPosition().getX(), getScreenPosition().getY()), position), block) : null;
        }else if(block instanceof ConditionBlock){
            return simulateRegion(block,getScreenPosition().getX(),getScreenPosition().getY()).contains(position) ?
                    new BlockClickInfo(BlockClickLocation.getClickLocation(block, simulateRegion(block, getScreenPosition().getX(), getScreenPosition().getY()), position), block) : null;
        }
        return null;
    }

    private BlockClickInfo getDeepestBlockFlowControl(WindowPosition position, int x, int y, ControlFlowBlock cfb) {
        ConditionBlock cb = cfb.getCondition();
        int og_x = x;
        int og_y = y;
        x = x + BlockData.BLOCK_WIDTH;
        y = y;
        while (cb != null){
            var r = simulateRegion(cb,x,y);
            if(r.contains(position)){
                return new BlockClickInfo(BlockClickLocation.getClickLocation(cb, r, position), cb);
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
                    return new BlockClickInfo(BlockClickLocation.getClickLocation(sb, r, position), sb);
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
                    return new BlockClickInfo(BlockClickLocation.getClickLocation(sb, r, position), sb);
                }
            }
            y+=measureHeightOfBlock(sb);
            sb = sb.getNext();
        }
        //return simulateRegion(cfb,getScreenPosition().getX(),getScreenPosition().getY()).contains(position) ? cfb : null;
        return simulateRegion(cfb,og_x,og_y).contains(position) ?
                new BlockClickInfo(BlockClickLocation.getClickLocation(cfb, simulateRegion(cfb, og_x, og_y), position), cfb) : null;
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
        if(createdInPalette){
            WindowPosition paletteAreaContainerPos = mediator.send(new GetUIInfo()).getPalettePosition();
            drawPosition = new WindowPercentPosition(upperLeft.getX() - UIInfo.widthToPercent(paletteAreaContainerPos.getX()), upperLeft.getY() - UIInfo.heightToPercent(paletteAreaContainerPos.getY()));
        }else{
            WindowPosition programAreaContainerPos = mediator.send(new GetUIInfo()).getGameAreaPosition();
            drawPosition = new WindowPercentPosition(upperLeft.getX() - UIInfo.widthToPercent(programAreaContainerPos.getX()), upperLeft.getY() - UIInfo.heightToPercent(programAreaContainerPos.getY()));
        }
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
                UIInfo.percentToWidth(getDrawPosition().getX()),
                UIInfo.percentToHeight(getDrawPosition().getY()),
                UIInfo.percentToWidth(getDrawPosition().getX())+getWidth(),
                UIInfo.percentToHeight(getDrawPosition().getY()) + getHeight());
        graphics.setColor(Color.green);
        if(block instanceof ControlFlowBlock){
            graphics.setColor(Color.yellow);
        }else if(block instanceof ConditionBlock){
            graphics.setColor(Color.PINK);
        }
        graphics.fillRect(cw.getMinX(),cw.getMinY(),cw.getWidth(),cw.getHeight());
        graphics.setColor(Color.BLUE);
        graphics.drawRect(cw.getMinX(),cw.getMinY(),cw.getWidth(),cw.getHeight());
        graphics.setColor(BlockData.FONT_COLOR);
        new com.ui.components.textcomponent.TextComponent(getName(block),BlockData.FONT_SIZE, HorizontalAlign.Left, VerticalAlign.Top).draw(graphics.create(cw.getMinX(),cw.getMinY(),cw.getWidth(),cw.getHeight()));

        if(block instanceof ControlFlowBlock){
            Block b = ((ControlFlowBlock) block).getCondition();
            int currentX = BlockData.BLOCK_WIDTH;
            int currentY = 0;
            //Cutoff
            if(b == null){
                graphics.setColor(BlockData.BG_COLOR);
                graphics.fillRect(currentX, currentY,500,BlockData.CONDITION_BLOCK_HEIGHT);
            }
            while (b != null){
                var childWindow = getChildRegion(cw,b,currentX,currentY);
                graphics.setColor(Color.PINK);
                graphics.fillRect(childWindow.getMinX(),childWindow.getMinY(),childWindow.getWidth(),childWindow.getHeight());
                graphics.setColor(Color.RED);
                graphics.drawRect(childWindow.getMinX(),childWindow.getMinY(),childWindow.getWidth(),childWindow.getHeight());
                graphics.setColor(BlockData.FONT_COLOR);
                new com.ui.components.textcomponent.TextComponent(getName(b),BlockData.FONT_SIZE, HorizontalAlign.Left, VerticalAlign.Top).draw(graphics.create(childWindow.getMinX(),childWindow.getMinY(),childWindow.getWidth(),childWindow.getHeight()));
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
                    new com.ui.components.textcomponent.TextComponent(getName(sb),BlockData.FONT_SIZE, HorizontalAlign.Left, VerticalAlign.Top).draw(graphics.create(childWindow.getMinX(),childWindow.getMinY(),childWindow.getWidth(),childWindow.getHeight()));

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
                    graphics.fillRect(childWindow.getMinX(), childWindow.getMinY(), childWindow.getWidth(), childWindow.getHeight());
                    graphics.setColor(Color.RED);
                    graphics.drawRect(childWindow.getMinX(), childWindow.getMinY(), childWindow.getWidth(), childWindow.getHeight());
                    graphics.setColor(BlockData.FONT_COLOR);
                    new com.ui.components.textcomponent.TextComponent(getName(sb),BlockData.FONT_SIZE, HorizontalAlign.Left, VerticalAlign.Top).draw(graphics.create(childWindow.getMinX(),childWindow.getMinY(),childWindow.getWidth(),childWindow.getHeight()));

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
        new com.ui.components.textcomponent.TextComponent(getName(block),BlockData.FONT_SIZE, HorizontalAlign.Left, VerticalAlign.Top).draw(graphics.create(childRegion.getMinX(),childRegion.getMinY(),childRegion.getWidth(),childRegion.getHeight()));

        Block b = ((ControlFlowBlock) block).getCondition();
        int currentX = x + BlockData.BLOCK_WIDTH;
        int currentY = y;
        //Cutoff
        if(b == null){
            var temp = getChildRegion(cw,block,x,y);
            graphics.setColor(BlockData.BG_COLOR);
            graphics.fillRect(temp.getMinX() + BlockData.BLOCK_WIDTH, temp.getMinY(),500,BlockData.CONDITION_BLOCK_HEIGHT);
        }
        while (b != null){
            var childWindow = getChildRegion(cw,b,currentX,currentY);
            graphics.setColor(Color.PINK);
            graphics.fillRect(childWindow.getMinX(),childWindow.getMinY(),childWindow.getWidth(),childWindow.getHeight());
            graphics.setColor(Color.RED);
            graphics.drawRect(childWindow.getMinX(),childWindow.getMinY(),childWindow.getWidth(),childWindow.getHeight());
            graphics.setColor(BlockData.FONT_COLOR);
            new com.ui.components.textcomponent.TextComponent(getName(b),BlockData.FONT_SIZE, HorizontalAlign.Left, VerticalAlign.Top).draw(graphics.create(childWindow.getMinX(),childWindow.getMinY(),childWindow.getWidth(),childWindow.getHeight()));
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
                graphics.fillRect(childWindow.getMinX(), childWindow.getMinY(), childWindow.getWidth(), childWindow.getHeight());
                graphics.setColor(Color.RED);
                graphics.drawRect(childWindow.getMinX(), childWindow.getMinY(), childWindow.getWidth(), childWindow.getHeight());
                graphics.setColor(BlockData.FONT_COLOR);
                new com.ui.components.textcomponent.TextComponent(getName(sb),BlockData.FONT_SIZE, HorizontalAlign.Left, VerticalAlign.Top).draw(graphics.create(childWindow.getMinX(),childWindow.getMinY(),childWindow.getWidth(),childWindow.getHeight()));

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
