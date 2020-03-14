package com.blockr.ui.components.gridui;

import com.blockr.domain.gameworld.GameWorld;
import com.blockr.domain.gameworld.Orientation;
import com.blockr.domain.gameworld.Position;
import com.blockr.domain.gameworld.TileType;
import com.ui.Component;
import com.ui.Container;
import com.ui.WindowRegion;
import com.ui.components.divcomponent.Border;
import com.ui.components.divcomponent.DivComponent;
import com.ui.components.divcomponent.Margin;
import com.ui.components.divcomponent.Padding;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GridContainerComponent extends Container {
    private GameWorld gameWorld;
    private static final GameWorld testWorld;
    static {
        TileType[][] tempTiles = new TileType[4][4];

        for (int i = 0; i < tempTiles.length; i++) {
            for (int j = 0; j < tempTiles[i].length; j++) {
                if(i == 0 || j == 0 || i == tempTiles.length-1 || j == tempTiles[i].length-1){
                    tempTiles[i][j] = TileType.Blocked;
                    //System.out.println("X: "+j + " , Y: "+i);
                }else{
                    tempTiles[i][j] = TileType.Free;
                }
            }
        }

        testWorld = new GameWorld(tempTiles,new Position(1,1), Orientation.NORTH,new Position(tempTiles.length-2,tempTiles.length-2));
        testWorld.reset();
    }

    private List<Component> children;
    @Override
    public List<Component> getChildren() {
        return children;
    }

    public GameWorld getGameWorld() {
        return gameWorld;
    }

    public GridContainerComponent(GameWorld gameWorld){
        this.gameWorld = gameWorld == null ? testWorld : gameWorld;
        children = new ArrayList<>();

        generateInternalComponents();
    }

    private void generateInternalComponents() {
        Component[][] gridColumns = new Component[gameWorld.getHeight()][gameWorld.getWidth()];

        for (int i = 0; i < gameWorld.getHeight(); i++) {

            gridColumns[i] = new Component[gameWorld.getWidth()];

            for (int j = 0; j < gameWorld.getWidth(); j++) {
                gridColumns[i][j] = DivComponent.builder()
                        .withPadding(new Padding(0))
                        .withBorder(new Border(Color.BLACK, 1 , 1, 1, 1))
                        .addChildren(new GridTile(gameWorld.getTileType(j,i)))
                        .build();
                children.add(gridColumns[i][j]);
            }
        }

        children.add(new RobotTile(gameWorld.getRobotPosition(),gameWorld.getRobotOrientation()));
        children.add(new GoalTile(gameWorld.getGoalPosition()));
    }

    /*
    Centers the "Grid-World" in this container
     */
    @Override
    public WindowRegion getChildRegion(WindowRegion region, Component child) {
        var childIndex = getChildren().indexOf(child);

        if(child instanceof RobotTile){
            childIndex = getChildren().indexOf(children.get(((RobotTile) child).robotPosition.getX() + ((RobotTile) child).robotPosition.getY() * gameWorld.getWidth()));
        }else if(child instanceof GoalTile){
            childIndex = getChildren().indexOf(children.get(((GoalTile) child).goalPosition.getX() + ((GoalTile) child).goalPosition.getY() * gameWorld.getWidth()));
        }

        var childIndexX = childIndex % gameWorld.getWidth();
        var childIndexY = childIndex / gameWorld.getWidth();
        var width = region.getWidth() / gameWorld.getWidth();
        var height = region.getHeight() / gameWorld.getHeight();

        var size = Math.min(width, height);

        var offsetX = region.getWidth()-size * gameWorld.getWidth();
        var offsetY = region.getHeight()-size * gameWorld.getHeight();

        //return region;
        return new WindowRegion(
                region.getMinX() + offsetX/2 + childIndexX * size,
                region.getMinY() + offsetY/2 + childIndexY * size,
                region.getMinX() + offsetX/2 + childIndexX* size + size,
                region.getMinY() + offsetY/2 + childIndexY * size + size);
    }

    @Override
    protected void draw(Graphics graphics) {

    }
}

/*
TODO: instead of doing this, figure out a way to use world state via the pipeline
 */
class RobotTile extends Component{

    Position robotPosition;
    Orientation robotOrientation;

    RobotTile(Position robotPosition, Orientation robotOrientation){
        this.robotPosition = robotPosition;
        this.robotOrientation = robotOrientation;
    }

