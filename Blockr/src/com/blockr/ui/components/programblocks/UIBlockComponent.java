package com.blockr.ui.components.programblocks;

import an.awesome.pipelinr.Pipeline;
import com.blockr.domain.block.*;
import com.blockr.domain.block.interfaces.*;
import com.blockr.domain.block.interfaces.markers.ReadOnlyConditionBlock;
import com.blockr.domain.block.FunctionBodyBlock;
import com.blockr.domain.block.interfaces.markers.ReadOnlyNotBlock;
import com.blockr.domain.block.interfaces.markers.ReadOnlyWallInFrontBlock;
import com.blockr.handlers.blockprogram.getblockprogram.GetBlockProgram;
import com.ui.Component;
import com.ui.WindowPosition;
import com.ui.WindowRegion;
import com.ui.components.textcomponent.HorizontalAlign;
import com.ui.components.textcomponent.TextComponent;
import com.ui.components.textcomponent.VerticalAlign;

import java.awt.*;

/**
 * Visual component to represent a Block on screen
 */
public abstract class UIBlockComponent extends Component {
    protected Block source;
    protected final Pipeline mediator;
    protected final WindowPosition upperLeft;
    private final String title;
    private final TextComponent titleComponent;

    private enum ClickLocations {PREVIOUS, NEXT, CFB_BODY, CFB_CONDITION, C_LEFT, C_RIGHT, INVALID}

    public UIBlockComponent(Block source, Pipeline mediator, WindowPosition rootPosition) {
        this.source = source;
        this.mediator = mediator;
        this.upperLeft = rootPosition;
        this.title = BlockData.getName(source);
        this.titleComponent = new TextComponent(this.title, BlockData.FONT_SIZE, HorizontalAlign.Center, VerticalAlign.Top);
    }

    //TODO: Write some unit tests
    public static int getHeight(Block source) {
        if(source == null){
            return 0;
        }
        if(source instanceof ReadOnlyControlFlowBlock){
            int bodyheight = 0;
            var body = ((ReadOnlyControlFlowBlock) source).getBody();
            while (body != null) {
                bodyheight += getHeight(body);
                body = body.getNext();
            }
            return BlockData.BLOCK_HEIGHT + bodyheight;
        }else if(source instanceof ReadOnlyStatementBlock){
            return BlockData.BLOCK_HEIGHT;
        }else if(source instanceof ReadOnlyConditionBlock){
            return BlockData.CONDITION_BLOCK_HEIGHT;
        }else if(source instanceof FunctionBodyBlock){
            int bodyheight = 0;
            var body = ((FunctionBodyBlock) source).getBody();
            while (body != null) {
                bodyheight += getHeight(body);
                body = body.getNext();
            }
            return BlockData.BLOCK_HEIGHT + bodyheight;
        }
        return BlockData.BLOCK_HEIGHT;
    }

    //TODO: Write some unit tests
    public static int getWidth(Block source) {
        if(source == null){
            return 0;
        }
        if(source instanceof ReadOnlyControlFlowBlock){
            return BlockData.BLOCK_WIDTH;
            //return Math.max(BlockData.BLOCK_WIDTH + getWidth(((ControlFlowBlock) source).getCondition()), BlockData.CONTROL_FLOW_INNER_START + getWidth(((ControlFlowBlock) source).getBody()));
        }else if(source instanceof ReadOnlyStatementBlock){
            return BlockData.BLOCK_WIDTH;
        }else if(source instanceof ReadOnlyConditionBlock){
            if(source instanceof ReadOnlyWallInFrontBlock){
                return BlockData.CONDITION_BLOCK_WIDTH;
            }else if(source instanceof ReadOnlyNotBlock){
                return BlockData.CONDITION_BLOCK_WIDTH;
            }
        }else if(source instanceof FunctionBodyBlock){
            return BlockData.BLOCK_WIDTH;
        }
        return BlockData.BLOCK_WIDTH;
    }

