package com.robotgame;

import com.gameworld.GameWorldSnapshot;

class RobotGameWorldSnapshot implements GameWorldSnapshot {

    Position getRobotPosition(){
        return robotPosition;
    }

    private final Position robotPosition;

    Orientation getRobotOrientation(){
        return orientation;
    }

    private final Orientation orientation;

    RobotGameWorldSnapshot(Position robotPosition, Orientation orientation) {
        this.robotPosition = robotPosition;
        this.orientation = orientation;
    }
}
