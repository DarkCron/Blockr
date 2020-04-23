package com.blockr.ui.components.executeprogrambutton;

import an.awesome.pipelinr.Pipeline;
import com.blockr.handlers.blockprogram.canstart.CanStart;
import com.blockr.handlers.blockprogram.executeprogram.ExecuteProgram;
import com.ui.WindowRegion;
import com.ui.components.divcomponent.*;
import com.ui.components.textcomponent.TextComponent;
import com.ui.mouseevent.MouseEvent;

import java.awt.*;
import java.util.ArrayList;

public class ExecuteProgramButton extends DivComponent {

    private static final int BUTTON_WIDTH = 100;
    private static final int BUTTON_HEIGHT = 40;

    private static final Color BUTTON_COLOR_ENABLED = Color.ORANGE;
    private static final Color BUTTON_COLOR_DISABLED = Color.RED;

    private static final Color BUTTON_BORDER_COLOR = Color.BLACK;
    private static final int BUTTON_BORDER_WIDTH = 1;

    private final Pipeline pipeline;

    private boolean enabled = false;

    private static final TextComponent TEXT = new TextComponent("Execute program", 12);


    public ExecuteProgramButton(Pipeline pipeline) {
        super(new ArrayList<>(), Margin.NONE, new Border(BUTTON_BORDER_COLOR, BUTTON_BORDER_WIDTH), Padding.NONE, FlexAxis.Horizontal);
        this.pipeline = pipeline;
    }

    @Override
    public void draw(Graphics graphics) {

        //check if the program is executable
        enabled = pipeline.send(new CanStart());

        var windowRegion = WindowRegion.fromGraphics(graphics);

        var offsetX = (windowRegion.getWidth() - BUTTON_WIDTH) / 2;
        var offsetY = (windowRegion.getHeight() - BUTTON_HEIGHT) / 2;

        if(offsetX < 0 || offsetY < 0){
            return;
        }

        var newRegion = windowRegion.shrinkRegion(new Margin(offsetY, offsetX, offsetY, offsetX));

        super.draw(newRegion.create(graphics));

        var contentRegion = getContentRegion(newRegion);

        var color = enabled ? BUTTON_COLOR_ENABLED : BUTTON_COLOR_DISABLED;

        graphics.setColor(color);
        graphics.fillRect(contentRegion.getMinX(), contentRegion.getMinY(), contentRegion.getWidth(), contentRegion.getHeight());

        graphics.setColor(Color.black);
        TEXT.draw(graphics);
    }

    private static boolean bMouseLock = false;

    @Override
    public void onMouseEvent(MouseEvent mouseEvent) {
        enabled = pipeline.send(new CanStart());

        switch (mouseEvent.getType()){
            case MOUSE_UP:
                bMouseLock = false;
                break;
        }

        if(!enabled){
            //optionally do something here
            return;
        }
        if(bMouseLock){
            return;
        }

        switch (mouseEvent.getType()){
            case MOUSE_DOWN:
                bMouseLock = true;
                pipeline.send(new ExecuteProgram());
                break;
            case MOUSE_UP:
                bMouseLock = false;
                break;
        }


        getViewContext().repaint();

    }
}
