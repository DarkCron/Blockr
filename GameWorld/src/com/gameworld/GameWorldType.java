package com.gameworld;

import java.util.Set;

public interface GameWorldType {

    Set<Action> getSupportedActions();
    Set<Predicate> getSupportedPredicates();

    GameWorld create();
}
