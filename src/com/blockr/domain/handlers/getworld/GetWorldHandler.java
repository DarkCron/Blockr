package com.blockr.domain.handlers.getworld;

import an.awesome.pipelinr.Command;
import com.blockr.domain.State;
import com.blockr.domain.gameworld.ReadOnlyGameWorld;
import com.blockr.domain.handlers.HandlerBase;

public class GetWorldHandler extends HandlerBase<GetWorld, ReadOnlyGameWorld> {

    public GetWorldHandler(State state) {
        super(state);
    }

    @Override
    public ReadOnlyGameWorld handle(GetWorld getWorld) {
        return getState().getGameWorld();
    }
}
