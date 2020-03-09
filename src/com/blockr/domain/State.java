package com.blockr.domain;

import com.blockr.domain.gameworld.GameWorld;

public class State {

    public GameWorld getGameWorld(){
        return gameWorld;
    }

    public void setGameWorld(GameWorld gameWorld){

        if(gameWorld == null){
            throw new IllegalArgumentException("gameWorld must be effective");
        }

        this.gameWorld = gameWorld;
    }

    private GameWorld gameWorld;

    public ProgramExecutor getProgramExecutor(){
        return programExecutor;
    }

    public void setProgramExecutor(ProgramExecutor programExecutor){

        if(programExecutor == null){
            throw new IllegalArgumentException("programExecutor must be effective");
        }

        this.programExecutor = programExecutor;
    }

    private ProgramExecutor programExecutor;

}
