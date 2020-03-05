package com.ui.components.divcomponent;

import com.ui.Component;
import com.ui.Container;
import com.ui.WindowPosition;
import com.ui.WindowRegion;

import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DivComponent extends Container {

    public static Margin DEFAULT_MARGIN = new Margin(0);
    public static Border DEFAULT_BORDER = new Border(Color.BLACK, 0);
    public static Padding DEFAULT_PADDING = new Padding(5);

    public List<Component> getChildren() {
        return Collections.unmodifiableList(children);
    }

    private List<Component> children;

    public Margin getMargin(){
        return margin;
    }

    private final Margin margin;

    public Border getBorder(){
        return border;
    }

    private final Border border;

    public Padding getPadding(){
        return padding;
    }

    private final Padding padding;

    public FlexAxis getFlexAxis(){
        return flexAxis;

    }

    private FlexAxis flexAxis;

    private DivComponent(List<Component> children, Margin margin, Border border, Padding padding, FlexAxis flexAxis){

        throwIfNull(margin, "margin");
        throwIfNull(border, "border");
        throwIfNull(padding, "padding");
        throwIfNull(flexAxis, "flexAxis");
      
        this.children = new LinkedList<>(children);

        this.margin = margin;
        this.border = border;
        this.padding = padding;
        this.flexAxis = flexAxis;
    }

    private void throwIfNull(Object object, String name){
        if(object != null)
            return;

        throw new IllegalArgumentException(String.format("%s must be effective", name));
    }
  
    /**
     * Returns the region where the given child component should be drawn
     *
     * @param   region
     *          The WindowRegion where this component is drawn
     * @param   child
     *          The given child
     * @return  A WindowRegion representing the region where the child should be drawn
     */
    public WindowRegion getChildRegion(WindowRegion region, Component child) {

        region = getContentRegion(region);

        var childIndex = getChildren().indexOf(child);

        if(childIndex == -1){
            throw new IllegalArgumentException("The given component is not a child of this component");
        }

        var flexHorizontal = getFlexAxis() == FlexAxis.Horizontal;

        var axisLength = flexHorizontal ? region.getWidth() : region.getHeight();
        var childLength = axisLength / getChildren().size();

        if(flexHorizontal){
            return new WindowRegion(
                    region.getMinX() + childIndex * childLength,
                    region.getMinY(),
                    region.getMinX() + childIndex* childLength + childLength,
                    region.getMaxY());
        }

        return new WindowRegion(
                region.getMinX(),
                region.getMinY() + childIndex * childLength,
                region.getMaxX(),
                region.getMinY() + childIndex * childLength + childLength
        );
    }

    private WindowRegion getContentRegion(WindowRegion region){

        var props = Arrays.asList(getMargin(), getBorder(), getPadding());

        for(var prop : props){

            if(region.isEmpty()){
                return WindowRegion.EMPTY;
            }

            region = region.shrinkRegion(prop);
        }

        return region;
    }

    public void draw(Graphics graphics) {
      
        var region = WindowRegion.fromGraphics(graphics);

        //margin
        region = region.shrinkRegion(margin);
        graphics = shrinkGraphics(graphics, region);

        //border
        graphics.setColor(getBorder().getColor());

        graphics.fillRect(0, 0, region.getWidth(), getBorder().getTop());
        graphics.fillRect(region.getWidth() - getBorder().getRight(), 0, getBorder().getRight(), region.getHeight());
        graphics.fillRect(0, region.getHeight() - getBorder().getBottom(), region.getWidth(), getBorder().getBottom());
        graphics.fillRect(0, 0, getBorder().getLeft(), region.getHeight());
    }

    private Graphics shrinkGraphics(Graphics graphics, WindowRegion region){
        return graphics.create(region.getMinX(), region.getMinY(), region.getWidth(), region.getHeight());
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder {

        private Margin margin = DEFAULT_MARGIN;

        private Border border = DEFAULT_BORDER;

        private Padding padding = DEFAULT_PADDING;

        private FlexAxis flexAxis = FlexAxis.Horizontal;

        private List<Component> children = new LinkedList<>();

        public Builder withMargin(Margin margin){
            this.margin = margin;
            return this;
        }

        public Builder withBorder(Border border){
            this.border = border;
            return this;
        }

        public Builder withPadding(Padding padding){
            this.padding = padding;
            return this;
        }

        public Builder addChildren(Component... children){

            this.children.addAll(Arrays.asList(children));

            return this;
        }

        public Builder withFlexAxis(FlexAxis flexAxis){
            this.flexAxis = flexAxis;
            return this;
        }

        public DivComponent build(){
            return new DivComponent(children, margin, border, padding, flexAxis);
        }
    }
}
