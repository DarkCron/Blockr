package com.blockr.handlers.level.selectlevel;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;

public class SelectLevel implements Command<Voidy> {

    public int getLevel(){
        return level;
    }

    private final int level;

    public SelectLevel(int level){
        this.level = level;
    }

}
