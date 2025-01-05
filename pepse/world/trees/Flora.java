package pepse.world.trees;

import danogl.gui.ImageReader;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.util.Constants;
import pepse.world.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The Flora class handles the creation and management of trees in the game world.
 * It generates trees within a specified range based on predefined probabilities
 * and the current terrain height.
 */
public class Flora {
    /** A function to retrieve the current terrain height for a given x-coordinate. */
    private final PepseGameManager.FloatFunction curHeightGetter;

    /** An ImageReader instance used for loading tree images. */
    private final ImageReader imageReader;

    /** A single static instance of Random to ensure consistent tree generation. */
    private static final Random seededRandom = new Random(); // Single instance for consistency

    /**
     * Constructs a Flora instance responsible for generating trees in the game world.
     *
     * @param curHeightGetter A function to obtain the terrain height at specific x-coordinates.
     * @param imageReader     An ImageReader instance for loading tree-related images.
     */
    public Flora(PepseGameManager.FloatFunction curHeightGetter,
                 ImageReader imageReader){
        this.curHeightGetter = curHeightGetter;
        this.imageReader = imageReader;
    }

    /**
     * Generates a seed value based on a specific position in the game world.
     *
     * @param coordinate The coordinate for which the seed is generated.
     * @return A deterministic integer seed value derived from the coordinate.
     */
    private int generateSeedForPosition(Vector2 coordinate) {
        return (int) (coordinate.x() + coordinate.y()) * Constants.RANDOM_SEED;
    }

    /**
     * Creates trees within a specified range of x-coordinates. The trees' placement is
     * determined by a random chance and adjusted based on the terrain height.
     *
     * @param minX The minimum x-coordinate of the range.
     * @param maxX The maximum x-coordinate of the range.
     * @return A list of Tree objects created within the specified range.
     */
    public List<Tree> createInRange(int minX, int maxX) {
        List<Tree> trees = new ArrayList<Tree>();

        int adjustedMinX = (int) Math.floor((double) minX / Block.SIZE) * Block.SIZE;
        int adjustedMaxX = (int) Math.ceil((double) maxX / Block.SIZE) * Block.SIZE;

        // Create trees along the X range
        for (int x = adjustedMinX; x <= adjustedMaxX; x += Block.SIZE) {
            float curTreeY = curHeightGetter.apply(x) + Constants.TRACK_Y_OFFSET ;
            Vector2 treeCor = new Vector2(x, curTreeY);

            int seed = generateSeedForPosition(treeCor);
            seededRandom.setSeed(seed);

            if (seededRandom.nextFloat() < Constants.TREE_THRESHOLD) { // 10% chance to create a tree
                Tree tree = new Tree(imageReader, treeCor);
                tree.setTag(Constants.TREE);
                trees.add(tree);
            }
        }
        return trees;
    }
}