    public static int getTotalWidth(Block source) {
        if(source == null){
            return 0;
        }
        if(true)
            return BlockData.BLOCK_WIDTH*2;

        if(source instanceof ReadOnlyControlFlowBlock){
            return BlockData.BLOCK_WIDTH + getTotalWidth(((ReadOnlyControlFlowBlock) source).getBody());
            //return Math.max(BlockData.BLOCK_WIDTH + getWidth(((ControlFlowBlock) source).getCondition()), BlockData.CONTROL_FLOW_INNER_START + getWidth(((ControlFlowBlock) source).getBody()));
        }else if(source instanceof ReadOnlyStatementBlock){
            return BlockData.BLOCK_WIDTH;
        }else if(source instanceof ReadOnlyConditionBlock){
            if(source instanceof ReadOnlyWallInFrontBlock){
                return BlockData.CONDITION_BLOCK_WIDTH;
            }else if(source instanceof ReadOnlyNotBlock){
                return BlockData.CONDITION_BLOCK_WIDTH;
            }
        }else if(source instanceof FunctionBodyBlock){
            if(((FunctionBodyBlock) source).getBody() == null)
                return BlockData.BLOCK_WIDTH;
            return BlockData.BLOCK_WIDTH + getTotalWidth(((FunctionBodyBlock) source).getBody());
        }
        return BlockData.BLOCK_WIDTH;
    }

    /**
     * Generates and returns information based on what block is currently selected and where to connect it to.
     * @param mousePosition ScreenSpace location on where the user clicked.
     * @param blockToAdd Information on what Block the User currently has selected and may try to add to the current block.
     * @return information based on what block is currently selected and where to connect it to.
     */
    public ProgramBlockInsertInfo getSocketAndPlug(WindowPosition mousePosition, Block blockToAdd){
        //TODO FIX UP LEFT SOCKET OF WALLINFRONT
        if(blockToAdd == null || mousePosition == null){
            return null;
        }

        var clickLocation = getClickLocation(mousePosition,blockToAdd);

        switch (clickLocation){
            case INVALID:
                if(source instanceof ReadOnlyControlFlowBlock){
                    if (((ReadOnlyControlFlowBlock) source).getBody() != null){
                        var body = ((ReadOnlyControlFlowBlock) source).getBody();
                        while (body!=null){
                            for (var pbc: ProgramArea.programBlockComponents) {
                                if(pbc.getSource() == body){
                                    var result = pbc.getSocketAndPlug(mousePosition,blockToAdd);
                                    if(result != null){
                                        return result;
                                    }
                                }
                            }
                            body = body.getNext();
                        }

                    }
                }else if(source instanceof FunctionBodyBlock){
                    if (((FunctionBodyBlock) source).getBody() != null){
                        var body = ((ContainerBlock) source).getBody();
                        while (body!=null){
                            for (var pbc: ProgramArea.programBlockComponents) {
                                if(pbc.getSource() == body){
                                    var result = pbc.getSocketAndPlug(mousePosition,blockToAdd);
                                    if(result != null){
                                        return result;
                                    }
                                }
                            }
                            body = body.getNext();
                        }
                    }
                }
                return null;
            case NEXT:
                return new ProgramBlockInsertInfo(source,blockToAdd, ProgramBlockInsertInfo.PlugLocation.OTHER);
            case PREVIOUS:
                //Try to turn a previous in a next.
                if(((StatementBlock)source).getPrevious() == null){
                    return new ProgramBlockInsertInfo(blockToAdd, source, ProgramBlockInsertInfo.PlugLocation.OTHER);
                }else{
                    if(((StatementBlock) source).getPrevious() instanceof ContainerBlock){
                        if(((ContainerBlock) ((StatementBlock) source).getPrevious()).getBody() == (source)){
                            return new ProgramBlockInsertInfo(((StatementBlock) source).getPrevious(), blockToAdd, ProgramBlockInsertInfo.PlugLocation.BODY);
                        }
                    }
                    return new ProgramBlockInsertInfo(((StatementBlock) source).getPrevious(), blockToAdd, ProgramBlockInsertInfo.PlugLocation.OTHER);
                }
            case C_RIGHT:
                return new ProgramBlockInsertInfo(source,blockToAdd, ProgramBlockInsertInfo.PlugLocation.OTHER);
            case C_LEFT:
                return new ProgramBlockInsertInfo(blockToAdd, source, ProgramBlockInsertInfo.PlugLocation.OTHER);
            case CFB_BODY:
                return new ProgramBlockInsertInfo(source,blockToAdd, ProgramBlockInsertInfo.PlugLocation.BODY);
            case CFB_CONDITION:
                return new ProgramBlockInsertInfo(source,blockToAdd, ProgramBlockInsertInfo.PlugLocation.OTHER);
        }

        return null;
    }

