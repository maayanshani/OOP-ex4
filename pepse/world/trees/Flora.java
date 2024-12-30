package pepse.world.trees;

import pepse.world.Block;

import java.util.ArrayList;
import java.util.List;

public class Flora {
    public List<Tree> createInRange(int minX, int maxX) {
        // TODO: maybe get callback instead of creat Terrain?
//        Terrain terrain = new Terrain(windowDimensions, Constants.RANDOM_SEED);

        List<Tree> trees = new ArrayList<Tree>();
        // TODO: change from block to trees
        // Round minX and maxX to bounds divisible by Block.SIZE
        int adjustedMinX = (int) Math.floor((double) minX / Block.SIZE) * Block.SIZE;
        int adjustedMaxX = (int) Math.ceil((double) maxX / Block.SIZE) * Block.SIZE;

        // Create blocks along the X range
        for (int x = adjustedMinX; x <= adjustedMaxX; x += Block.SIZE) {
        }
        return trees;
    }

}
