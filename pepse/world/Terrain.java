/**
 * Represents the terrain in the game world, handling the creation of blocks based on noise generation.
 * The terrain includes height calculations and dynamic block generation within a given range.
 */
package pepse.world;

import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.Constants;
import pepse.util.NoiseGenerator;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Todo: what should i do with the seed?
 */

/**
 * Represents the terrain in the game world, managing the creation and layout of blocks
 * that form the ground. The terrain dynamically generates blocks based on a noise function,
 * providing a natural and varied appearance.
 *
 * - The terrain height is calculated using a Perlin noise generator.
 * - Blocks are created in columns to represent the ground and its depth.
 * - The terrain supports dynamic generation for a specified horizontal range.
 *
 * This class allows for easy integration of procedurally generated ground into the game.
 */
public class Terrain {
    /** The noise generator used to create random but smooth terrain heights. */
    private final NoiseGenerator noiseGenerator;

    /** The seed used for initializing the noise generator, ensuring consistent terrain generation. */
    private final int seed;

    /** The base height of the ground at the x = 0 position. */
    private final int groundHeightAtX0;

    /** The base color of the terrain blocks. */
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);

    /** The depth of the terrain in terms of the number of blocks below the surface. */
    private static final int TERRAIN_DEPTH = 20;

    /** Random number generator for color variation. */
    private static final Random RANDOM = new Random();

    /**
     * Constructs a new Terrain object.
     *
     * @param windowDimensions The dimensions of the game window, used to calculate initial ground height.
     * @param seed The seed for noise generation to ensure consistent terrain structure.
     */
    public Terrain(Vector2 windowDimensions, int seed) {
        this.groundHeightAtX0 = (int) (windowDimensions.y() * Constants.SCALE_HEIGHT_X0);
        this.seed = seed;

        // Initialize NoiseGenerator with the seed and starting point
        noiseGenerator = new NoiseGenerator(seed, Constants.NOISE_GENERATOR_START_POINT);
    }

    /**
     * Calculates the ground height at a specific x-coordinate based on noise generation.
     *
     * @param x The x-coordinate to calculate the ground height for.
     * @return The calculated ground height at the specified x-coordinate.
     */
    public float groundHeightAt(float x) {
        return (float) (groundHeightAtX0 + noiseGenerator.noise(x, Constants.NOISE_FACTOR));
    }

    /**
     * Randomizes the color of the terrain blocks slightly by adjusting the RGB values.
     *
     * @return A randomized color based on the base ground color.
     */
    private Color getRandomizedColor() {
        int red = Math.min(255, Math.max(0, BASE_GROUND_COLOR.getRed() + RANDOM.nextInt(21) - 10));
        int green = Math.min(255, Math.max(0, BASE_GROUND_COLOR.getGreen() + RANDOM.nextInt(21) - 10));
        int blue = Math.min(255, Math.max(0, BASE_GROUND_COLOR.getBlue() + RANDOM.nextInt(21) - 10));
        return new Color(red, green, blue);
    }

    /**
     * Creates a list of blocks representing the terrain within the specified x-coordinate range.
     *
     * @param minX The minimum x-coordinate of the range.
     * @param maxX The maximum x-coordinate of the range.
     * @return A list of blocks representing the terrain in the specified range.
     */
    public List<Block> createInRange(int minX, int maxX) {
        List<Block> blockList = new ArrayList<>();

        // Round minX and maxX to bounds divisible by Block.SIZE
        int adjustedMinX = (int) Math.floor((double) minX / Block.SIZE) * Block.SIZE;
        int adjustedMaxX = (int) Math.ceil((double) maxX / Block.SIZE) * Block.SIZE;

        // Create blocks along the X range
        for (int x = adjustedMinX; x <= adjustedMaxX; x += Block.SIZE) {
            // Calculate the starting height of the top block in the column
            float groundHeight = (float) Math.floor(groundHeightAt(x) / Block.SIZE) * Block.SIZE;

            // Create a column of blocks with a fixed depth (TERRAIN_DEPTH)
            for (int y = 0; y < TERRAIN_DEPTH; y++) {
                int blockY = (int) groundHeight + (y * Block.SIZE);
                Renderable renderable = new RectangleRenderable(ColorSupplier.approximateColor(getRandomizedColor()));
                Block block = new Block(new Vector2(x, blockY), renderable);
                block.setTag(Constants.GROUND);
                blockList.add(block);
            }
        }
        return blockList;
    }
}