    /**
     * Generates Click location information, to determine whether a certain click is to connect a block to the upper or
     * lower sockets for example.
     * @param mousePostion
     * @param blockToAdd
     * @return
     */
    protected ClickLocations getClickLocation(WindowPosition mousePostion, Block blockToAdd){
        var relativePosition = mousePostion.minus(upperLeft);
        relativePosition = relativePosition.minus(new WindowPosition(-4,7));

        if(source instanceof FunctionBodyBlock){
            if(blockToAdd instanceof ReadOnlyStatementBlock){
                var region = new WindowRegion(BlockData.CONTROL_FLOW_INNER_START,BlockData.CONDITION_BLOCK_HEIGHT/2,BlockData.BLOCK_WIDTH,BlockData.CONDITION_BLOCK_HEIGHT);
                if(region.contains(relativePosition)){
                    return ClickLocations.CFB_BODY;
                }
            }
        }else if(source instanceof ReadOnlyControlFlowBlock){
            if(blockToAdd instanceof ReadOnlyConditionBlock){
                var region = new WindowRegion(BlockData.BLOCK_WIDTH - BlockData.CONDITION_BLOCK_WIDTH, 0, BlockData.BLOCK_WIDTH,BlockData.CONDITION_BLOCK_HEIGHT);
                if(region.contains(relativePosition)){
                    //TODO: this if clause prevents certain problems with wallinfront
                    if(((ControlFlowBlock) source).getCondition() != null && blockToAdd instanceof ReadOnlyWallInFrontBlock){
                        return ClickLocations.INVALID;
                    }
                    return ClickLocations.CFB_CONDITION;
                }
            }else if(blockToAdd instanceof ReadOnlyStatementBlock){
                var region = new WindowRegion(BlockData.CONTROL_FLOW_INNER_START,BlockData.CONDITION_BLOCK_HEIGHT/2,BlockData.BLOCK_WIDTH,BlockData.CONDITION_BLOCK_HEIGHT);
                if(region.contains(relativePosition)){
                    return ClickLocations.CFB_BODY;
                }
                region = new WindowRegion(0,0,BlockData.BLOCK_WIDTH,BlockData.CONDITION_BLOCK_HEIGHT/2);
                if(region.contains(relativePosition)){
                    return ClickLocations.PREVIOUS;
                }
                region = new WindowRegion(0,BlockData.CONDITION_BLOCK_HEIGHT/2,BlockData.BLOCK_WIDTH,getHeight(source));
                if(region.contains(relativePosition)){
                    return ClickLocations.NEXT;
                }
            }
        }else if(source instanceof ReadOnlyStatementBlock){
            var region = new WindowRegion(0,0,BlockData.BLOCK_WIDTH,BlockData.BLOCK_HEIGHT/2);
            if(region.contains(relativePosition)){
                return ClickLocations.PREVIOUS;
            }
            region = new WindowRegion(0,BlockData.BLOCK_HEIGHT/2,BlockData.BLOCK_WIDTH,BlockData.BLOCK_HEIGHT);
            if(region.contains(relativePosition)){
                return ClickLocations.NEXT;
            }
        }else if(source instanceof ReadOnlyConditionBlock){
            if(source instanceof ReadOnlyWallInFrontBlock){
                var region = new WindowRegion(0,0,BlockData.CONDITION_BLOCK_WIDTH/2,BlockData.CONDITION_BLOCK_HEIGHT);
                if(blockToAdd instanceof ReadOnlyNotBlock && region.contains(relativePosition)){
                    return ClickLocations.C_LEFT;
                }else{
                    return ClickLocations.INVALID;
                }
            }else if(source instanceof ReadOnlyNotBlock){
                var region = new WindowRegion(0,0,BlockData.CONDITION_BLOCK_WIDTH/2,BlockData.CONDITION_BLOCK_HEIGHT);
                if(blockToAdd instanceof ReadOnlyNotBlock && region.contains(relativePosition)){
                    return ClickLocations.C_LEFT;
                }
                region = new WindowRegion(BlockData.CONDITION_BLOCK_WIDTH/2,0,BlockData.CONDITION_BLOCK_WIDTH,BlockData.CONDITION_BLOCK_HEIGHT);
                if(region.contains(relativePosition)){
                    return ClickLocations.C_RIGHT;
                }
            }
        }
        return ClickLocations.INVALID;
    }

