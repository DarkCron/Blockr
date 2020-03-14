package com.blockr.handlers.ui;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;
import com.ui.WindowRegion;

public class SetUIInfo implements Command<Voidy> {
    private final WindowRegion canvasRegion;
    private final WindowRegion paletteRegion;
    private final WindowRegion gameAreaRegion;

    public SetUIInfo(WindowRegion canvasRegion, WindowRegion paletteRegion, WindowRegion gameAreaRegion) {
        this.canvasRegion = canvasRegion;
        this.paletteRegion = paletteRegion;
        this.gameAreaRegion = gameAreaRegion;
    }

    public UIInfo build(){
        return new UIInfo(canvasRegion,paletteRegion,gameAreaRegion);
    }
}
