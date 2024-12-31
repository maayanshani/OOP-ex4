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
//    // TODO: where is the right place to put it?
//    @FunctionalInterface
//    public interface ObjectRemoveFunction {
//        void apply(GameObject x);
//    }

    private final List<GameObject> leaves = new ArrayList<>();
    private final List<GameObject> fruits = new ArrayList<>();
    private final ImageReader imageReader;
    private GameObject trunk;
    private static final Random seededRandom = new Random(); // Single instance for consistency
    private final Vector2 coordinates;
    private PepseGameManager.ObjectRemoveFunction objectRemoveFunction;


    public Tree(ImageReader imageReader, Vector2 coordinates, PepseGameManager.ObjectRemoveFunction function) {
        super(coordinates, Vector2.ZERO, null);
        this.imageReader = imageReader;
        this.coordinates = coordinates;
        this.objectRemoveFunction = function;

        // set Random seed:
        seededRandom.setSeed(Constants.RANDOM_SEED);

        // create trunk:
        createTrunk();

        // create leaves:
        createLeaves();

        // create fruits:
        createFruits();

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
        trunk.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        trunk.setTag(Constants.TREE);
        this.setDimensions(trunk.getDimensions());
        this.trunk = trunk;

    }


    private void createLeaves() {
        float leavesWidth = Constants.NUM_OF_LEAVES_IN_ROW * (Constants.LEAF_SIZE + Constants.LEAF_SPACE);
        float leavesStartX = trunk.getCenter().x() - leavesWidth / 2;

        for (int i = 0; i < Constants.NUM_OF_LEAVES_IN_ROW; i++) {
            for (int j = 0; j < Constants.NUM_OF_LEAVES_IN_ROW; j++) {
                if (seededRandom.nextFloat() < Constants.LEEF_THRESHOLD) { // 80% chance to create a leaf
                    Vector2 curLeafCoordinate = new Vector2(
                            leavesStartX + i * (Constants.LEAF_SIZE + Constants.LEAF_SPACE),
                            this.trunk.getCenter().y() - j * (Constants.LEAF_SIZE + Constants.LEAF_SPACE));
                    GameObject curLeaf = Leaf.create(imageReader, curLeafCoordinate);

                    leaves.add(curLeaf);
                }
            }

        }

    }

    private void createFruits() {
        float leavesWidth = Constants.NUM_OF_LEAVES_IN_ROW * (Constants.LEAF_SIZE + Constants.LEAF_SPACE);
        float leavesStartX = trunk.getCenter().x() - leavesWidth / 2;
        float leavesStartY = trunk.getCenter().y() - leavesWidth / 2;


        // get random num of fruits on the tree:
        int numFruits = seededRandom.nextInt(Constants.MIN_FRUITS_ON_A_TREE, Constants.MAX_FRUITS_ON_A_TREE);
        for (int i = 0; i < numFruits; i++) {
            // get random location:
            float fruitX = seededRandom.nextFloat(leavesStartX, leavesStartX+leavesWidth);
            float fruitY = seededRandom.nextFloat(leavesStartY, leavesStartY+leavesWidth);
            GameObject fruit = new Fruit(imageReader, new Vector2(fruitX, fruitY), objectRemoveFunction);

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

    public static GameObject create(ImageReader imageReader, Vector2 coordinate) {

        // create trunk:
        Renderable trunkImage = imageReader.readImage(Constants.TRUNK_IMAGE_PATH, true);
        seededRandom.setSeed(Constants.RANDOM_SEED);
        float treeTrunkHeight = seededRandom.nextFloat(
                Constants.TREE_TRUNK_HEIGHT_MIN, Constants.TREE_TRUNK_HEIGHT_MAX);
        GameObject tree = new GameObject(
                coordinate,
                new Vector2(Constants.TREE_TRUNK_WIDTH, treeTrunkHeight),
                trunkImage);
//                new RectangleRenderable(Constants.TRUNK_COLOR));
        tree.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        tree.setTag(Constants.TREE);

        // create leaves:
        for (int i = 0; i < Constants.NUM_OF_LEAVES_IN_ROW; i++) {
            for (int j = 0; j < Constants.NUM_OF_LEAVES_IN_ROW; j++) {
                if (seededRandom.nextFloat() < Constants.LEEF_THRESHOLD) { // 80% chance to create a leaf
                    Vector2 curLeafCoordinate = new Vector2(
                            coordinate.x() + i * Constants.LEAF_SIZE,
                            coordinate.y() - j * Constants.LEAF_SIZE);
                    GameObject curLeaf = Leaf.create(imageReader, curLeafCoordinate);
                }
            }

        }

        return tree;
    }
}
