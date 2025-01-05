package pepse.world.trees;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.ImageReader;
import danogl.util.Vector2;
import pepse.util.Constants;

/**
 * Represents the trunk of a tree in the game world.
 * A trunk is an immovable game object that forms the base of a tree.
 */
public class Trunk extends GameObject {

    /**
     * Constructs a Trunk object with the specified coordinates, dimensions, and image.
     * The trunk is set to be immovable and prevents intersections with other objects.
     *
     * @param imageReader An ImageReader instance used to load the image for the trunk.
     * @param coordinates The top-left corner coordinates of the trunk in the game world.
     * @param dimensions  The width and height dimensions of the trunk.
     */
    public Trunk(ImageReader imageReader,
                 Vector2 coordinates,
                 Vector2 dimensions) {
        super(coordinates,
                dimensions,
                imageReader.readImage(Constants.TRUNK_IMAGE_PATH, true));
        this.setTag(Constants.TRUNK);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
    }

}
