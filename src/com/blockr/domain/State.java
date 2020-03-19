package com.blockr.domain;

import com.blockr.domain.block.BlockProgram;
import com.blockr.domain.gameworld.GameWorld;
import com.blockr.handlers.ui.input.PaletteSelection;
import com.blockr.handlers.ui.input.ProgramAreaSelection;
import com.blockr.ui.components.programblocks.ProgramArea;
import com.ui.WindowPosition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class State {

    public GameWorld getGameWorld(){
        return activeLevel.getGameWorld();
    }


    public BlockProgram getBlockProgram(){
        return blockProgram;
    }

    public void resetBlockProgram(){
        this.blockProgram = new BlockProgram(getGameWorld());
    }

    private BlockProgram blockProgram;

    public List<Level> getLevels(){
        return Collections.unmodifiableList(levels);
    }

    private final List<Level> levels;

    public Level getActiveLevel(){
        return activeLevel;
    }

    public void setActiveLevel(Level level){

        if(level == null){
            throw new IllegalArgumentException("level must be effective");
        }

        this.activeLevel = level;
    }

    private Level activeLevel;

    public State(List<Level> levels){

        if(levels.size() == 0){
            throw new IllegalArgumentException("There must be at least one level");
        }

        this.levels = new ArrayList<>(levels);
        this.activeLevel = levels.get(0);

        this.blockProgram = new BlockProgram(getActiveLevel().getGameWorld());
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