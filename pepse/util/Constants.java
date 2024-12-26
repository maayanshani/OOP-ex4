package pepse.util;

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
            SUN_HALO   = "sunHalo";

    /** Numeric constants */
    public static final int
            NUM_EXAMPLE = 1,
            RANDOM_SEED = 42,
            NOISE_FACTOR = 200,
            NOISE_GENERATOR_START_POINT = 100;

    public static final float SCALE_HEIGHT_X0 = (float) 2 / 3;

    /** Boolean constants */
    // Add boolean constants as needed here.

    // Private constructor to prevent instantiation
    private Constants() { }
}
