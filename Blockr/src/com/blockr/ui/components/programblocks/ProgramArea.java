package com.blockr.ui.components.programblocks;

import an.awesome.pipelinr.Pipeline;
import com.blockr.domain.Palette;
import com.blockr.domain.block.*;
import com.blockr.domain.block.interfaces.*;
import com.blockr.domain.block.interfaces.markers.ReadOnlyConditionBlock;
import com.blockr.domain.block.interfaces.markers.ReadOnlyConditionedBlock;
import com.blockr.domain.block.interfaces.markers.ReadOnlyNotBlock;
import com.blockr.domain.block.interfaces.markers.ReadOnlyWallInFrontBlock;
import com.blockr.handlers.actions.record.DoRecord;
import com.blockr.handlers.actions.redo.DoRedo;
import com.blockr.handlers.actions.reset.DoReset;
import com.blockr.handlers.actions.undo.DoUndo;
import com.blockr.handlers.blockprogram.addblock.AddBlock;
import com.blockr.handlers.blockprogram.canstart.CanStart;
import com.blockr.handlers.blockprogram.disconnectconditionblock.DisconnectConditionBlock;
import com.blockr.handlers.blockprogram.disconnectstatementblock.DisconnectStatementBlock;
import com.blockr.handlers.blockprogram.executeprogram.ExecuteProgram;
import com.blockr.handlers.blockprogram.getblockprogram.GetBlockProgram;
import com.blockr.handlers.blockprogram.removeblock.RemoveBlock;
import com.blockr.handlers.ui.input.GetPaletteSelection;
import com.blockr.handlers.ui.input.GetProgramSelection;
import com.blockr.handlers.ui.input.SetProgramSelection;
import com.blockr.handlers.ui.input.recordMousePos.GetMouseRecord;
import com.blockr.handlers.ui.input.recordMousePos.SetRecordMouse;
import com.blockr.handlers.ui.input.resetuistate.ResetUIState;
import com.ui.Component;
import com.ui.Container;
import com.ui.*;
import com.ui.mouseevent.MouseEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The Program Area Container visual on screen.
 * Handles the draw and input logic for the BlockPrograms currently in-game.
 * Logic behind connecting Blocks is delegated to BlockProgram via the mediator.
 */
public class ProgramArea extends Container {

    protected static final List<ProgramBlockComponent> programBlockComponents = new ArrayList<>();
    //private static final List<WindowPosition> regionPositions = new ArrayList<>();

    private final Pipeline mediator;
    private static ProgramArea mainProgramArea;

    public ProgramArea(Pipeline mediator) {
        this.mediator = mediator;
        mainProgramArea = this;
    }

    public static void CarryOverCopy(Block block, Block copy) {
        for (var pbc: programBlockComponents) {
            if(pbc.getSource() == block){
                pbc.reassignSource(copy);
                break;
            }
        }

        var sel1 = mainProgramArea.mediator.send(new GetProgramSelection());
        if(sel1 != null){
            if(sel1.getBlockType().getSource() == block){
                sel1.getBlockType().reassignSource(copy);
            }
        }
    }

    @Override
    public List<? extends Component> getChildren() {
        return programBlockComponents;
    }

    @Override
    public WindowRegion getChildRegion(WindowRegion region, Component child) {
        if(!(child instanceof ProgramBlockComponent)){
            return null;
        }
        int index = programBlockComponents.indexOf(child);

        if(index == -1){
            return null;
        }

        WindowPosition blockPosition = ((ProgramBlockComponent) child).getUpperLeft();
        blockPosition = new WindowPosition(blockPosition.getX() + region.getMinX(), blockPosition.getY() + region.getMinY());
        WindowRegion childRegion = new WindowRegion(blockPosition.getX(), blockPosition.getY(),blockPosition.getX()+ UIBlockComponent.getWidth(((ProgramBlockComponent) child).getSource()), blockPosition.getY()+ UIBlockComponent.getHeight(((ProgramBlockComponent) child).getSource()));

        return new WindowRegion(Math.max(region.getMinX(),childRegion.getMinX()),Math.max(region.getMinY(),childRegion.getMinY()),Math.min(region.getMaxX(),childRegion.getMaxX()),Math.min(region.getMaxY(),childRegion.getMaxY()));
    }

