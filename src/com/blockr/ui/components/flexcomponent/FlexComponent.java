package com.blockr.ui.components.flexcomponent;

import com.blockr.ui.components.Component;
import purecollections.PList;

import java.awt.*;

public class FlexComponent implements Component {

    public PList<Component> getChildComponents(){
        return childComponents;
    }

    public void addChildComponent(Component component){
        childComponents = childComponents.plus(component);
    }

    private PList<Component> childComponents = PList.empty();

    public FlexDirection getFlexDirection(){
        return flexDirection;
    }

    public void setFlexDirection(FlexDirection direction){
        this.flexDirection = direction;
    }

    private FlexDirection flexDirection = FlexDirection.Horizontal;

    @Override
    public void draw(int width, int height, Graphics graphics) {

        var flexHorizontal = getFlexDirection() == FlexDirection.Horizontal;

        var toDivide = flexHorizontal ? width : height;
        var unit = toDivide / childComponents.size();

        int current = 0;
        for(var child : getChildComponents()){

            var childGraphics = flexHorizontal ? graphics.create(current, 0, unit, height) : graphics.create(0, current, width, unit);

            current += unit;

            if(flexHorizontal) {
                child.draw(unit, height, childGraphics);
                continue;
            }

            child.draw(width, unit, childGraphics);
        }
    }
}