    protected void reassignSource(Block Block){
        source = Block;
    }

    @Override
    protected void draw(Graphics graphics) {
        if(source instanceof FunctionBodyBlock){
            drawControlFlow(graphics);
            //drawControlFlow should work otherwise use drawFunction
        }else if(source instanceof ReadOnlyControlFlowBlock){
            drawControlFlow(graphics);
        }else if(source instanceof ReadOnlyStatementBlock){
            drawNormalStatement(graphics);
        }else if(source instanceof ReadOnlyConditionBlock){
            drawCondition(graphics);
        }

        drawTextComponent(graphics);
    }

    private void drawTextComponent(Graphics graphics) {
        //The code below fixes the text position since it's based on the Clip Region of Graphics
        var w = WindowRegion.fromGraphics(graphics);
        int width = source instanceof ReadOnlyConditionBlock ? BlockData.CONDITION_BLOCK_WIDTH : BlockData.BLOCK_WIDTH;
        {
            Polygon poly = new Polygon(new int[]{0,width,width,Math.min(w.getWidth(),width),Math.min(w.getWidth(),width),0},
                    new int[]{0,0,1,1,w.getHeight(),w.getHeight()}, 6);

            graphics.setClip(poly);
            graphics.setColor(BlockData.FONT_COLOR);
            this.titleComponent.draw(graphics);
        }
    }

    private void drawControlFlow(Graphics graphics) {
        var w = WindowRegion.fromGraphics(graphics);

        //Since I technically draw the CFB on 'TOP' of it's body I only draw the part that is supposed to be below it thanks to a custom poly shape.
        {
            int x0 = 0;
            int x1 = Math.min(BlockData.BLOCK_WIDTH,w.getWidth());
            int x2 = x1;
            int x3 = Math.min(BlockData.CONTROL_FLOW_INNER_START, w.getWidth());
            int x4 = x3;
            int x5 = x1;
            int x6 = x1;
            int x7 = x0;

            int y0 = 0;
            int y1 = y0;
            int y2 = Math.min(BlockData.CONDITION_BLOCK_HEIGHT, w.getHeight());
            int y3 = Math.min(BlockData.CONDITION_BLOCK_HEIGHT, w.getHeight());
            int y4 = Math.min(getHeight(source)-(BlockData.BLOCK_HEIGHT - BlockData.CONDITION_BLOCK_HEIGHT), w.getHeight());
            int y5 = y4;
            int y6 = Math.min(getHeight(source), w.getHeight());
            int y7 = y6;

            Polygon flowShape = new Polygon(new int[]{x0,x1,x2,x3,x4,x5,x6,x7}, new int[]{y0,y1,y2,y3,y4,y5,y6,y7},8);

            graphics.setClip(flowShape);
            graphics.setColor(Color.green);
            graphics.fillRect(0, 0, getWidth(source), getHeight(source));

            if(mediator.send(new GetBlockProgram()).getActive() == source){
                graphics.setColor(Color.PINK);
                graphics.fillRect(0, 0, getWidth(source), getHeight(source));
                graphics.setColor(Color.green);
                graphics.fillRect(BlockData.BLOCK_SELECTION_BORDER, BlockData.BLOCK_SELECTION_BORDER, getWidth(source) - 2*BlockData.BLOCK_SELECTION_BORDER, getHeight(source) - 2*BlockData.BLOCK_SELECTION_BORDER);
            }

            if(mediator.send(new GetBlockProgram()).getActive() instanceof FunctionDefinitionBlock){
                if(((FunctionDefinitionBlock) mediator.send(new GetBlockProgram()).getActive()).getCurrent() == source){
                    graphics.setColor(Color.PINK);
                    graphics.fillRect(0, 0, getWidth(source), getHeight(source));
                    graphics.setColor(Color.green);
                    graphics.fillRect(BlockData.BLOCK_SELECTION_BORDER, BlockData.BLOCK_SELECTION_BORDER, getWidth(source) - 2*BlockData.BLOCK_SELECTION_BORDER, getHeight(source) - 2*BlockData.BLOCK_SELECTION_BORDER);
                }
            }

            flowShape = new Polygon(new int[]{Math.max(0,x0),Math.max(0,x1-1),Math.max(0,x2-1),Math.max(0,x3-1),Math.max(0,x4-1),Math.max(0,x5-1),Math.max(0,x6-1),Math.max(0,x7)}
            , new int[]{Math.max(0,y0),Math.max(0,y1),Math.max(0,y2-1),Math.max(0,y3-1),Math.max(0,y4),Math.max(0,y5),Math.max(0,y6-1),Math.max(0,y7-1)},8);
            graphics.setColor(Color.black);
            graphics.drawPolygon(flowShape);
        }
    }

