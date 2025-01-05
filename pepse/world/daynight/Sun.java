package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.ImageReader;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import pepse.util.Constants;
import pepse.world.Terrain;

import java.awt.*;

/**
 * Represents the sun in the game world. The sun cycles across the sky
 * following a predefined trajectory based on the given cycle length.
 */
public class Sun {

    /**
     * Creates the sun game object with a circular trajectory in the sky.
     *
     * @param windowDimensions Dimensions of the game window.
     * @param cycleLength The time (in seconds) for the sun to complete a full cycle.
     * @param imageReader An ImageReader instance used to load the image for the sun.
     * @return A GameObject representing the sun.
     */
    public static GameObject create(ImageReader imageReader, Vector2 windowDimensions, float cycleLength) {
        // Terrain object for determining ground height.
        Terrain terrain = new Terrain(windowDimensions, Constants.RANDOM_SEED);

        // Calculate the initial position of the sun.
        float sunX = windowDimensions.x() * Constants.HALF - Constants.SUN_SIZE * Constants.HALF;
        float sunY = (windowDimensions.y() - terrain.groundHeightAt(sunX)) * Constants.HALF;
        Vector2 initialSunCenter = new Vector2(sunX, sunY);

        // Create the sun GameObject.
        GameObject sun = new GameObject(
                initialSunCenter, // Initial position (updated by the Transition)
                new Vector2(Constants.SUN_SIZE, Constants.SUN_SIZE),
                imageReader.readImage(Constants.SUN_IMAGE_PATH, true));
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sun.setTag(Constants.SUN);

        // Define the center point of the sun's trajectory.
        float cycleCenterX = windowDimensions.x() * Constants.HALF;
        float cycleCenterY = terrain.groundHeightAt(cycleCenterX);
        Vector2 cycleCenter = new Vector2(cycleCenterX, cycleCenterY);

        // Add a Transition to animate the sun's movement.
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
