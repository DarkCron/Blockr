package com.blockr.ui.components.programblocks;

import an.awesome.pipelinr.Pipeline;
import com.blockr.domain.block.*;
import com.blockr.domain.block.interfaces.Block;
import com.ui.Component;
import com.ui.WindowPosition;
import com.ui.components.textcomponent.HorizontalAlign;
import com.ui.components.textcomponent.TextComponent;
import com.ui.components.textcomponent.VerticalAlign;

import java.awt.*;

public abstract class UIBlockComponent extends Component {
    protected final Block source;
    protected final Pipeline mediator;
    protected final WindowPosition upperLeft;
    private final String title;
    private final TextComponent titleComponent;

    public UIBlockComponent(Block source, Pipeline mediator, WindowPosition rootPosition) {
        this.source = source;
        this.mediator = mediator;
        this.upperLeft = rootPosition;
        this.title = BlockData.getName(source);
        this.titleComponent = new TextComponent(this.title, BlockData.FONT_SIZE, HorizontalAlign.Center, VerticalAlign.Top);
    }

    public static int getHeight(Block source) {
        if(source == null){
            return 0;
        }
        if(source instanceof ControlFlowBlock){
            int bodyheight = 0;
            var body = ((ControlFlowBlock) source).getBody();
            while (body != null) {
                bodyheight += getHeight(body);
                body = body.getNext();
            }
            return BlockData.BLOCK_HEIGHT + bodyheight;
        }else if(source instanceof StatementBlock){
            return BlockData.BLOCK_HEIGHT;
        }else if(source instanceof ConditionBlock){
            return BlockData.CONDITION_BLOCK_HEIGHT;
        }
        return BlockData.BLOCK_HEIGHT;
    }

    public static int getWidth(Block source) {
        if(source == null){
            return 0;
        }
        if(source instanceof ControlFlowBlock){
            return Math.max(BlockData.BLOCK_WIDTH + getWidth(((ControlFlowBlock) source).getCondition()), BlockData.CONTROL_FLOW_INNER_START + getWidth(((ControlFlowBlock) source).getBody()));
        }else if(source instanceof StatementBlock){
            return BlockData.BLOCK_WIDTH;
        }else if(source instanceof ConditionBlock){
            if(source instanceof WallInFrontBlock){
                return BlockData.CONDITION_BLOCK_WIDTH;
            }else if(source instanceof NotBlock){
                return BlockData.CONDITION_BLOCK_WIDTH;
            }
        }
        return BlockData.BLOCK_WIDTH;
    }

    @Override
    protected void draw(Graphics graphics) {
        if(source instanceof ControlFlowBlock){
            drawControlFlow(graphics);
        }else if(source instanceof StatementBlock){
            drawNormalStatement(graphics);
        }else if(source instanceof ConditionBlock){
            drawCondition(graphics);
        }

        graphics.setColor(BlockData.FONT_COLOR);
        this.titleComponent.draw(graphics);
    }

    private void drawControlFlow(Graphics graphics) {
        var c = graphics.getClipBounds();
        Polygon flowShape = new Polygon(new int[]{ 0, BlockData.BLOCK_WIDTH, BlockData.BLOCK_WIDTH, BlockData.CONTROL_FLOW_INNER_START,BlockData.CONTROL_FLOW_INNER_START, BlockData.BLOCK_WIDTH, BlockData.BLOCK_WIDTH,0}
        , new int[]{0,0,BlockData.CONDITION_BLOCK_HEIGHT,BlockData.CONDITION_BLOCK_HEIGHT,getHeight(source)-(BlockData.BLOCK_HEIGHT - BlockData.CONDITION_BLOCK_HEIGHT),getHeight(source)-(BlockData.BLOCK_HEIGHT - BlockData.CONDITION_BLOCK_HEIGHT), getHeight(source), getHeight(source)},8);
        graphics.setClip(flowShape);
        graphics.setColor(Color.green);
        graphics.fillRect(0, 0, getWidth(source), getHeight(source));

        flowShape = new Polygon(new int[]{ 0, BlockData.BLOCK_WIDTH-1, BlockData.BLOCK_WIDTH-1, BlockData.CONTROL_FLOW_INNER_START,BlockData.CONTROL_FLOW_INNER_START, BlockData.BLOCK_WIDTH-1, BlockData.BLOCK_WIDTH-1,0}
                , new int[]{0,0,BlockData.CONDITION_BLOCK_HEIGHT,BlockData.CONDITION_BLOCK_HEIGHT,getHeight(source)-(BlockData.BLOCK_HEIGHT - BlockData.CONDITION_BLOCK_HEIGHT),getHeight(source)-(BlockData.BLOCK_HEIGHT - BlockData.CONDITION_BLOCK_HEIGHT), getHeight(source)-1, getHeight(source)-1},8);

        graphics.setColor(Color.black);
        graphics.drawPolygon(flowShape);
    }

    private void drawCondition(Graphics graphics) {
        var c = graphics.getClipBounds();
        graphics.setColor(Color.pink);
        graphics.fillRect(0, 0, getWidth(source), getHeight(source));
        graphics.setColor(Color.black);
        graphics.drawRect(0, 0, getWidth(source)-1, getHeight(source)-1);
    }

    private void drawNormalStatement(Graphics graphics) {
        var c = graphics.getClipBounds();
        graphics.setColor(Color.yellow);
        graphics.fillRect(0, 0, getWidth(source), getHeight(source));
        graphics.setColor(Color.black);
        graphics.drawRect(0, 0, getWidth(source)-1, getHeight(source)-1);
    }

    public Block getSource(){return source;}

    public WindowPosition getUpperLeft(){ return upperLeft;}
}