    /**
     * Removes a particular BlockProgram based on a given root of a program. Effectively removing
     * a BlockProgram visually, logic delegated by the mediator.
     * @param root The 'root' of a BlockProgram is the first (highest visually) Statement Block of a BlockProgram.
     *             A Block is a root if root.getPrevious() == null
     */
    private void removeProgramBlockComponentsBaseOnRoot(Block root){
        if(root == null){
            return;
        }
        if(root instanceof ReadOnlyControlFlowBlock){
            programBlockComponents.removeIf(pbc -> pbc.getSource() == root);
            removeProgramBlockComponentsBaseOnRoot(((ReadOnlyControlFlowBlock) root).getCondition());
            removeProgramBlockComponentsBaseOnRoot(((ReadOnlyControlFlowBlock) root).getBody());
            removeProgramBlockComponentsBaseOnRoot(((ReadOnlyControlFlowBlock) root).getNext());
        }else if(root instanceof FunctionDefinitionBlock){
            programBlockComponents.removeIf(pbc -> pbc.getSource() == root);
            removeProgramBlockComponentsBaseOnRoot(((ReadOnlyStatementBlock) root).getNext());
            removeProgramBlockComponentsBaseOnRoot(((FunctionDefinitionBlock) root).getFunctionBody());
        }
        else if(root instanceof ReadOnlyStatementBlock){
            programBlockComponents.removeIf(pbc -> pbc.getSource() == root);
            removeProgramBlockComponentsBaseOnRoot(((ReadOnlyStatementBlock) root).getNext());
        }else if(root instanceof ReadOnlyConditionBlock){
            if(root instanceof ReadOnlyWallInFrontBlock){
                programBlockComponents.removeIf(pbc -> pbc.getSource() == root);
            }else if(root instanceof ReadOnlyNotBlock){
                programBlockComponents.removeIf(pbc -> pbc.getSource() == root);
                removeProgramBlockComponentsBaseOnRoot(((ReadOnlyNotBlock) root).getCondition());
            }
        }else if(root instanceof FunctionBodyBlock){
            programBlockComponents.removeIf(pbc -> pbc.getSource() == root);
            removeProgramBlockComponentsBaseOnRoot(((FunctionBodyBlock) root).getBody());
        }
    }

    private void removeProgramBlockComponentsBaseOnRoot(List<? extends Block> componentsToMove) {
        //TODO: not efficient but works
        for (Block b: componentsToMove) {
            removeProgramBlockComponentsBaseOnRoot(b);
        }
    }

    /**
     *  Adds a particular BlockProgram based on a given root of a program. Effectively adding a BlockProgram visually
     *  to the Game Area.
     * @param root The 'root' of a BlockProgram is the first (highest visually) Statement Block of a BlockProgram.
     *             A Block is a root if root.getPrevious() == null
     * @param rootPosition The Block of a BlockProgram are visually based on screen based on a position relative to the RootPosition, the
     *                     position of the 'root' Block. This position is effectively the Right Upper position of this 'root' block.
     */
    private void buildProgramBlockComponentFromRoot(Block root, WindowPosition rootPosition){
        if(root == null){
            return;
        }
        if(root instanceof ReadOnlyControlFlowBlock){
            buildProgramBlockComponentFromRoot(((ReadOnlyControlFlowBlock) root).getBody(),rootPosition.plus(new WindowPosition(BlockData.CONTROL_FLOW_INNER_START,BlockData.CONDITION_BLOCK_HEIGHT)));
            buildProgramBlockComponentFromRoot(((ReadOnlyControlFlowBlock) root).getCondition(),rootPosition.plus(new WindowPosition(BlockData.BLOCK_WIDTH,0)));
            buildProgramBlockComponentFromRoot(((ReadOnlyControlFlowBlock) root).getNext(),rootPosition.plus(new WindowPosition(0,ProgramBlockComponent.getHeight(root))));
            programBlockComponents.add(new ProgramBlockComponent(root,mediator,rootPosition,this));
        }else if(root instanceof FunctionDefinitionBlock){
            programBlockComponents.add(new ProgramBlockComponent(root,mediator,rootPosition,this));
            buildProgramBlockComponentFromRoot(((ReadOnlyStatementBlock)root).getNext(),rootPosition.plus(new WindowPosition(0,ProgramBlockComponent.getHeight(root))));
            buildProgramBlockComponentFromRoot(((FunctionDefinitionBlock) root).getFunctionBody(),rootPosition.minus(new WindowPosition(ProgramBlockComponent.getTotalWidth(((FunctionDefinitionBlock) root).getFunctionBody()),0)));
            //programBlockComponents.add(new ProgramBlockComponent(((FunctionDefinitionBlock) root).getFunctionBody(),mediator,rootPosition.minus(new WindowPosition(100,0)),this));
        }
        else if(root instanceof ReadOnlyStatementBlock){
            programBlockComponents.add(new ProgramBlockComponent(root,mediator,rootPosition,this));
            buildProgramBlockComponentFromRoot(((ReadOnlyStatementBlock)root).getNext(),rootPosition.plus(new WindowPosition(0,ProgramBlockComponent.getHeight(root))));
        }else if(root instanceof ReadOnlyConditionBlock){
            if(root instanceof ReadOnlyWallInFrontBlock){
                programBlockComponents.add(new ProgramBlockComponent(root,mediator,rootPosition,this));
            }else if(root instanceof ReadOnlyNotBlock){
                programBlockComponents.add(new ProgramBlockComponent(root,mediator,rootPosition,this));
                buildProgramBlockComponentFromRoot(((ReadOnlyNotBlock) root).getCondition(),rootPosition.plus(new WindowPosition(BlockData.CONDITION_BLOCK_WIDTH,0)));
            }
        }else if(root instanceof FunctionBodyBlock){
            buildProgramBlockComponentFromRoot(((FunctionBodyBlock) root).getBody(),rootPosition.plus(new WindowPosition(BlockData.CONTROL_FLOW_INNER_START,BlockData.CONDITION_BLOCK_HEIGHT)));
            programBlockComponents.add(new ProgramBlockComponent(root,mediator,rootPosition,this));
        }
    }

