package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.Constants;

import java.awt.*;

public class Sun {
    public static GameObject create(Vector2 windowDimensions, float cycleLength) {
        // create sun object:
        float sunX = windowDimensions.x()*Constants.HALF - Constants.SUN_SIZE*Constants.HALF;
        float sunY = windowDimensions.y()*Constants.QUARTER - Constants.SUN_SIZE*Constants.HALF;
        GameObject sun = new GameObject(
                new Vector2(sunX, sunY),
                new Vector2(Constants.SUN_SIZE, Constants.SUN_SIZE),
                new OvalRenderable(Color.YELLOW));
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sun.setTag(Constants.SUN);

        // TODO:
        // add transition, no need to use the object created
//        new Transition<Float>(
//                night, // the game object being changes
//                night.renderer()::setOpaqueness, // the method to call
//                0f, // initial transition value
//                Constants.MIDNIGHT_OPACITY, // final transition value
//                Transition.CUBIC_INTERPOLATOR_FLOAT, // use a cubic interpolator
//                cycleLength/Constants.TWO, // transition fully over half a day
//                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, // transition ENUM value
//                null);

        return sun;
    }
}