    private void drawCondition(Graphics graphics) {
        graphics.setColor(Color.pink);
        graphics.fillRect(0, 0, getWidth(source), getHeight(source));
        graphics.setColor(Color.black);
        graphics.drawRect(0, 0, getWidth(source)-1, getHeight(source)-1);
    }

    private void drawNormalStatement(Graphics graphics) {
        graphics.setColor(Color.yellow);
        graphics.fillRect(0, 0, getWidth(source), getHeight(source));

        if(mediator.send(new GetBlockProgram()).getActive() == source){
            graphics.setColor(Color.PINK);
            graphics.fillRect(0, 0, getWidth(source), getHeight(source));
            graphics.setColor(Color.yellow);
            graphics.fillRect(BlockData.BLOCK_SELECTION_BORDER, BlockData.BLOCK_SELECTION_BORDER, getWidth(source) - 2*BlockData.BLOCK_SELECTION_BORDER, getHeight(source) - 2*BlockData.BLOCK_SELECTION_BORDER);
        }

        if(mediator.send(new GetBlockProgram()).getActive() instanceof FunctionDefinitionBlock){
            if(((FunctionDefinitionBlock) mediator.send(new GetBlockProgram()).getActive()).getCurrent() == source
            || (((FunctionDefinitionBlock) mediator.send(new GetBlockProgram()).getActive()).getCurrent() == null
            && ((FunctionDefinitionBlock) mediator.send(new GetBlockProgram()).getActive()).getFunctionBody().getBody() == this.source) ){
                graphics.setColor(Color.PINK);
                graphics.fillRect(0, 0, getWidth(source), getHeight(source));
                graphics.setColor(Color.yellow);
                graphics.fillRect(BlockData.BLOCK_SELECTION_BORDER, BlockData.BLOCK_SELECTION_BORDER, getWidth(source) - 2*BlockData.BLOCK_SELECTION_BORDER, getHeight(source) - 2*BlockData.BLOCK_SELECTION_BORDER);
            }
        }

        graphics.setColor(Color.black);
        graphics.drawRect(0, 0, getWidth(source)-1, getHeight(source)-1);
    }

    /**
     * @return returns the abstract logical representation on which the visual component is based on
     */
    public Block getSource(){return source;}

    public WindowPosition getUpperLeft(){ return upperLeft;}

    public void drawAt(Graphics graphics, WindowPosition mousePosition) {
        WindowRegion windowRegion = new WindowRegion(mousePosition.getX(), mousePosition.getY(),mousePosition.getX() + getWidth(source),mousePosition.getY() +  getHeight(source));
        graphics = graphics.create(windowRegion.getMinX(),windowRegion.getMinY(), windowRegion.getWidth(),windowRegion.getHeight());
        draw(graphics);
    }
}
