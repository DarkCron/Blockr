package com.blockr.domain;

import com.blockr.domain.block.BlockProgram;
import com.blockr.domain.gameworld.GameWorld;
import com.blockr.handlers.ui.input.PaletteSelection;
import com.ui.WindowPosition;

public class State {
    public GameWorld getGameWorld(){
        return gameWorld;
    }

    public void setGameWorld(GameWorld gameWorld){

        if(gameWorld == null){
            throw new IllegalArgumentException("gameWorld must be effective");
        }

        this.gameWorld = gameWorld;
        //TODO: this is temp
        this.blockProgram = new BlockProgram(gameWorld);
    }

    private GameWorld gameWorld;

    public BlockProgram getBlockProgram(){
        return blockProgram;
    }

    public void setBlockProgram(BlockProgram blockProgram){

        if(blockProgram == null){
            throw new IllegalArgumentException("programExecutor must be effective");
        }

        this.blockProgram = blockProgram;
    }

    private BlockProgram blockProgram;

    public void createBlockProgram() {
        blockProgram = new BlockProgram(getGameWorld());
    }

    /*UI STUFF*/
    private PaletteSelection paletteSelection;
    public void setPaletteSelection(PaletteSelection paletteSelection) {
        this.paletteSelection = paletteSelection;
    }
    public PaletteSelection getPaletteSelection() {
        return paletteSelection;
    }

    private WindowPosition recordMouse = null;
    public void setRecordMouse(WindowPosition recordMouse) {
        this.recordMouse = recordMouse;
    }
    public WindowPosition getRecordMouse() {
        return recordMouse;
    }

    public void resetUIState() {
        paletteSelection = null;
        recordMouse = null;
    }
}