package pepse.world.trees;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.util.Vector2;
import pepse.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a Tree object in the game world.
 * A tree consists of a trunk, leaves, and fruits.
 */
public class Tree extends GameObject {

    /** A list of leaves attached to the tree. */
    private final List<GameObject> leaves = new ArrayList<>();
    /** A list of fruits attached to the tree. */
    private final List<Fruit> fruits = new ArrayList<>();
    /** ImageReader instance for rendering images for the tree components. */
    private final ImageReader imageReader;
    /** Represents the trunk of the tree. */
    private Trunk trunk;
    /** A single Random instance for consistent randomization of tree properties. */
    private static final Random seededRandom = new Random();
    /** The top-left corner coordinates of the tree. */
    private Vector2 coordinates;

    /**
     * Constructs a Tree object at the given coordinates.
     * Initializes the trunk, leaves, and fruits based on randomized properties.
     *
     * @param imageReader An ImageReader instance to load images for tree components.
     * @param coordinates The top-left corner coordinates of the tree in the game world.
     */
    public Tree(ImageReader imageReader,
                Vector2 coordinates) {
        super(coordinates, Vector2.ZERO, null);
        this.imageReader = imageReader;
        this.coordinates = coordinates;

        // Set Random seed:
        seededRandom.setSeed(Constants.RANDOM_SEED);

        // Create trunk:
        createTrunk();

        // Create leaves and fruits:
        createLeavesAndFruits();

    }

    /**
     * Creates the trunk of the tree with randomized height.
     * Sets the dimensions of the Tree object to match the trunk height.
     */
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

    /**
     * Creates leaves and fruits for the tree.
     * Leaves are created with a higher probability than fruits, and their positions are randomized.
     */
    private void createLeavesAndFruits() {
        float leavesWidth = Constants.NUM_OF_LEAVES_IN_ROW * (Constants.LEAF_SIZE + Constants.LEAF_SPACE);
        float leavesStartX = trunk.getCenter().x() - leavesWidth / 2;

        for (int i = 0; i < Constants.NUM_OF_LEAVES_IN_ROW; i++) {
            for (int j = 0; j < Constants.NUM_OF_LEAVES_IN_ROW; j++) {

                Vector2 curCoordinate = new Vector2(
                        leavesStartX + i * (Constants.LEAF_SIZE + Constants.LEAF_SPACE),
                        this.trunk.getCenter().y() - j * (Constants.LEAF_SIZE + Constants.LEAF_SPACE));

                if (seededRandom.nextFloat() < Constants.LEAF_THRESHOLD) { // 80% chance to create a leaf
                    GameObject curLeaf = Leaf.create(curCoordinate);
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


    /**
     * Retrieves the trunk of the tree.
     *
     * @return The trunk GameObject of the tree.
     */
    public GameObject getTrunk() {
        return trunk;
    }

    /**
     * Retrieves the list of leaves attached to the tree.
     *
     * @return A list of GameObjects representing the leaves.
     */
    public List<GameObject> getLeaves() {
        return leaves;
    }

    /**
     * Retrieves the list of fruits attached to the tree.
     *
     * @return A list of Fruits.
     */
    public List<Fruit> getFruits() {
        return fruits;
    }

    /**
     * Updates the center of the tree and its components (trunk, leaves, fruits).
     *
     * @param center The new center position of the tree.
     */
    @Override
    public void setCenter(Vector2 center) {
        Vector2 offset = center.subtract(trunk.getCenter());
        trunk.setCenter(trunk.getCenter().add(offset));

        for (GameObject leaf : leaves) {
            leaf.setCenter(leaf.getCenter().add(offset));
        }
        for (Fruit fruit : fruits) {
            fruit.setCenter(fruit.getCenter().add(offset));
        }

        super.setCenter(center);
    }

    /**
     * Updates the top-left corner of the tree and its components (trunk, leaves, fruits).
     *
     * @param topLeftCorner The new top-left corner position of the tree.
     */
    @Override
    public void setTopLeftCorner(Vector2 topLeftCorner) {
        Vector2 offset = topLeftCorner.subtract(trunk.getTopLeftCorner());
        trunk.setTopLeftCorner(topLeftCorner);

        for (GameObject leaf : leaves) {
            leaf.setTopLeftCorner(leaf.getTopLeftCorner().add(offset));
        }
        for (Fruit fruit : fruits) {
            fruit.setTopLeftCorner(fruit.getTopLeftCorner().add(offset));
        }

        super.setTopLeftCorner(topLeftCorner);
        this.coordinates = topLeftCorner;
    }
}
