package com.blockr;

import com.blockr.domain.block.BlockProgram;
import com.blockr.handlers.ui.input.PaletteSelection;
import com.blockr.handlers.ui.input.ProgramAreaSelection;
import com.gameworld.GameWorld;
import com.ui.WindowPosition;

public class State {

    public GameWorld getGameWorld(){
        return gameWorld;
    }

    private final GameWorld gameWorld;

    public BlockProgram getBlockProgram(){
        return blockProgram;
    }

    private BlockProgram blockProgram;

    public State(GameWorld gameWorld){
        this.gameWorld = gameWorld;
        this.blockProgram = new BlockProgram(gameWorld);
    }

    /*UI STUFF*/
    private PaletteSelection paletteSelection;
    public void setPaletteSelection(PaletteSelection paletteSelection) {
        this.paletteSelection = paletteSelection;
    }
    public PaletteSelection getPaletteSelection() {
        return paletteSelection;
    }

    private ProgramAreaSelection programAreaSelection;
    public ProgramAreaSelection getProgramAreaSelection() {
        return programAreaSelection;
    }
    public void setProgramAreaSelection(ProgramAreaSelection programAreaSelection) {
        this.programAreaSelection = programAreaSelection;
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
        programAreaSelection = null;
    }


}