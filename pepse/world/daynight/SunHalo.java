package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import pepse.util.Constants;


/**
 * Utility class for creating a Sun Halo, a graphical effect that surrounds the sun object
 * to enhance its visual appearance.
 */
public class SunHalo {

    /**
     * Creates a new Sun Halo game object. The Sun Halo follows the sun's position, providing
     * a glowing halo effect around it.
     *
     * @param sun The sun GameObject around which the halo will be positioned and follow.
     * @return A new GameObject representing the sun halo.
     */
    public static GameObject create(GameObject sun) {
        // create sunHalo object:
        GameObject sunHalo = new GameObject(
                sun.getTopLeftCorner(), // doesn't matter, the Transition is set by the sun at any frame
                sun.getDimensions().mult(Constants.HALO_MULT_RATIO),
                new OvalRenderable(Constants.SUN_HALO_COLOR));
        sunHalo.setCenter(sun.getCenter());
        sunHalo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sunHalo.setTag(Constants.SUN_HALO);

        // add transition, no need to use the object created
        sunHalo.addComponent(deltaTime -> sunHalo.setCenter(sun.getCenter()));

        return sunHalo;
    }
}
