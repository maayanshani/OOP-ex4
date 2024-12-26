package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.Constants;

import java.awt.*;

public class Night {
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
                night, // the game object being changes
                night.renderer()::setOpaqueness, // the method to call
                0f, // initial transition value
                Constants.MIDNIGHT_OPACITY, // final transition value
                Transition.CUBIC_INTERPOLATOR_FLOAT, // use a cubic interpolator
                cycleLength*Constants.HALF, // transition fully over half a day
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, // transition ENUM value
                null);

        return night;
    }
}