    void update(Position robotPosition, Orientation robotOrientation){
        this.robotPosition = robotPosition;
        this.robotOrientation = robotOrientation;
    }

    @Override
    protected void draw(Graphics graphics) {
        var windowRegion = WindowRegion.fromGraphics(graphics);
        graphics = graphics.create(windowRegion.getMinX(), windowRegion.getMinY(), windowRegion.getWidth(), windowRegion.getHeight());

        float antennaCenterRadius = 0.2f;
        FloatPosition antennaCenter = new FloatPosition(0.5f-antennaCenterRadius/2,1f-0.1f-antennaCenterRadius/2);

        FloatPosition antennaPoleStart = new FloatPosition(0.5f,0.9f-antennaCenterRadius/2);
        FloatPosition antennaPoleEnd = new FloatPosition(0.5f,0.6f);

        FloatPosition robotFaceStart = new FloatPosition(0.15f,0.15f);
        FloatPosition robotFaceEnd = new FloatPosition(0.85f,0.6f);
        FloatPosition robotFaceSize = new FloatPosition(robotFaceEnd.x-robotFaceStart.x,robotFaceEnd.y-robotFaceStart.y);

        float robotEyeSize = 0.3f;
        FloatPosition robotLeftEye = new FloatPosition(0.35f-robotEyeSize/2,0.25f);
        FloatPosition robotRightEye = new FloatPosition(0.65f-robotEyeSize/2,0.25f);

        float innerRobotEyeSize = 0.15f;
        FloatPosition innerRobotLeftEye = new FloatPosition(0.35f-innerRobotEyeSize/2,0.25f);
        FloatPosition innerRobotRightEye = new FloatPosition(0.65f-innerRobotEyeSize/2,0.25f);

        switch (robotOrientation){
            case NORTH:
                break;
            case EAST:
                innerRobotLeftEye = new FloatPosition(0.45f-innerRobotEyeSize/2,0.35f);
                innerRobotRightEye = new FloatPosition(0.75f-innerRobotEyeSize/2,0.35f);
                break;
            case SOUTH:
                innerRobotLeftEye = new FloatPosition(0.35f-innerRobotEyeSize/2,0.40f);
                innerRobotRightEye = new FloatPosition(0.65f-innerRobotEyeSize/2,0.40f);
                break;
            case WEST:
                innerRobotLeftEye = new FloatPosition(0.25f-innerRobotEyeSize/2,0.35f);
                innerRobotRightEye = new FloatPosition(0.55f-innerRobotEyeSize/2,0.35f);
                break;
        }
        graphics.setColor(Color.ORANGE);
        graphics.fillArc((int)(windowRegion.getWidth()*antennaCenter.x),
                (int)(windowRegion.getHeight()*antennaCenter.y),
                (int)(windowRegion.getWidth()*antennaCenterRadius),
                (int)(windowRegion.getHeight()*antennaCenterRadius),
                0,360);
        graphics.setColor(Color.BLACK);
        graphics.drawArc((int)(windowRegion.getWidth()*antennaCenter.x),
                (int)(windowRegion.getHeight()*antennaCenter.y),
                (int)(windowRegion.getWidth()*antennaCenterRadius),
                (int)(windowRegion.getHeight()*antennaCenterRadius),
                0,360);
        graphics.drawLine(antennaPoleStart.getX(windowRegion.getWidth()),antennaPoleStart.getY(windowRegion.getHeight()),
                antennaPoleEnd.getX(windowRegion.getWidth()),antennaPoleEnd.getY(windowRegion.getHeight()));
        graphics.setColor(Color.CYAN);
        graphics.fillRect(robotFaceStart.getX(windowRegion.getWidth()),robotFaceStart.getY(windowRegion.getHeight()),
                robotFaceSize.getX(windowRegion.getWidth()),robotFaceSize.getY(windowRegion.getHeight()));
        graphics.setColor(Color.BLACK);
        graphics.drawRect(robotFaceStart.getX(windowRegion.getWidth()),robotFaceStart.getY(windowRegion.getHeight()),
                robotFaceSize.getX(windowRegion.getWidth()),robotFaceSize.getY(windowRegion.getHeight()));
        graphics.setColor(Color.white);
        graphics.fillArc(robotLeftEye.getX(windowRegion.getWidth()),
                robotLeftEye.getY(windowRegion.getHeight()),
                (int)(windowRegion.getWidth()*robotEyeSize),
                (int)(windowRegion.getHeight()*robotEyeSize),
                0,360);
        graphics.fillArc(robotRightEye.getX(windowRegion.getWidth()),
                robotRightEye.getY(windowRegion.getHeight()),
                (int)(windowRegion.getWidth()*robotEyeSize),
                (int)(windowRegion.getHeight()*robotEyeSize),
                0,360);
        graphics.setColor(Color.black);
        graphics.drawArc(robotLeftEye.getX(windowRegion.getWidth()),
                robotLeftEye.getY(windowRegion.getHeight()),
                (int)(windowRegion.getWidth()*robotEyeSize),
                (int)(windowRegion.getHeight()*robotEyeSize),
                0,360);
        graphics.drawArc(robotRightEye.getX(windowRegion.getWidth()),
                robotRightEye.getY(windowRegion.getHeight()),
                (int)(windowRegion.getWidth()*robotEyeSize),
                (int)(windowRegion.getHeight()*robotEyeSize),
                0,360);
        graphics.fillArc(innerRobotLeftEye.getX(windowRegion.getWidth()),
                innerRobotLeftEye.getY(windowRegion.getHeight()),
                (int)(windowRegion.getWidth()*innerRobotEyeSize),
                (int)(windowRegion.getHeight()*innerRobotEyeSize),
                0,360);
        graphics.fillArc(innerRobotRightEye.getX(windowRegion.getWidth()),
                innerRobotRightEye.getY(windowRegion.getHeight()),
                (int)(windowRegion.getWidth()*innerRobotEyeSize),
                (int)(windowRegion.getHeight()*innerRobotEyeSize),
                0,360);
    }

