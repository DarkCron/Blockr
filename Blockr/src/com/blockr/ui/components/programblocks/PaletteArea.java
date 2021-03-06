package com.blockr.ui.components.programblocks;

import an.awesome.pipelinr.Pipeline;
import com.blockr.domain.block.*;
import com.blockr.handlers.actions.record.DoRecord;
import com.blockr.handlers.blockprogram.getblockprogram.GetBlockProgram;
import com.blockr.handlers.ui.input.GetProgramSelection;
import com.blockr.handlers.ui.input.resetuistate.ResetUIState;
import com.ui.Component;
import com.ui.Container;
import com.ui.WindowPosition;
import com.ui.WindowRegion;
import com.ui.mouseevent.MouseEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Palette Area Container, the visual component on screen.
 * Generates a list of possible blocks to use in game and displays then.
 * Handles Palette logic, selection and removal.
 */
public class PaletteArea extends Container {

    private static final List<PaletteBlockComponent> programBlockComponents = new ArrayList<>();
    private static final List<WindowPosition> regionPositions = new ArrayList<>();
    private static PaletteBlockComponent functionComponent;

    private final Pipeline mediator;

    private static void init(Pipeline mediator) {

        int spaceBetween = 30;
        int block_height = 40;

        var rootPos = new WindowPosition(50,50);
        programBlockComponents.add(new PaletteBlockComponent(new MoveForwardBlock(), mediator, rootPos));
        regionPositions.add(rootPos);

        rootPos = rootPos.plus(new WindowPosition(0,spaceBetween + block_height));
        var turnBlock = new TurnBlock();
        turnBlock.setDirection(TurnBlock.Direction.LEFT);
        programBlockComponents.add(new PaletteBlockComponent(turnBlock, mediator, rootPos));
        regionPositions.add(rootPos);

        rootPos = rootPos.plus(new WindowPosition(0,spaceBetween + block_height));
        turnBlock = new TurnBlock();
        turnBlock.setDirection(TurnBlock.Direction.RIGHT);
        programBlockComponents.add(new PaletteBlockComponent(turnBlock, mediator, rootPos));
        regionPositions.add(rootPos);

        rootPos = rootPos.plus(new WindowPosition(0,spaceBetween + block_height));
        programBlockComponents.add(new PaletteBlockComponent(new IfBlock(), mediator, rootPos));
        regionPositions.add(rootPos);

        rootPos = rootPos.plus(new WindowPosition(0,spaceBetween + block_height));
        programBlockComponents.add(new PaletteBlockComponent(new WhileBlock(), mediator, rootPos));
        regionPositions.add(rootPos);

        rootPos = rootPos.plus(new WindowPosition(0,spaceBetween/2 + block_height));
        programBlockComponents.add(new PaletteBlockComponent(new NotBlock(), mediator, rootPos));
        regionPositions.add(rootPos);

        rootPos = rootPos.plus(new WindowPosition(0,spaceBetween/2 + block_height));
        programBlockComponents.add(new PaletteBlockComponent(new WallInFrontBlock(), mediator, rootPos));
        regionPositions.add(rootPos);

        rootPos = rootPos.plus(new WindowPosition(0,spaceBetween + block_height));
        programBlockComponents.add(new PaletteBlockComponent(new FunctionDefinitionBlock(), mediator, rootPos));
        regionPositions.add(rootPos);
        functionComponent = programBlockComponents.get(programBlockComponents.size()-1);
    }

    public PaletteArea(Pipeline mediator) {
        this.mediator = mediator;
        init(mediator);
    }

    @Override
    public List<? extends Component> getChildren() {
        return programBlockComponents;
    }

    @Override
    public WindowRegion getChildRegion(WindowRegion region, Component child) {
        if(!(child instanceof PaletteBlockComponent)){
            return null;
        }
        int index = programBlockComponents.indexOf(child);

        if(index == -1){
            return null;
        }


        WindowPosition blockPosition = regionPositions.get(index);
        blockPosition = new WindowPosition(blockPosition.getX() + region.getMinX(), blockPosition.getY() + region.getMinY());
        WindowRegion childRegion = new WindowRegion(blockPosition.getX(), blockPosition.getY(),blockPosition.getX()+ UIBlockComponent.getWidth(((PaletteBlockComponent) child).getSource()), blockPosition.getY()+ UIBlockComponent.getHeight(((PaletteBlockComponent) child).getSource()));

        return new WindowRegion(childRegion.getMinX(),childRegion.getMinY(),Math.min(region.getMaxX(),childRegion.getMaxX()),Math.min(region.getMaxY(),childRegion.getMaxY()));
    }

    /**
     * Handles 1 particular important input event:
     * If a BlockProgram gets dragged over this Palette Area then we remove it from game and redraw the current state.
     * @param mouseEvent
     */
    @Override
    public void onMouseEvent(MouseEvent mouseEvent) {
        super.onMouseEvent(mouseEvent);
        switch (mouseEvent.getType()){
            case MOUSE_UP:
                var programSelection = mediator.send(new GetProgramSelection());
                if(programSelection != null){
                    programSelection.getBlockType().callForCleanUp();
                    getViewContext().repaint();
                }
                break;
            case MOUSE_DRAG:
                break;
            case MOUSE_DOWN:
                break;
        }

        switch (mouseEvent.getType()){
            case MOUSE_UP:
                mediator.send(new ResetUIState());
                break;
            case MOUSE_DRAG:
                break;
            case MOUSE_DOWN:
                break;
        }
    }

    @Override
    protected void draw(Graphics graphics) {
        if(ProgramArea.programHasFunction()){
            programBlockComponents.remove(functionComponent);
        }else if(!programBlockComponents.stream().anyMatch(pbc -> pbc.getSource() instanceof FunctionDefinitionBlock)){
            programBlockComponents.add(functionComponent);
        }
    }
}
