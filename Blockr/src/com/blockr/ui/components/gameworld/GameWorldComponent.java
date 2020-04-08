package com.blockr.ui.components.gameworld;

import an.awesome.pipelinr.Pipeline;
import com.blockr.handlers.world.GetWorld;
import com.gameworld.GameWorld;
import com.ui.Component;

import java.awt.*;

public class GameWorldComponent extends Component {

    private GameWorld gameWorld;

    public GameWorldComponent(Pipeline pipeline) {
        this.gameWorld = pipeline.send(new GetWorld());
    }

    @Override
    protected void draw(Graphics graphics) {
        gameWorld.draw(graphics);
    }
}
