package com.blockr.handlers.world;

import com.blockr.State;
import com.blockr.handlers.HandlerBase;
import com.gameworld.GameWorld;

public class GetWorldHandler extends HandlerBase<GetWorld, GameWorld> {

    public GetWorldHandler(State state) {
        super(state);
    }

    @Override
    public GameWorld handle(GetWorld getWorld) {
        return getState().getGameWorld();
    }
}
