package com.blockr.ui.components;

public class ScreenRegion {

    public ScreenPosition getTopLeft(){
        return topLeft;
    }

    private final ScreenPosition topLeft;

    public ScreenPosition getBottomRight(){
        return bottomRight;
    }

    private final ScreenPosition bottomRight;

    public ScreenRegion(ScreenPosition topLeft, ScreenPosition bottomRight){
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

}
