package com.blockr.ui.components.boxcomponent;

import com.blockr.ui.components.Component;

import java.awt.Graphics;
import java.awt.Color;

public class BoxComponent implements Component {

    public final Margin DEFAULT_MARGIN = new Margin(1, 1, 1,1);
    public final Padding DEFAULT_PADDING = new Padding(0, 0, 0, 0);
    public final Border DEFAULT_BORDER = new Border(Color.BLACK, 1, 1, 1, 1);

    public BoxComponent(){

    }

    public Component getChildComponent(){
        return childComponent;
    }

    public void setChildComponent(Component component){
        this.childComponent = component;
    }

    private Component childComponent;

    public Margin getMargin(){
        return margin;
    }

    public void setMargin(Margin margin){
        this.margin = margin;
    }

    private Margin margin = DEFAULT_MARGIN;

    public void setPadding(Padding padding){
        this.padding = padding;
    }

    private Padding padding = DEFAULT_PADDING;

    public Border getBorder(){
        return border;
    }

    public void setBorder(Border border){
        this.border = border;
    }

    private Border border = DEFAULT_BORDER;

    @Override
    public void draw(int width, int height, Graphics graphics) {

        //TODO: throw exceptions if dimensions are negative?

        var topX = margin.getLeft();
        var topY = margin.getTop();
        var bottomX = width - margin.getRight();
        var bottomY = height - margin.getBottom();

        var widthAfterMargin = bottomX - topX;
        var heightAfterMargin = bottomY - topY;

        if(widthAfterMargin <= 0 || heightAfterMargin <= 0)
        {
            //The margin takes up the entire box
            return;
        }

        //nothing should be drawn in the margin
        graphics = graphics.create(topX, topY, widthAfterMargin, heightAfterMargin);

        //draw the border
        graphics.setColor(border.getColor());

        graphics.fillRect(0, 0, widthAfterMargin, border.getTop());
        graphics.fillRect(0, 0, border.getLeft(), heightAfterMargin);
        graphics.fillRect(widthAfterMargin - border.getRight(), 0, border.getRight(), heightAfterMargin);
        graphics.fillRect(0, heightAfterMargin - border.getBottom(), widthAfterMargin, border.getBottom());

        topX = padding.getLeft();
        topY = padding.getTop();
        bottomX = widthAfterMargin - padding.getRight();
        bottomY = heightAfterMargin - padding.getBottom();

        var widthAfterPadding = bottomX - topX;
        var heightAfterPadding = bottomY - topY;

        if(widthAfterPadding <= 0 || heightAfterPadding <= 0){
            //no space left for the content
            return;
        }

        graphics = graphics.create(topX, topY, widthAfterPadding, heightAfterPadding);

        if(getChildComponent() != null){
            getChildComponent().draw(widthAfterPadding, heightAfterPadding, graphics);
        }
    }


}
