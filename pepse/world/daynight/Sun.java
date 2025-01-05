package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.ImageReader;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.Constants;
import pepse.world.Terrain;

import java.awt.*;

public class Sun {
    public static GameObject create(Vector2 windowDimensions, float cycleLength) {
        // TODO: maybe get callback instead of creat Terrain?
        Terrain terrain = new Terrain(windowDimensions, Constants.RANDOM_SEED);

        // create sun object:
        float sunX = windowDimensions.x()*Constants.HALF - Constants.SUN_SIZE*Constants.HALF;
        float sunY = (windowDimensions.y() - terrain.groundHeightAt(sunX)) * Constants.HALF;
        Vector2 initialSunCenter = new Vector2(sunX, sunY);

        GameObject sun = new GameObject(
                initialSunCenter, // doesn't matter  because the Transition
                new Vector2(Constants.SUN_SIZE, Constants.SUN_SIZE),
                new OvalRenderable(Color.YELLOW));
//                imageReader.readImage(Constants.SUN_IMAGE_PATH, true));
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sun.setTag(Constants.SUN);

        // add transition, no need to use the object created
        float cycleCenterX = windowDimensions.x()*Constants.HALF;
        float cycleCenterY = terrain.groundHeightAt(cycleCenterX);
        Vector2 cycleCenter = new Vector2(cycleCenterX, cycleCenterY);

        new Transition<Float>(
                sun, // the game object being changes
                (Float angle) -> sun.setCenter(initialSunCenter.subtract(cycleCenter)
                                .rotated(angle)
                                .add(cycleCenter)), // the method to call
                Constants.SUN_ANGLE_MIN, // initial transition value
                Constants.SUN_ANGLE_MAX, // final transition value
                Transition.LINEAR_INTERPOLATOR_FLOAT, // use a cubic interpolator
                cycleLength, // transition fully over half a day
                Transition.TransitionType.TRANSITION_LOOP, // transition ENUM value
                null);

        return sun;
    }
}
