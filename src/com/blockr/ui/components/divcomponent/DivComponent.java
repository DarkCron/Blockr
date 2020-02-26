package com.blockr.ui.components;

import com.blockr.ui.Component;
import com.blockr.ui.Container;
import com.blockr.ui.WindowRegion;
import purecollections.PList;

import java.awt.*;

public class DivComponent implements Container {

    @Override
    public PList<Component> getChildComponents() {
        return childComponents;
    }

    private PList<Component> childComponents;

    @Override
    public WindowRegion getChildRegion(Component child) {
        return null;
    }

    @Override
    public void draw(Graphics graphics) {

    }
}
