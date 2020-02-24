package com.blockr.ui.components.textcomponent;

import com.blockr.ui.components.Component;

import java.awt.*;

public class TextComponent implements Component {

    public TextAlign getTextAlign(){
        return textAlign;
    }

    public void setTextAlign(TextAlign textAlign){
        this.textAlign = textAlign;
    }

    private TextAlign textAlign = TextAlign.Center;

    private VerticalAlign getVerticalAlign(){
        return verticalAlign;
    }

    public void setVerticalAlign(VerticalAlign verticalAlign){
        this.verticalAlign = verticalAlign;
    }

    private VerticalAlign verticalAlign = VerticalAlign.Middle;

    public String getText(){
        return text;
    }

    private final String text;

    public TextComponent(String text){
        this.text = text;
    }

    @Override
    public void draw(int width, int height, Graphics graphics) {

        var textWidth = graphics.getFontMetrics().stringWidth(getText());
        var textHeight = graphics.getFontMetrics().getHeight();

        int textX = -1;
        int textY = -1;

        switch(getTextAlign()){
            case Left:
                textX = 0;
                break;
            case Center:
                textX = (width - textWidth) / 2;
                break;
            case Right:
                textX = width - textWidth;
        }

        switch(getVerticalAlign()){
            case Top:
                textY = textHeight;
                break;
            case Middle:
                textY = (height - textHeight) / 2;
                break;
            case Bottom:
                textY = (height);
        }

        graphics.drawString(getText(), textX, textY);
    }
}
