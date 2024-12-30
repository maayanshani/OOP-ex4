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
import java.util.Random;

public class Tree {
    private static final Random seededRandom = new Random(); // Single instance for consistency

    public static GameObject create(ImageReader imageReader, Vector2 coordinate) {
        // TODO: maybe get callback instead of creat Terrain?
//        Terrain terrain = new Terrain(windowDimensions, Constants.RANDOM_SEED);

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

        // add transition, no need to use the object created
//        float cycleCenterX = windowDimensions.x()*Constants.HALF;
//        float cycleCenterY = terrain.groundHeightAt(cycleCenterX);
//        Vector2 cycleCenter = new Vector2(cycleCenterX, cycleCenterY);
//
//        new Transition<Float>(
//                sun, // the game object being changes
//                (Float angle) -> sun.setCenter(initialSunCenter.subtract(cycleCenter)
//                        .rotated(angle)
//                        .add(cycleCenter)), // the method to call
//                Constants.ANGLE_MIN, // initial transition value
//                Constants.ANGLE_MAX, // final transition value
//                Transition.LINEAR_INTERPOLATOR_FLOAT, // use a cubic interpolator
//                cycleLength, // transition fully over half a day
//                Transition.TransitionType.TRANSITION_LOOP, // transition ENUM value
//                null);

        return tree;
    }
}
