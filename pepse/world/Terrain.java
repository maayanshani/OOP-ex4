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

public class Terrain {
    private final NoiseGenerator noiseGenerator;
    private final int seed;
    private final int groundHeightAtX0;

    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    private static final int TERRAIN_DEPTH = 20;

    public Terrain(Vector2 windowDimensions, int seed) {
        this.groundHeightAtX0 = (int) (windowDimensions.y() * Constants.SCALE_HEIGHT_X0);
        this.seed = seed;

        // Initialize NoiseGenerator with the seed and starting point
        noiseGenerator = new NoiseGenerator(seed, Constants.NOISE_GENERATOR_START_POINT);
    }

    public float groundHeightAt(float x) {
        return (float) (groundHeightAtX0 + noiseGenerator.noise(x, Constants.NOISE_FACTOR));
    }

    public List<Block> createInRange(int minX, int maxX) {
        List<Block> blockList = new ArrayList<>();
        Renderable renderable = new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));

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
                Block block = new Block(new Vector2(x, blockY), renderable);
                block.setTag(Constants.GROUND);
                blockList.add(block);
            }
        }
        return blockList;
    }
}