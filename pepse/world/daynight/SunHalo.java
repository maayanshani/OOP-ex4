package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import pepse.util.Constants;
import pepse.world.Terrain;

import java.awt.*;

public class SunHalo {
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
