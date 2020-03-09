package com.blockr.domain.gameworld;

public interface ReadOnlyGameWorld {

    int getWidth();

    int getHeight();

    TileType getTileType(int x, int y);

    Position getStartPosition();

    Position getGoalPosition();

    Position getRobotPosition();

    Direction getRobotDirection();
}
