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
    private final PepseGameManager.ObjectFunction addObjectFunction;
    private final ImageReader imageReader;
    private static final Random seededRandom = new Random(); // Single instance for consistency

    public Flora(Vector2 windowDimensions,
                 PepseGameManager.FloatFunction curHeightGetter,
                 PepseGameManager.ObjectFunction addObjectFunction,
                 ImageReader imageReader){
        this.windowDimensions = windowDimensions;
        this.curHeightGetter = curHeightGetter;
        this.addObjectFunction = addObjectFunction;
        this.imageReader = imageReader;

        // set Random seed:
        seededRandom.setSeed(Constants.RANDOM_SEED);

    }

    public List<Tree> createInRange(int minX, int maxX) {

        List<Tree> trees = new ArrayList<Tree>();
        // TODO: change from block to trees
        // Round minX and maxX to bounds divisible by Block.SIZE
        int adjustedMinX = (int) Math.floor((double) minX / Block.SIZE) * Block.SIZE;
        int adjustedMaxX = (int) Math.ceil((double) maxX / Block.SIZE) * Block.SIZE;

        // Create trees along the X range
        for (int x = adjustedMinX; x <= adjustedMaxX; x += Block.SIZE) {
            if (seededRandom.nextFloat() < Constants.TREE_THRESHOLD) { // 10% chance to create a leaf
                // TODO: fix this using the tree hight
                float curTreeY = this.windowDimensions.y() - curHeightGetter.apply(x) ;
                Tree tree = new Tree(imageReader, new Vector2(x, curTreeY));
//                float treeHeight = tree.getDimensions().y();
//                tree.setTopLeftCorner(new Vector2(x, curTreeY));
                trees.add(tree);
            }
        }
        return trees;
    }


}