    @Override
    public void onMouseEvent(MouseEvent mouseEvent) {
        switch (mouseEvent.getType()){
            case MOUSE_UP:
                var paletteSelection = mediator.send(new GetPaletteSelection());
                if(paletteSelection!=null){
                    mediator.send(new DoRecord());

                    var copy = Palette.createInstance((Block) paletteSelection.getBlockType().getSource());
                    if(paletteSelection.getBlockType().getSource() instanceof TurnBlock){
                        ((TurnBlock)copy).setDirection(((TurnBlock) paletteSelection.getBlockType().getSource()).getDirection());
                    }

                    mediator.send(new AddBlock(copy));
                   // var rootBlock = mediator.send(new GetRootBlock(copy));

                    var rootBlock = copy;

                    var recordedMouse = mediator.send(new GetMouseRecord());
                    if(recordedMouse == null){
                        recordedMouse = (new WindowPosition(50,50));
                    }else{
                        recordedMouse = (mouseEvent.getWindowPosition().minus(recordedMouse));
                    }

                    programBlockComponents.add(new ProgramBlockComponent(rootBlock,mediator, recordedMouse,this));
                    buildProgramBlockComponentFromBP(mediator.send(new GetBlockProgram()));


                    this.getViewContext().repaint();
                }
                break;
            case MOUSE_DRAG:
                var recordedMouse = mediator.send(new GetMouseRecord());
                if(recordedMouse == null){
                    mediator.send(new SetRecordMouse(new WindowPosition(mouseEvent.getWindowPosition().getX(),0)));
                }

                handleProgramMove(mouseEvent);
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

    private void buildProgramBlockComponentFromBP(ReadOnlyBlockProgram bp) {
        var elements = new HashMap<Block, WindowPosition>();
        for (var c: bp.getComponents()) {
            if (programBlockComponents.stream().anyMatch(pbc -> pbc.getSource() == c)) {
                elements.put(c, programBlockComponents.stream().filter(pbc -> pbc.getSource() == c).findFirst().get().getUpperLeft());
            }

        }

        programBlockComponents.clear();
        for (var entry : elements.keySet()){
                buildProgramBlockComponentFromRoot(entry,elements.get(entry));
        }
    }

    /**
     * Handles the movement of a selected BlockProgram to a new position based on a new position contained in mouseEvent.
     * The ProgramArea does this by effectively removing the BlockProgram from its old position and adding it back into
     * the area based on the new position.
     * @param mouseEvent @see src/com/ui/mouseevent/MouseEvent
     */
    public void handleProgramMove(MouseEvent mouseEvent){
        //TODO: break up in smaller functions
        var programSelection = mediator.send(new GetProgramSelection());
        if(programSelection != null){

            var recordedMouse = mediator.send(new GetMouseRecord());
            if(recordedMouse == null){
                recordedMouse = (new WindowPosition(50,50));
            }else{
                recordedMouse = (mouseEvent.getWindowPosition().minus(recordedMouse));
            }
            recordedMouse = MyCanvasWindow.mouseInProgramAreaPosition;
            var bp = mediator.send(new GetBlockProgram());
            //var selectionRoot = bp.getRootBlock(programSelection.getBlockType().getSource());
            var componentsToMove = programSelection.getBlockType().getSource();
            var disconnectionRoot = BlockUtilities.getRootFrom(componentsToMove);
            var disconnectionRootPosition = programSelection.getBlockType().getUpperLeft();
            disconnectionRootPosition = positionFromBlock(disconnectionRoot) == null? disconnectionRootPosition : positionFromBlock(disconnectionRoot);

            if(disconnectionRoot == null){
                if(componentsToMove instanceof FunctionBodyBlock){
                    disconnectionRoot = componentsToMove;
                }
            }
            removeProgramBlockComponentsBaseOnRoot(disconnectionRoot);

            if(componentsToMove instanceof ReadOnlyStatementBlock){
                if(((ReadOnlyStatementBlock)componentsToMove).getPrevious() != null){
                    mediator.send(new DisconnectStatementBlock((ReadOnlyStatementBlock) programSelection.getBlockType().getSource()));
                    buildProgramBlockComponentFromRoot(disconnectionRoot,disconnectionRootPosition);
                }else{
                    if(programBlockComponents.stream().anyMatch(b -> b.getSource() instanceof FunctionBodyBlock)){
                        if(((FunctionBodyBlock)programBlockComponents.stream().filter(b -> b.getSource() instanceof FunctionBodyBlock).findFirst().get().getSource()).getBody() == componentsToMove){
                            mediator.send(new DisconnectStatementBlock((ReadOnlyStatementBlock) componentsToMove));
                        }
                    }
                }
            }else if(programSelection.getBlockType().getSource() instanceof ReadOnlyConditionedBlock){
                mediator.send(new DisconnectConditionBlock((ReadOnlyConditionBlock) programSelection.getBlockType().getSource()));
            }
            //bp.disconnectStatementBlock(componentsToMove, (ReadOnlyStatementBlock) programSelection.getBlockType().getSource());

            //programBlockComponents.remove(programSelection.getBlockType());
            //removeProgramBlockComponentsBaseOnRoot(programSelection.getBlockType().getSource());

            //buildProgramBlockComponentFromRoot(programSelection.getBlockType().getSource(),recordedMouse);
            buildProgramBlockComponentFromRoot(programSelection.getBlockType().getSource(),recordedMouse);
            //buildProgramBlockComponentFromBP(mediator.send(new GetBlockProgram()));

            var temp = programBlockComponents.stream().filter(pbc -> pbc.getSource() == programSelection.getBlockType().getSource()).findFirst().orElse(null);
            mediator.send(new SetProgramSelection(recordedMouse,temp));
        }
    }

    public void handleTryAttach(MouseEvent mouseEvent) {
        var programSelection = mediator.send(new GetProgramSelection());
        if(programSelection != null){
            var recordedMouse = MyCanvasWindow.mouseInProgramAreaPosition;

        }
    }

    private WindowPosition positionFromBlock(Block disconnectionRoot) {
        for (var pbc: programBlockComponents) {
            if(pbc.getSource() == disconnectionRoot){
                return pbc.getUpperLeft();
            }
        }
        return null;
    }

    @Override
    protected void draw(Graphics graphics) {
        //TODO: proper place for this
        graphics = getViewContext().getGraphicsDevice();
        var selection = mediator.send(new GetPaletteSelection());
        if(selection != null){
            selection.getBlockType().drawAt(graphics,getViewContext().getMousePosition());
        }
    }

    /**
     * Updates the visual BlockProgram after modifying the logic behind it. Effectively rebuilding and redrawing the
     * visual BlockProgram.
     * Works in the following steps:
     * 1) Find the UpperLeft position of the Block that was previously the root of the BlockProgram
     * 2) Remove the old BlockProgram
     * 3) Add the new BlockProgram based on the previous BlockProgram's position
     * 4) Redraw the visuals
     * @param newRoot The 'root' of the BlockProgram to update.
     */
    public void updateBlockProgram(Block newRoot) {
        //TODO: keep this for testing purposes for now
        if(!(newRoot instanceof FunctionBodyBlock) && (newRoot == null || ((ReadOnlyStatementBlock)newRoot).getPrevious() != null)){
            System.out.println();
        }
        var programBlockComponent = programBlockComponents.stream().filter(pbc -> pbc.getSource() == newRoot).findFirst().orElse(null);
        var pos = programBlockComponent == null? null : programBlockComponent.getUpperLeft();
        var temp = newRoot instanceof FunctionBodyBlock ? ((FunctionBodyBlock) newRoot).getBody() : ((StatementBlock) newRoot).getNext();
        while (pos == null && temp != null){
            programBlockComponent = programBlockComponents.stream().filter(pbc -> pbc.getSource() == temp).findFirst().orElse(null);
            pos = programBlockComponent == null? null : programBlockComponent.getUpperLeft();
        }
        removeProgramBlockComponentsBaseOnRoot(newRoot);
        buildProgramBlockComponentFromRoot(newRoot,pos);
        getViewContext().repaint();
    }

    /**
     * Removes a (part of) a BlockProgram based on the given component
     * @param programBlockComponent Remove all Block Components connected 'after' this one
     */
    public void cleanUp(ProgramBlockComponent programBlockComponent) {
        removeProgramBlockComponentsBaseOnRoot(programBlockComponent.getSource());
        try {
            mediator.send(new RemoveBlock((ReadOnlyStatementBlock) programBlockComponent.getSource()));
        }catch (Exception e){

        }
    }


    /**
     * Generates and returns a restorable snapshot of the Game Area
     *
     * @param rbp BlockProgram an abstract logic representation of the visual components
     *            currently in the Program Area
     * @return A generated snapshot of the Game Area components and locations
     */
    public static ProgramAreaState generateProgramAreaState(BlockProgram rbp){
        return new ProgramAreaState(mainProgramArea, rbp);
    }

    /**
     * Restores the Game Area to a previous snapshot
     * @param state A snapshot of the current layout and locations of the Program Area
     */
    public static void restoreProgramAreaState(ProgramAreaState state) {
        while (programBlockComponents.size() > 0){
            mainProgramArea.cleanUp(programBlockComponents.get(0));
        }
        for (var rootAndLocation: state.getRootLocations()) {
            //mainProgramArea.removeProgramBlockComponentsBaseOnRoot(rootAndLocation.getFirst());
            mainProgramArea.buildProgramBlockComponentFromRoot(rootAndLocation.getFirst(),rootAndLocation.getSecond());
        }

        mainProgramArea.getViewContext().repaint();
    }


    /**
     * Returns the upper left position of the visual component of a certain block if it's present in the
     * Game Area.
     * @param rob a Block that is or is not pressent in the game Area
     * @return returns the upper left location of the given block if it's in the Game Area,
     *          Returns null if the block isn't available.
     */
    public WindowPosition locationOf(Block rob) {
        for (ProgramBlockComponent pbc: programBlockComponents) {
            if(pbc.getSource() == rob){
                return pbc.getUpperLeft();
            }
        }

        return null;
    }

    @Override
    public void onKeyEvent(KeyPressUtility keyPressUtility) {
        if(keyPressUtility.hasPressedUndo()){
            mediator.send(new DoUndo());
            keyPressUtility.ConsumeInput();
        }else if(keyPressUtility.hasPressedRedo()){
            mediator.send(new DoRedo());
            keyPressUtility.ConsumeInput();
        }else if(keyPressUtility.hasPressedExecute()){
            if(mediator.send(new CanStart())){
                mediator.send(new ExecuteProgram());
                keyPressUtility.ConsumeInput();
                getViewContext().repaint();
            }
        }else if(keyPressUtility.hasPressedResetProgram()){
            mediator.send(new DoReset());
            keyPressUtility.ConsumeInput();
            getViewContext().repaint();
        }
        keyPressUtility.Reset();
    }

    protected static boolean programHasFunction(){
        return programBlockComponents.stream().anyMatch(pbc -> pbc.getSource() instanceof FunctionDefinitionBlock);
    }
}
