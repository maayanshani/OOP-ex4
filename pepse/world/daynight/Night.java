package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.Constants;

import java.awt.*;

/**
 * Represents the night component in the game. This class is responsible for creating the night object
 * and managing its transitions to simulate day-night cycles.
 */
public class Night {

    /**
     * Creates a night object that darkens the game world during nighttime.
     *
     * @param windowDimensions The dimensions of the game window.
     * @param cycleLength The duration of a full day-night cycle in seconds.
     * @return A GameObject representing the night effect.
     */
    public static GameObject create(Vector2 windowDimensions, float cycleLength) {
        // create night object:
        GameObject night = new GameObject(
                Vector2.ZERO,
                windowDimensions,
                new RectangleRenderable(Color.BLACK));
        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        night.setTag(Constants.NIGHT);

        // add transition, no need to use the object created
        new Transition<Float>(
                night, // the game object being changed
                night.renderer()::setOpaqueness, // the method to call
                0f, // initial transition value
                Constants.MIDNIGHT_OPACITY, // final transition value
                Transition.CUBIC_INTERPOLATOR_FLOAT, // use a cubic interpolator
                cycleLength * Constants.HALF, // transition fully over half a day
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, // transition ENUM value
                null);

        return night;
    }
}