    class FloatPosition{
        private float x,y;
        FloatPosition(float x, float y){
            this.x = x;
            this.y = y;
        }

        int getX(int regionWidth){
            return ((int)(x * regionWidth));
        }

        int getY(int regionHeight){
            return ((int)(y * regionHeight));
        }
    }
}

class GoalTile extends Component{
    Position goalPosition;

    GoalTile(Position goalPosition){
        this.goalPosition = goalPosition;
    }

    @Override
    protected void draw(Graphics graphics) {
        var windowRegion = WindowRegion.fromGraphics(graphics);
        graphics = graphics.create(windowRegion.getMinX(), windowRegion.getMinY(), windowRegion.getWidth(), windowRegion.getHeight());

        float radius = 0.9f;
        float mod = 0.1f;
        for (int i = 1; i < 10; i++) {
            if(i%2 == 0){
                graphics.setColor(Color.white);
            }else{
                graphics.setColor(Color.red);
            }
            graphics.fillArc((int)((0.5-radius/2) * windowRegion.getWidth()),(int)((0.5-radius/2)* windowRegion.getWidth()),(int)(radius* windowRegion.getWidth()),(int)(radius* windowRegion.getWidth()),0,360);
            radius-=mod;
        }

    }
}

/*
Draws the actual tile
 */
class GridTile extends Component{
    private final TileType tileType;

    GridTile(TileType tileType){
        this.tileType = tileType;
    }

    @Override
    protected void draw(Graphics graphics) {
        switch (tileType){
            case Blocked:
                var windowRegion = WindowRegion.fromGraphics(graphics);

                windowRegion = windowRegion.shrinkRegion(Margin.ZERO_MARGIN);
                graphics = graphics.create(windowRegion.getMinX(), windowRegion.getMinY(), windowRegion.getWidth(), windowRegion.getHeight());
                var c = graphics.getColor();

                graphics.setColor(Color.RED);
                graphics.fillRect(0,0,windowRegion.getWidth(),windowRegion.getHeight());

                graphics.setColor(c);
                break;
            case Free:
                windowRegion = WindowRegion.fromGraphics(graphics);

                windowRegion = windowRegion.shrinkRegion(new Margin(0));
                graphics = graphics.create(windowRegion.getMinX(), windowRegion.getMinY(), windowRegion.getWidth(), windowRegion.getHeight());
                c = graphics.getColor();

                graphics.setColor(Color.GREEN);
                graphics.fillRect(0,0,windowRegion.getWidth(),windowRegion.getHeight());

                graphics.setColor(c);
                break;
        }
    }
}
