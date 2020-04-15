package com.gameworld;

import java.awt.*;

public interface GameWorld {

    ExecutionResult execute(Action action);

    boolean evaluate(Predicate predicate);

    void draw(Graphics graphics);

    GameWorldSnapshot takeSnapshot();

    void restoreSnapshot(GameWorldSnapshot gameWorldSnapshot);

    enum ExecutionResult {
        Sucesss,
        Failure,
        EndOfGame
    }
}