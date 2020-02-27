package com.ui.components.textcomponent;

import com.ui.Component;
import com.ui.WindowRegion;

import java.awt.*;

public class TextComponent extends Component {

    public HorizontalAlign getHorizontalAlign(){
        return horizontalAlign;
    }

    private final HorizontalAlign horizontalAlign;

    public VerticalAlign getVerticalAlign(){
        return verticalAlign;
    }

    private final VerticalAlign verticalAlign;

    public String getText(){
        return text;
    }

    private final String text;

    public int getFontSize(){
        return fontSize;
    }

    private int fontSize;

    public TextComponent(String text, int fontSize, HorizontalAlign horizontalAlign, VerticalAlign verticalAlign){
        this.text = text;
        this.fontSize = fontSize;
        this.horizontalAlign = horizontalAlign;
        this.verticalAlign = verticalAlign;
    }

    @Override
    public void draw(Graphics graphics) {

        var text = getText();

        var region = WindowRegion.toWindowRegion(graphics);

        graphics.setFont(new Font(graphics.getFont().getName(), graphics.getFont().getStyle(), getFontSize()));
        var textWidth = graphics.getFontMetrics().stringWidth(text);
        var textHeight = graphics.getFontMetrics().getHeight();

        int x = 0;

        switch(getHorizontalAlign()){

            case Left:
                x = 0;
                break;
            case Center:
                x = (region.getWidth() - textWidth) / 2;
                break;
            case Right:
                x = region.getWidth() - textWidth;
                break;
        }

        int y = 0;

        switch(getVerticalAlign()){

            case Top:
                y = textHeight;
                break;
            case Middle:
                y = (region.getHeight() - textHeight) / 2 + textHeight;
                break;
            case Bottom:
                y = region.getHeight();
                break;
        }

        graphics.drawString(text, x, y);
    }
}
