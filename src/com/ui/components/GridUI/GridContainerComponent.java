package com.ui.components.GridUI;

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
        TileType[][] tempTiles = new TileType[10][10];

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

        testWorld = new GameWorld(tempTiles,new Position(1,1), Orientation.NORTH,new Position(tempTiles.length-1,tempTiles.length-1));
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
    }

    @Override
    public WindowRegion getChildRegion(WindowRegion region, Component child) {
        var childIndexX = getChildren().indexOf(child) % gameWorld.getWidth();
        var childIndexY = getChildren().indexOf(child) / gameWorld.getWidth();
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
        drawRobot(gameWorld.getRobotPosition(),gameWorld.getRobotOrientation(),graphics);
    }

    private void drawRobot(Position robotPosition, Orientation robotOrientation, Graphics graphics) {
        var windowRegion = getChildRegion(WindowRegion.fromGraphics(graphics),children.get(robotPosition.getX() + robotPosition.getY() * gameWorld.getWidth()));
        graphics = graphics.create(windowRegion.getMinX(), windowRegion.getMinY(), windowRegion.getWidth(), windowRegion.getHeight());

        FloatPosition antennaCenter = new FloatPosition(0.5f,0.1f);
        float antennaCenterRadius = 0.1f;


        switch (robotOrientation){
            case NORTH:
                {

                }
                graphics.setColor(Color.BLACK);
                graphics.drawArc((int)(windowRegion.getWidth()*antennaCenter.x),
                        (int)(windowRegion.getHeight()*antennaCenter.y),
                        (int)(windowRegion.getWidth()*antennaCenterRadius),
                        (int)(windowRegion.getHeight()*antennaCenterRadius),
                        0,360);
                break;
        }
    }

    class FloatPosition{
        private float x,y;
        FloatPosition(float x, float y){
            this.x = x;
            this.y = y;
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
