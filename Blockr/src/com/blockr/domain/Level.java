package com.blockr.domain;

import com.blockr.domain.gameworld.GameWorld;

public class Level {

    public GameWorld getGameWorld(){
        return gameWorld;
    }

    private final GameWorld gameWorld;

    public int getMaxBlocks(){
        return maxBlocks;
    }

    private final int maxBlocks;

    public Level(GameWorld gameWorld, int maxBlocks){
        this.gameWorld = gameWorld;
        this.maxBlocks = maxBlocks;
    }


}
