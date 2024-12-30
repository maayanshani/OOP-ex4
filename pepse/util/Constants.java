package pepse.util;

import danogl.collisions.Layer;

import java.awt.*;

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
     * TODO: Make sure you add here only constants that are not PRIVATE!
     * There are some constants that should remain inside their respective classes,
     * such as BLOCK_SIZE and BASE_GROUND_COLOR.
     */

    /** String constants */
    public static final String
            EXAMPLE    = "example",
            AVATAR     = "avatar",
            BLOCK      = "block",
            GROUND     = "ground",
            SKY        = "sky",
            CLOUD      = "cloud",
            TERRAIN    = "terrain",
            SUN        = "sun",
            SUN_HALO   = "sunHalo",
            TREE       = "tree",
            LEAF       = "leaf",
            FRUIT      = "fruit",
            NIGHT      = "night",
            RUN        = "run",
            JUMP       = "jump",
            IDLE       = "idle";

    // image paths
    public static final String
            AVATAR_IDLE_PATH_FORMAT = "assets/idle_%d.png",
            AVATAR_RUN_PATH_FORMAT = "assets/run_%d.png",
            AVATAR_JUMP_PATH_FORMAT = "assets/jump_%d.png",
            TRUNK_IMAGE_PATH = "assets/trunk.png";

    // System out
    public static final String
            ENERGY_TEXT_FORMAT = "Energy: %.2f";

    /** Numeric constants */
    public static final int
            NUM_EXAMPLE = 1,
            RANDOM_SEED = 42,
            DOUBLE = 2,

            NOISE_FACTOR = 200,
            NOISE_GENERATOR_START_POINT = 100,
            SUN_SIZE = 180,
            TREE_TRUNK_WIDTH = 100,
            TREE_TRUNK_HEIGHT_MIN = 200,
            TREE_TRUNK_HEIGHT_MAX = 400,
            LEAF_SIZE = 40,
            DAY_LONG = 30,
            CLOUD_CYCLE = 10,
            ENERGY_TEXT_LOCATION = 20,
            ENERGY_TEXT_SIZE = 30,
            NUM_IDLE_FRAMES = 4,
            NUM_RUN_FRAMES = 6,
            NUM_JUMP_FRAMES = 4,
            AVATAR_SIZE = 50,
            COLOR_VARIATION = 10,
            COLOR_MIN = 0,
            COLOR_MAX = 255;



    public static final float
            MAX_ENERGY = 100,
            IDLE_ENERGY = 1,
            RUN_ENERGY = 0.5F,
            JUMP_ENERGY = 10,
            ANGLE_MAX = 360,
            ANGLE_MIN = 0,
            SCALE_HEIGHT_X0 = (float) 2 / 3,
            HALO_MULT_RATIO = 1.5f,

            HALF = 0.5f,
            QUARTER = 0.25f;


    /** Layers constants */
    public static final int
            SKY_LAYER = Layer.BACKGROUND,
            SUN_HALO_LAYER = Layer.STATIC_OBJECTS-2,
            SUN_LAYER = Layer.STATIC_OBJECTS-1,
            GROUND_LAYER = Layer.STATIC_OBJECTS,
            CLOUD_LAYER = Layer.STATIC_OBJECTS+2,
            TREES_TRUNKS_LAYER = Layer.STATIC_OBJECTS+3,
            TREE_LEAVES_LAYER = Layer.STATIC_OBJECTS+4,
            AVATAR_LAYER = Layer.DEFAULT,
            NIGHT_LAYER = Layer.DEFAULT+1,
            ENERGY_LAYER = Layer.UI;

    /** Colors constants */
    public static final Color
            BASIC_SKY_COLOR = Color.decode("#80C6E5"),
            // TODO: is it okay to use const for it?
            SUN_HALO_COLOR = new Color(255, 255, 0, 20),
            TRUNK_COLOR = new Color(100, 50, 20),
            LEAF_COLOR = new Color(50, 200, 30);


    public static final Float MIDNIGHT_OPACITY = 0.5f;


    /** Boolean constants */
    // Add boolean constants as needed here.

    // Private constructor to prevent instantiation
    private Constants() { }
}
