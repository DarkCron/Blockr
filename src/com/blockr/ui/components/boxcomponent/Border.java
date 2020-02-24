package com.blockr.ui.components.boxcomponent;

import java.awt.*;

public class Border extends BoxProperty {

    public Color getColor(){
        return color;
    }

    private final Color color;

    public Border(Color color, int top, int right, int bottom, int left) {
        super(top, right, bottom, left);
        this.color = color;
    }

    public Border(Color color, int border){
        super(border);
        this.color = color;
    }
}
