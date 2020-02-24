package com.blockr.ui.components;

import java.awt.*;

public class SolidRectComponent implements Component {

    public Color getColor(){
        return color;
    }

    private final Color color;

    public SolidRectComponent(Color color){
        this.color = color;
    }

    @Override
    public void draw(int width, int height, Graphics graphics) {
        graphics.setColor(color);
        graphics.fillRect(0, 0, width, height);
    }
}
