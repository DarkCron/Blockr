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
        this.title = getName(source);
        this.titleComponent = new TextComponent(this.title, BlockData.FONT_SIZE, HorizontalAlign.Left, VerticalAlign.Top);
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
        graphics.setColor(Color.green);
        graphics.fillRect(0, 0, getWidth(source), getHeight(source));
    }

    private void drawCondition(Graphics graphics) {
        var c = graphics.getClipBounds();
        graphics.setColor(Color.pink);
        graphics.fillRect(0, 0, getWidth(source), getHeight(source));
    }

    private void drawNormalStatement(Graphics graphics) {
        var c = graphics.getClipBounds();
        graphics.setColor(Color.yellow);
        graphics.fillRect(0, 0, getWidth(source), getHeight(source));
    }

    public Block getSource(){return source;}

    public WindowPosition getUpperLeft(){ return upperLeft;}

    public static String getName(Block block){
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
