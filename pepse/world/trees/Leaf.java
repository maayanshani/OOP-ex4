package pepse.world.trees;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.Constants;

public class Leaf {
    public static GameObject create(ImageReader imageReader, Vector2 coordinate) {
        // create leaf:
        GameObject leaf = new GameObject(
                coordinate,
                new Vector2(Constants.LEAF_SIZE, Constants.LEAF_SIZE),
                new RectangleRenderable(Constants.LEAF_COLOR));
        leaf.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        leaf.setTag(Constants.TREE);

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

        return leaf;
    }
}
