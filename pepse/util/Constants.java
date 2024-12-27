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
            TERRAIN    = "terrain",
            SUN        = "sun",
            SUN_HALO   = "sunHalo",
            NIGHT      = "night";

    public static final String AVATAR_IMAGE_PATH = "assets/idle_0.png";

    /** Numeric constants */
    public static final int
            NUM_EXAMPLE = 1,

            RANDOM_SEED = 42,

            NOISE_FACTOR = 200,
            NOISE_GENERATOR_START_POINT = 100,

            SUN_SIZE = 300,
            DAY_LONG = 30;



    /** Layers constants */
    public static final int
             SKY_LAYER = Layer.BACKGROUND,
             SUN_HALO_LAYER = Layer.STATIC_OBJECTS,
             SUN_LAYER = Layer.STATIC_OBJECTS+1,
             CLOUDS_LAYER = Layer.STATIC_OBJECTS+2,
             TREES_TRUNKS_LAYER = Layer.STATIC_OBJECTS+3,
             TREE_LEAVES_LAYER = Layer.STATIC_OBJECTS+4,
             AVATAR_LAYER = Layer.DEFAULT,
             NIGHT_LAYER = Layer.DEFAULT+1,
             ENERGY_LAYER = Layer.UI;

    /** Colors constants */
    public static final Color BASIC_SKY_COLOR = Color.decode("#80C6E5");


    /** Numeric constants */
    public static final float
            SCALE_HEIGHT_X0 = (float) 2 / 3,

            HALF = 0.5f,
            QUARTER = 0.25f;

    public static final Float MIDNIGHT_OPACITY = 0.5f;


    /** Boolean constants */
    // Add boolean constants as needed here.

    // Private constructor to prevent instantiation
    private Constants() { }
}
