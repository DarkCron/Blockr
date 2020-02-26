package com.blockr.ui;

import com.blockr.ui.components.SolidRectComponent;
import com.blockr.ui.components.boxcomponent.Border;
import com.blockr.ui.components.boxcomponent.BoxComponent;
import com.blockr.ui.components.boxcomponent.Margin;
import com.blockr.ui.components.boxcomponent.Padding;
import com.blockr.ui.components.flexcomponent.FlexComponent;
import com.blockr.ui.components.flexcomponent.FlexDirection;
import com.blockr.ui.components.textcomponent.TextComponent;
import com.kul.CanvasWindow;

import java.awt.*;

public class MyCanvasWindow extends CanvasWindow {

    private static FlexComponent rootComponent = new FlexComponent();

    private static BoxComponent createDefaultBox(){
        var comp = new BoxComponent();

        comp.setMargin(new Margin(5));
        comp.setPadding(new Padding(10));
        comp.setChildComponent(new TextComponent("GameWorld"));

        return comp;
    }

    static {
        var worldComponent = new SolidRectComponent(Color.BLACK);

        var belowWorld = new BoxComponent();
        belowWorld.setMargin(new Margin(5));
        belowWorld.setPadding(new Padding(10));
        belowWorld.setBorder(new Border(Color.GREEN, 5));
        belowWorld.setChildComponent(new TextComponent("This is below the world"));

        var worldSurround = new FlexComponent();
        worldSurround.setFlexDirection(FlexDirection.Vertical);
        worldSurround.addChildComponent(worldComponent);
        worldSurround.addChildComponent(belowWorld);

        var worldBox = createDefaultBox();
        worldBox.setChildComponent(worldSurround);

        var palleteBox = createDefaultBox();
        palleteBox.setChildComponent(new TextComponent("Pallete"));

        var programBox = createDefaultBox();
        programBox.setChildComponent(new TextComponent("Program area"));

        rootComponent.addChildComponent(worldBox);
        rootComponent.addChildComponent(palleteBox);
        rootComponent.addChildComponent(programBox);
    }

    public MyCanvasWindow(String title) {
        super(title);
    }

    @Override
    protected void paint(Graphics g) {
        var clipRect = g.getClipBounds();
        rootComponent.draw(clipRect.width, clipRect.height, g);
    }

    @Override
    protected void handleMouseEvent(int id, int x, int y, int clickCount) {

    }

    @Override
    protected void handleKeyEvent(int id, int keyCode, char keyChar) {

    }
}
