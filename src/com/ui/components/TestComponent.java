package com.ui.components;

import com.ui.Component;
import com.ui.WindowRegion;
import com.ui.mouseevent.MouseEvent;

import java.awt.*;

public class TestComponent extends Component {

    private Color color = Color.BLACK;

    @Override
    protected void draw(Graphics graphics) {
        var region = WindowRegion.fromGraphics(graphics);
        graphics.setColor(color);
        graphics.fillRect(0, 0, region.getWidth(), region.getHeight());
    }

    @Override
    public void onMouseEvent(MouseEvent mouseEvent) {
        if(mouseEvent.getType() == MouseEvent.Type.MOUSE_DOWN){
            color = color == Color.BLACK ? Color.RED : Color.BLACK;
            getViewContext().repaint();
        }
    }
}
