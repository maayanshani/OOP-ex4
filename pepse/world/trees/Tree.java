package pepse.world.trees;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/** todo
 * 1. avatar can jump on trunks
 * 2. trunks hovering above the terrain
 */
public class Tree extends GameObject {

    private final List<GameObject> leaves = new ArrayList<>();
    private final List<GameObject> fruits = new ArrayList<>();
    private final ImageReader imageReader;
    private Trunk trunk;
    private static final Random seededRandom = new Random(); // Single instance for consistency
    private Vector2 coordinates;


    public Tree(ImageReader imageReader,
                Vector2 coordinates) {
        super(coordinates, Vector2.ZERO, null);
        this.imageReader = imageReader;
        this.coordinates = coordinates;

        // set Random seed:
        seededRandom.setSeed(Constants.RANDOM_SEED);

        // create trunk:
        createTrunk();

        // create leaves and Fruits:
        createLeavesAndFruits();

        // create fruits:
//        createFruits();

    }

    private void createTrunk() {
        float treeTrunkHeight = seededRandom.nextFloat(
                Constants.TREE_TRUNK_HEIGHT_MIN, Constants.TREE_TRUNK_HEIGHT_MAX);
        Vector2 trunkTopLeft = new Vector2(
                coordinates.x(),
                coordinates.y() - treeTrunkHeight // Adjust for height
        );

        Trunk trunk = new Trunk(imageReader,
                trunkTopLeft,
                new Vector2(Constants.TREE_TRUNK_WIDTH, treeTrunkHeight));

        this.setDimensions(trunk.getDimensions());
        this.trunk = trunk;

    }


    private void createLeavesAndFruits() {
        float leavesWidth = Constants.NUM_OF_LEAVES_IN_ROW * (Constants.LEAF_SIZE + Constants.LEAF_SPACE);
        float leavesStartX = trunk.getCenter().x() - leavesWidth / 2;

        for (int i = 0; i < Constants.NUM_OF_LEAVES_IN_ROW; i++) {
            for (int j = 0; j < Constants.NUM_OF_LEAVES_IN_ROW; j++) {

                Vector2 curCoordinate = new Vector2(
                        leavesStartX + i * (Constants.LEAF_SIZE + Constants.LEAF_SPACE),
                        this.trunk.getCenter().y() - j * (Constants.LEAF_SIZE + Constants.LEAF_SPACE));

                if (seededRandom.nextFloat() < Constants.LEEF_THRESHOLD) { // 80% chance to create a leaf
                    GameObject curLeaf = Leaf.create(imageReader, curCoordinate);
                    leaves.add(curLeaf);

                } else if (seededRandom.nextFloat() < Constants.FRUIT_THRESHOLD) { // 10% chance to create a fruit
                    Fruit fruit = new Fruit(imageReader, curCoordinate);
                    fruits.add(fruit);
                }

            }
        }
        float newHeight = trunk.getDimensions().y() +
                Constants.NUM_OF_LEAVES_IN_ROW * (Constants.LEAF_SIZE + Constants.LEAF_SPACE);
        this.setDimensions(new Vector2(leavesWidth, newHeight));

    }

    // TODO: no need
    private void createFruits() {
        float leavesWidth = Constants.NUM_OF_LEAVES_IN_ROW * (Constants.LEAF_SIZE + Constants.LEAF_SPACE);
        float leavesStartX = trunk.getCenter().x() - leavesWidth * Constants.HALF;
        float leavesStartY = trunk.getCenter().y() - leavesWidth * Constants.HALF;


        // get random num of fruits on the tree:
        int numFruits = seededRandom.nextInt(Constants.MIN_FRUITS_ON_A_TREE, Constants.MAX_FRUITS_ON_A_TREE);
        for (int i = 0; i < numFruits; i++) {
            // get random location:
            float fruitX = seededRandom.nextFloat(leavesStartX, leavesStartX+leavesWidth);
            float fruitY = seededRandom.nextFloat(leavesStartY, leavesStartY+leavesWidth);
            GameObject fruit = new Fruit(imageReader, new Vector2(fruitX, fruitY));

            fruits.add(fruit);
        }

    }


    public GameObject getTrunk() {
        return trunk;
    }

    public List<GameObject> getLeaves() {
        return leaves;
    }

    public List<GameObject> getFruits() {
        return fruits;
    }

    // TODO: this is wrong?
    @Override
    public void setCenter(Vector2 center) {
        Vector2 offset = center.subtract(trunk.getCenter()); // Calculate the movement offset
        trunk.setCenter(trunk.getCenter().add(offset)); // Update the trunk position

        // Update leaves and fruits positions relative to the trunk
        for (GameObject leaf : leaves) {
            leaf.setCenter(leaf.getCenter().add(offset));
        }
        for (GameObject fruit : fruits) {
            fruit.setCenter(fruit.getCenter().add(offset));
        }

        // Update the tree's own coordinates
        super.setCenter(center);
    }


    // TODO: this is wrong?
    @Override
    public void setTopLeftCorner(Vector2 topLeftCorner) {
        Vector2 offset = topLeftCorner.subtract(trunk.getTopLeftCorner()); // Calculate the movement offset
        trunk.setTopLeftCorner(topLeftCorner); // Update the trunk position

        // Update leaves and fruits positions relative to the trunk
        for (GameObject leaf : leaves) {
            leaf.setTopLeftCorner(leaf.getTopLeftCorner().add(offset));
        }
        for (GameObject fruit : fruits) {
            fruit.setTopLeftCorner(fruit.getTopLeftCorner().add(offset));
        }

        // Update the tree's own coordinates
        super.setTopLeftCorner(topLeftCorner);
        this.coordinates = topLeftCorner;
    }


}
