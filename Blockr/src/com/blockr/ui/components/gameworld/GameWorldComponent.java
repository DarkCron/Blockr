package com.blockr.ui.components.gameworld;

import an.awesome.pipelinr.Pipeline;
import com.blockr.handlers.world.GetWorld;
import com.gameworld.GameWorld;
import com.ui.Component;

import java.awt.*;

public class GameWorldComponent extends Component {

    private final Pipeline pipeline;
    
    public GameWorldComponent(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @Override
    protected void draw(Graphics graphics) {
        var gameWorld = pipeline.send(new GetWorld());
        gameWorld.draw(graphics);
    }
}
