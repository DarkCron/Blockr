package gameworld;

import com.robotgame.Orientation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.HashMap;
import java.util.Map;

public class OrientationTest {

    private Map<Orientation, Orientation> TURN_LEFT = new HashMap<>() {{
        put(Orientation.NORTH, Orientation.WEST);
        put(Orientation.WEST, Orientation.SOUTH);
        put(Orientation.SOUTH, Orientation.EAST);
        put(Orientation.EAST, Orientation.NORTH);
    }};

    private Map<Orientation, Orientation> TURN_RIGHT = new HashMap<>() {{
        put(Orientation.NORTH, Orientation.EAST);
        put(Orientation.EAST, Orientation.SOUTH);
        put(Orientation.SOUTH, Orientation.WEST);
        put(Orientation.WEST, Orientation.NORTH);
    }};

    @ParameterizedTest
    @EnumSource(Orientation.class)
    public void turnLeft(Orientation orientation){
        Assertions.assertEquals(TURN_LEFT.get(orientation), orientation.turnLeft());
    }

    @ParameterizedTest
    @EnumSource(Orientation.class)
    public void turnRight(Orientation orientation){
        Assertions.assertEquals(TURN_RIGHT.get(orientation), orientation.turnRight());
    }

}
