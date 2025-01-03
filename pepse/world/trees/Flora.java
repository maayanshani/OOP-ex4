package pepse.world.trees;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.util.Constants;
import pepse.world.Block;
import pepse.world.Terrain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Flora {
    private final Vector2 windowDimensions;
    private final PepseGameManager.FloatFunction curHeightGetter;
    private final ImageReader imageReader;
    private static final Random seededRandom = new Random(); // Single instance for consistency

    public Flora(Vector2 windowDimensions,
                 PepseGameManager.FloatFunction curHeightGetter,
                 ImageReader imageReader){
        this.windowDimensions = windowDimensions;
        this.curHeightGetter = curHeightGetter;
        this.imageReader = imageReader;

        // set Random seed:
        // TODO: this is the problem!!!
//        seededRandom.setSeed(Constants.RANDOM_SEED);

    }

    private int generateSeedForPosition(Vector2 coordinate) {
        return (int) (coordinate.x() + coordinate.y()) * Constants.RANDOM_SEED;
    }


    public List<Tree> createInRange(int minX, int maxX) {
        List<Tree> trees = new ArrayList<Tree>();
//        Random seededRandom = new Random();
//        seededRandom.setSeed(Constants.RANDOM_SEED);


        int adjustedMinX = (int) Math.floor((double) minX / Block.SIZE) * Block.SIZE;
        int adjustedMaxX = (int) Math.ceil((double) maxX / Block.SIZE) * Block.SIZE;

        // Create trees along the X range
        for (int x = adjustedMinX; x <= adjustedMaxX; x += Block.SIZE) {
            float curTreeY = curHeightGetter.apply(x) + Constants.TRACK_Y_OFFSET ;
            Vector2 treeCor = new Vector2(x, curTreeY);

            int seed = generateSeedForPosition(treeCor);
            seededRandom.setSeed(seed);

            if (seededRandom.nextFloat() < Constants.TREE_THRESHOLD) { // 10% chance to create a leaf
                Tree tree = new Tree(imageReader, treeCor);
//                tree.setTag(Constants.TREE);

//                float treeHeight = tree.getDimensions().y();
//                tree.setTopLeftCorner(new Vector2(x, curTreeY - treeHeight));
                trees.add(tree);
            }
        }
        return trees;
    }


}
