package com.ui;

import com.ui.mouseevent.MouseEvent;

import java.awt.*;

public abstract class Component {

    protected abstract void draw(Graphics graphics);

    public Component getComponentAt(WindowPosition windowPosition){
        return this;
    }

    public void onMouseEvent(MouseEvent mouseEvent){

    }
}
