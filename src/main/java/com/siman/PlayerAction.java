package main.java.com.siman;

/**
 * Created by siman on 1/3/16.
 */
public class PlayerAction {
    static final int NO_ACTION = 0;    // No action
    static final int ATTACK_SOUTH = 1;  // attack to the south
    static final int ATTACK_EAST = 2;   // attack to the east
    static final int ATTACK_NORTH = 3;  // attack to the north
    static final int ATTACK_WEST = 4;   // attack to the west
    static final int MOVE_SOUTH = 5;    // move to the south
    static final int MOVE_EAST = 6;     // move to the east
    static final int MOVE_NORTH = 7;    // move to the north
    static final int MOVE_WEST = 8;     // move to the west
    static final int HIDE = 9;          // hide
    static final int SHOW_UP = 10;      // show up

    static public int[] COST = {0, 4, 4, 4, 4, 2, 2, 2, 2, 1, 1};

    /**
     * All player action pattern
     */
    public static final int[][] PATTERN_LIST = {
            // No action (1 pattern)
            {NO_ACTION},   // ( 0, 0)

            // one move (4 pattern)
            {MOVE_SOUTH},  // ( 1, 0)
            {MOVE_EAST},   // ( 0, 1)
            {MOVE_NORTH},  // (-1, 0)
            {MOVE_WEST},   // { 0,-1)

            // two move (14 pattern)
            {MOVE_SOUTH, MOVE_SOUTH}, // ( 2, 0)

            {MOVE_SOUTH, MOVE_EAST},  // ( 1, 1)
            {MOVE_EAST, MOVE_SOUTH},  // ( 1, 1)

            {MOVE_SOUTH, MOVE_WEST},  // ( 1,-1)
            {MOVE_WEST, MOVE_SOUTH},  // ( 1,-1)

            {MOVE_EAST, MOVE_EAST},   // ( 0, 2)

            {MOVE_EAST, MOVE_NORTH},  // (-1, 1)
            {MOVE_NORTH, MOVE_EAST},  // (-1, 1)

            {MOVE_NORTH, MOVE_NORTH}, // {-2, 0)

            {MOVE_NORTH, MOVE_WEST},  // {-1,-1)
            {MOVE_WEST, MOVE_NORTH},  // {-1,-1)

            {MOVE_WEST, MOVE_WEST},   // { 0,-2)

            // three move (24 pattern)
            {MOVE_SOUTH, MOVE_SOUTH, MOVE_SOUTH}, // ( 3, 0)

            {MOVE_EAST, MOVE_SOUTH, MOVE_SOUTH},  // ( 2, 1)
            {MOVE_SOUTH, MOVE_EAST, MOVE_SOUTH},  // ( 2, 1)
            {MOVE_SOUTH, MOVE_SOUTH, MOVE_EAST},  // ( 2, 1)

            {MOVE_WEST, MOVE_SOUTH, MOVE_SOUTH},  // ( 2,-1)
            {MOVE_SOUTH, MOVE_WEST, MOVE_SOUTH},  // ( 2,-1)
            {MOVE_SOUTH, MOVE_SOUTH, MOVE_WEST},  // ( 2,-1)

            {MOVE_SOUTH, MOVE_EAST, MOVE_EAST},   // { 1, 2)
            {MOVE_EAST, MOVE_SOUTH, MOVE_EAST},   // { 1, 2)
            {MOVE_EAST, MOVE_EAST, MOVE_SOUTH},   // { 1, 2)

            {MOVE_SOUTH, MOVE_WEST, MOVE_WEST},   // ( 1,-2)
            {MOVE_WEST, MOVE_SOUTH, MOVE_WEST},   // ( 1,-2)
            {MOVE_WEST, MOVE_WEST, MOVE_SOUTH},   // ( 1,-2)

            {MOVE_EAST, MOVE_EAST, MOVE_EAST},    // ( 0, 3)

            {MOVE_NORTH, MOVE_EAST, MOVE_EAST},   // (-1, 2)
            {MOVE_EAST, MOVE_NORTH, MOVE_EAST},   // (-1, 2)
            {MOVE_EAST, MOVE_EAST, MOVE_NORTH},   // (-1, 2)

            {MOVE_EAST, MOVE_NORTH, MOVE_NORTH},  // (-2, 1)
            {MOVE_NORTH, MOVE_EAST, MOVE_NORTH},  // (-2, 1)
            {MOVE_NORTH, MOVE_NORTH, MOVE_EAST},  // (-2, 1)

            {MOVE_NORTH, MOVE_NORTH, MOVE_NORTH}, // (-3, 0)

            {MOVE_WEST, MOVE_NORTH, MOVE_NORTH},  // (-2,-1)
            {MOVE_NORTH, MOVE_WEST, MOVE_NORTH},  // (-2,-1)
            {MOVE_NORTH, MOVE_NORTH, MOVE_WEST},  // (-2,-1)

            {MOVE_NORTH, MOVE_WEST, MOVE_WEST},   // (-1,-2)
            {MOVE_WEST, MOVE_NORTH, MOVE_WEST},   // (-1,-2)
            {MOVE_WEST, MOVE_WEST, MOVE_NORTH},   // (-1,-2)

            {MOVE_WEST, MOVE_WEST, MOVE_WEST},    // ( 0,-3)

            // Attack only (4 pattern)
            {ATTACK_SOUTH},
            {ATTACK_EAST},
            {ATTACK_NORTH},
            {ATTACK_WEST},

            // Move and Attack(16 pattern)
            {MOVE_SOUTH, ATTACK_SOUTH},
            {MOVE_SOUTH, ATTACK_EAST},
            {MOVE_SOUTH, ATTACK_NORTH},
            {MOVE_SOUTH, ATTACK_WEST},

            {MOVE_EAST, ATTACK_SOUTH},
            {MOVE_EAST, ATTACK_EAST},
            {MOVE_EAST, ATTACK_NORTH},
            {MOVE_EAST, ATTACK_WEST},

            {MOVE_NORTH, ATTACK_SOUTH},
            {MOVE_NORTH, ATTACK_EAST},
            {MOVE_NORTH, ATTACK_NORTH},
            {MOVE_NORTH, ATTACK_WEST},

            {MOVE_WEST, ATTACK_SOUTH},
            {MOVE_WEST, ATTACK_EAST},
            {MOVE_WEST, ATTACK_NORTH},
            {MOVE_WEST, ATTACK_WEST},

            // Attack and Move(16 pattern)
            {ATTACK_SOUTH, MOVE_SOUTH},
            {ATTACK_EAST, MOVE_SOUTH},
            {ATTACK_NORTH, MOVE_SOUTH},
            {ATTACK_WEST, MOVE_SOUTH},

            {ATTACK_SOUTH, MOVE_EAST},
            {ATTACK_EAST, MOVE_EAST},
            {ATTACK_NORTH, MOVE_EAST},
            {ATTACK_WEST, MOVE_EAST},

            {ATTACK_SOUTH, MOVE_NORTH},
            {ATTACK_EAST, MOVE_NORTH},
            {ATTACK_NORTH, MOVE_NORTH},
            {ATTACK_WEST, MOVE_NORTH},

            {ATTACK_SOUTH, MOVE_WEST},
            {ATTACK_EAST, MOVE_WEST},
            {ATTACK_NORTH, MOVE_WEST},
            {ATTACK_WEST, MOVE_WEST},
    };
}
