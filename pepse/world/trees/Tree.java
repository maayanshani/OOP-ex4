package pepse.world.trees;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.ImageReader;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.Constants;
import pepse.world.Terrain;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Tree extends GameObject {
    private final List<GameObject> leaves = new ArrayList<>();
    private final ImageReader imageReader;
    private GameObject trunk;
    private static final Random seededRandom = new Random(); // Single instance for consistency
    private final Vector2 coordinates;

    public Tree(ImageReader imageReader, Vector2 coordinates) {
        super(coordinates, Vector2.ZERO, null);
        this.imageReader = imageReader;
        this.coordinates = coordinates;

        // set Random seed:
        seededRandom.setSeed(Constants.RANDOM_SEED);

        // create trunk:
        createTrunk();

        // create leaves:
        createLeaves();

    }

    private void createTrunk() {
        Renderable trunkImage = imageReader.readImage(Constants.TRUNK_IMAGE_PATH, true);
        seededRandom.setSeed(Constants.RANDOM_SEED);
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
                if (seededRandom.nextFloat() < 0.8) { // 80% chance to create a leaf
                    Vector2 curLeafCoordinate = new Vector2(
                            leavesStartX + i * (Constants.LEAF_SIZE + Constants.LEAF_SPACE),
                            this.trunk.getCenter().y() - j * (Constants.LEAF_SIZE + Constants.LEAF_SPACE));
                    GameObject curLeaf = Leaf.create(imageReader, curLeafCoordinate);

                    leaves.add(curLeaf);
                }
            }

        }

    }


    public GameObject getTrunk() {
        return trunk;
    }

    public List<GameObject> getLeaves() {
        return leaves;
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
                if (seededRandom.nextFloat() < 0.8) { // 80% chance to create a leaf
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
