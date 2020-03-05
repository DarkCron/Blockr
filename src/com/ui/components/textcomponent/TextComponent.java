package com.ui.components.textcomponent;

import com.ui.Component;
import com.ui.WindowRegion;

import java.awt.*;

public class TextComponent extends Component {

    public static final int MINIMUM_FONT_SIZE = 8;
    public static final int DEFAULT_FONT_SIZE = 12;

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
      
        throwIfNull(text, "text");

        if(fontSize < MINIMUM_FONT_SIZE){
            throw new IllegalArgumentException("fontSize must be greater than " + MINIMUM_FONT_SIZE);
        }

        throwIfNull(horizontalAlign, "horizontalAlign");
        throwIfNull(verticalAlign, "verticalAlign");
      
        this.text = text;
        this.fontSize = fontSize;
        this.horizontalAlign = horizontalAlign;
        this.verticalAlign = verticalAlign;
    }

    public TextComponent(String text, int fontSize){
        this(text, fontSize, HorizontalAlign.Center, VerticalAlign.Middle);
    }

    public TextComponent(String text){
        this(text, DEFAULT_FONT_SIZE);
    }

    private void throwIfNull(Object object, String name){
        if(object != null)
            return;
        throw new IllegalArgumentException(String.format("%s must be effective", name));
    }

    @Override
    public void draw(Graphics graphics) {

        var text = getText();

        var region = WindowRegion.fromGraphics(graphics);

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
