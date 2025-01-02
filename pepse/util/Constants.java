package pepse.util;

import danogl.collisions.Layer;

import java.awt.*;
import java.util.List;

/**
 * A utility class to store commonly used constants throughout the project.
 *
 * <h2>How to Use</h2>
 * <p>You can access the constants using the class name directly, as they are
 * declared as <code>public static final</code>. Example usage:</p>
 *
 * <pre>
 * // Access a string constant
 * String example = Constants.EXAMPLE;
 *
 * // Use a numeric constant
 * int doubledValue = Constants.NUM_EXAMPLE * 2;
 *
 * // Use a boolean constant
 * if (Constants.DEBUG_MODE) {
 *     System.out.println("Debug mode is enabled.");
 * }
 * </pre>
 */
public final class Constants {

    /**
     * TODO:1. before assignment delete what we didnt use
     */

    /** String constants */
    public static final String
            AVATAR     = "avatar",
            GROUND     = "ground",
            SKY        = "sky",
            CLOUD      = "cloud",
            SUN        = "sun",
            SUN_HALO   = "sunHalo",
            TREE       = "tree",
            LEAF       = "leaf",
            FRUIT      = "fruit",
            NIGHT      = "night",
            RUN        = "run",
            JUMP       = "jump",
            IDLE       = "idle",
            WATER_DROP = "water drop";

    // image paths
    public static final String
            AVATAR_IDLE_PATH_FORMAT = "assets/idle_%d.png",
            AVATAR_RUN_PATH_FORMAT = "assets/run_%d.png",
            AVATAR_JUMP_PATH_FORMAT = "assets/jump_%d.png",
            TRUNK_IMAGE_PATH = "assets/trunk.png",
            FRUIT_IMAGE_PATH = "assets/newapple.png",
            WATER_DROP_IMAGE_PATH = "assets/waterDrop.png";

    // System out
    public static final String
            ENERGY_TEXT_FORMAT = "Energy: %.2f";

    /** Numeric constants */
    public static final int
            RANDOM_SEED = 42,
            NOISE_FACTOR = 200,
            NOISE_GENERATOR_START_POINT = 100,
            SUN_SIZE = 180,
            WATER_DROP_SIZE = 20,
            TREE_TRUNK_WIDTH = 100,
            TREE_TRUNK_HEIGHT_MIN = 200,
            TREE_TRUNK_HEIGHT_MAX = 400,
            NUM_OF_LEAVES_IN_ROW = 7,
            MIN_FRUITS_ON_A_TREE = 1,
            MAX_FRUITS_ON_A_TREE = 3,
            FRUIT_ENERGY = 10,
            DAY_LONG = 30,
            CLOUD_CYCLE = 30,
            DROP_VEL_CYCLE = 8,
            DROP_OPACITY_CYCLE = 4,
            ENERGY_TEXT_LOCATION = 20,
            ENERGY_TEXT_SIZE = 30,
            NUM_IDLE_FRAMES = 4,
            NUM_RUN_FRAMES = 6,
            NUM_JUMP_FRAMES = 4,
            AVATAR_SIZE = 50,
            COLOR_VARIATION = 10,
            COLOR_MIN = 0,
            COLOR_MAX = 255,
            WATER_DROP_FALL_DISTANCE = 1000,
            MAX_NUM_DROPS = 10,
            DROP_LOC_X_VARIATION = 100,
            DROP_LOC_Y_VARIATION = 50;



    public static final float
            MAX_ENERGY = 100,
            IDLE_ENERGY = 1,
            RUN_ENERGY = 0.5F,
            JUMP_ENERGY = 10,
            SUN_ANGLE_MAX = 360,
            SUN_ANGLE_MIN = 0,
            LEAF_ANGLE_MAX = 25,
            LEAF_ANGLE_MIN = 0,
            LEAF_MOVES_TIME = 2f,
            LEAF_MAX_WAIT_TIME = 1f,
            SCALE_HEIGHT_X0 = 2f / 3,
            HALO_MULT_RATIO = 1.5f,
            MIDNIGHT_OPACITY = 0.5f,
            LEAF_SIZE = 25,
            FRUIT_SIZE = 25,
            LEAF_WIDTH_MIN = LEAF_SIZE*0.75f,
            LEAF_SPACE = 2,
            TREE_THRESHOLD = 0.1f,
            LEEF_THRESHOLD = 0.8f,
            FRUIT_THRESHOLD = 0.1f,
            CLOUD_LOCATION_X_RATIO = 0.1f,
            WATER_DROP_FINAL_OPACITY = 0F,
            HALF = 0.5f;


    /** Layers constants */
    public static final int
            SKY_LAYER = Layer.BACKGROUND,
            SUN_HALO_LAYER = Layer.STATIC_OBJECTS-30,
            SUN_LAYER = Layer.STATIC_OBJECTS-20,
            CLOUD_LAYER = Layer.STATIC_OBJECTS-15,
            TREES_TRUNKS_LAYER = Layer.STATIC_OBJECTS-20,
            TREE_LEAVES_LAYER = Layer.STATIC_OBJECTS-10,
            GROUND_LAYER = Layer.STATIC_OBJECTS,
//            FRUITS_LAYER = Layer.STATIC_OBJECTS+50,
            // TODO: fruits must be static so the collision will work
            FRUITS_LAYER = Layer.STATIC_OBJECTS,
            // TODO: we wanted the avatar and the ground on the same layer?
            //  + maybe change it to "main layer"?
            AVATAR_LAYER = Layer.DEFAULT,
            NIGHT_LAYER = Layer.DEFAULT+10,
            ENERGY_LAYER = Layer.UI;

    /** Colors constants */
    public static final Color
            BASIC_SKY_COLOR = Color.decode("#80C6E5"),
            SUN_HALO_COLOR = new Color(255, 255, 0, 20),
            TRUNK_COLOR = new Color(100, 50, 20),
            LEAF_COLOR = new Color(50, 200, 30),
            BASE_CLOUD_COLOR = new Color(255, 255, 255);

    public static final java.util.List<java.util.List<Integer>> CLOUD_LAYOUT = java.util.List.of(
            java.util.List.of(0, 0, 1, 1, 0, 0, 0, 0, 0),
            java.util.List.of(0, 1, 1, 1, 0, 0, 1, 0, 0),
            java.util.List.of(1, 1, 1, 1, 1, 1, 1, 1, 0),
            java.util.List.of(1, 1, 1, 1, 1, 1, 1, 1, 1),
            java.util.List.of(0, 1, 1, 1, 1, 1, 1, 1, 0),
            java.util.List.of(0, 0, 1, 1, 1, 1, 1, 0, 0),
            List.of(0, 0, 0, 1, 1, 0, 0, 0, 0)
    );
    public static final int NUM_CLOUD_BLOCKS = CLOUD_LAYOUT.size() * CLOUD_LAYOUT.getFirst().size();



    /** Boolean constants */
    // Add boolean constants as needed here.

    // Private constructor to prevent instantiation
    private Constants() { }
}
