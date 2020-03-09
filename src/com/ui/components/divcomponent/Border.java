package com.ui.components.divcomponent;

import java.awt.*;

public class Border extends BoxProperty {

    public Color getColor(){
        return color;
    }

    private final Color color;

    public Border(Color color, int top, int right, int bottom, int left) {
        super(top, right, bottom, left);
      
        if(color == null){
            throw new IllegalArgumentException("color must be effective");
        }

        this.color = color;
    }

    public Border(Color color, int width){
        //noinspection SuspiciousNameCombination
        this(color, width, width, width, width);
    }
}
