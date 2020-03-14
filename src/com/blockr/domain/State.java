package com.blockr.domain;

import com.blockr.domain.block.BlockProgram;
import com.blockr.domain.gameworld.GameWorld;
import com.blockr.handlers.ui.UIInfo;
import com.ui.MyCanvasWindow;

public class State {

    public UIInfo getUiInfo() {
        return uiInfo;
    }

    public void setUiInfo(UIInfo uiInfo) {
        this.uiInfo = uiInfo;
    }

    private UIInfo uiInfo;

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
}