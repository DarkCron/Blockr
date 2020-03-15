package com.blockr.handlers.level.selectlevel;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;
import com.blockr.domain.State;

public class SelectLevelHandler implements Command.Handler<SelectLevel, Voidy>{

    private final State state;

    public SelectLevelHandler(State state){
        this.state = state;
    }

    @Override
    public Voidy handle(SelectLevel selectLevel) {

        var levels = state.getLevels();

        if(selectLevel.getLevel() < 0 || selectLevel.getLevel() >= levels.size())
            return new Voidy();


        var newLevel = levels.get(selectLevel.getLevel());
        newLevel.getGameWorld().reset();

        state.setActiveLevel(newLevel);
        state.resetBlockProgram();

        return new Voidy();
    }
}
