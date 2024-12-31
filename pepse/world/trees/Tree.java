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

public class Tree extends GameObject {

    private final List<GameObject> leaves = new ArrayList<>();
    private final List<GameObject> fruits = new ArrayList<>();
    private final ImageReader imageReader;
    private GameObject trunk;
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
        Renderable trunkImage = imageReader.readImage(Constants.TRUNK_IMAGE_PATH, true);
        float treeTrunkHeight = seededRandom.nextFloat(
                Constants.TREE_TRUNK_HEIGHT_MIN, Constants.TREE_TRUNK_HEIGHT_MAX);
        GameObject trunk = new GameObject(
                coordinates,
                new Vector2(Constants.TREE_TRUNK_WIDTH, treeTrunkHeight),
                trunkImage);
//                new RectangleRenderable(Constants.TRUNK_COLOR));
        trunk.setTag(Constants.TREE);
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

    // TODO: this is wrong!
    @Override
    public void setCenter(Vector2 center) {
        super.setCenter(center);
        this.coordinates = center; // If we are using center, update coordinates accordingly

        // Update the trunk's position
        trunk.setCenter(center);

        // Update the leaves' positions
        for (GameObject leaf : leaves) {
            Vector2 leafOffset = leaf.getCenter().subtract(trunk.getCenter());
            leaf.setCenter(center.add(leafOffset));
        }

        // Update the fruits' positions
        for (GameObject fruit : fruits) {
            Vector2 fruitOffset = fruit.getCenter().subtract(trunk.getCenter());
            fruit.setCenter(center.add(fruitOffset));
        }
    }

    // TODO: this is wrong!
    @Override
    public void setTopLeftCorner(Vector2 topLeftCorner) {
        super.setTopLeftCorner(topLeftCorner);
        this.coordinates = topLeftCorner; // If we are using topLeftCorner, update coordinates accordingly

        // Update the trunk's position
        trunk.setTopLeftCorner(topLeftCorner);

        // Update the leaves' positions
        for (GameObject leaf : leaves) {
            Vector2 leafOffset = leaf.getTopLeftCorner().subtract(trunk.getTopLeftCorner());
            leaf.setTopLeftCorner(topLeftCorner.add(leafOffset));
        }

        // Update the fruits' positions
        for (GameObject fruit : fruits) {
            Vector2 fruitOffset = fruit.getTopLeftCorner().subtract(trunk.getTopLeftCorner());
            fruit.setTopLeftCorner(topLeftCorner.add(fruitOffset));
        }
    }

    @Override
    public Vector2 getDimensions() {
        // Get the dimensions of the trunk
        Vector2 trunkDimensions = trunk.getDimensions();

        // Define a variable to track the max width and height
        float maxWidth = trunkDimensions.x();
        float maxHeight = trunkDimensions.y();

        // Calculate the overall width based on leaves
        for (GameObject leaf : leaves) {
            maxWidth = Math.max(maxWidth, leaf.getCenter().x() + leaf.getDimensions().x() / 2);
        }

        // Calculate the overall height based on leaves and fruits (adjust as needed)
        for (GameObject leaf : leaves) {
            maxHeight = Math.max(maxHeight, leaf.getCenter().y() + leaf.getDimensions().y() / 2);
        }
        for (GameObject fruit : fruits) {
            maxHeight = Math.max(maxHeight, fruit.getCenter().y() + fruit.getDimensions().y() / 2);
        }

        // Return the overall dimensions considering trunk, leaves, and fruits
        return new Vector2(maxWidth, maxHeight);
    }

}
