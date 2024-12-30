package pepse.world.trees;

import pepse.world.Block;
import pepse.world.Terrain;

import java.util.ArrayList;
import java.util.List;

public class Flora {
    @FunctionalInterface
    public interface FloatFunction {
        float apply(float x);
    }

    public List<Tree> createInRange(int minX, int maxX, FunctionalInterface curHeightGetter) {

        List<Tree> trees = new ArrayList<Tree>();
        // TODO: change from block to trees
        // Round minX and maxX to bounds divisible by Block.SIZE
        int adjustedMinX = (int) Math.floor((double) minX / Block.SIZE) * Block.SIZE;
        int adjustedMaxX = (int) Math.ceil((double) maxX / Block.SIZE) * Block.SIZE;

        // Create blocks along the X range
        for (int x = adjustedMinX; x <= adjustedMaxX; x += Block.SIZE) {
//            float curHeight = curHeightGetter.apply(x);

        }
        return trees;
    }

}
