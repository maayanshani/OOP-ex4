package pepse.world;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.Constants;

/**
 * A utility class for creating the sky GameObject.
 * This class contains a method to create a sky object with specific dimensions and rendering properties.
 */
public class Sky {

    /**
     * Creates a new sky GameObject with specified window dimensions.
     * The sky is rendered with a basic color and placed in camera coordinates.
     * It is also tagged as "sky" for identification.
     *
     * @param windowDimensions The dimensions of the window, used to set the size of the sky object.
     * @return A GameObject representing the sky with the specified dimensions and rendering properties.
     */
    public static GameObject create(Vector2 windowDimensions) {
        GameObject sky = new GameObject(
                Vector2.ZERO,
                windowDimensions,
                new RectangleRenderable(Constants.BASIC_SKY_COLOR));
        sky.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sky.setTag(Constants.SKY);

        return sky;
    }
}
