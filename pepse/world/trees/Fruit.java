package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.ScheduledTask;
import danogl.gui.ImageReader;
import danogl.util.Vector2;
import pepse.util.Constants;
import pepse.world.Avatar;

/**
 * Represents a fruit object in the game world. Fruits can be collected by the Avatar
 * to gain energy, and they regenerate after a set time period.
 */
public class Fruit extends GameObject {

    /** The ImageReader instance used to load the fruit image. */
    private final ImageReader imageReader;

    /**
     * Constructs a Fruit object at the specified coordinates.
     *
     * @param imageReader An ImageReader instance to load the fruit image.
     * @param coordinates The initial position of the fruit in the game world.
     */
    public Fruit(ImageReader imageReader,
                 Vector2 coordinates) {
        super(coordinates,
                new Vector2(Constants.FRUIT_SIZE, Constants.FRUIT_SIZE),
                imageReader.readImage(Constants.FRUIT_IMAGE_PATH, true));
        this.imageReader = imageReader;

        this.setTag(Constants.FRUIT);
    }

    /**
     * Handles the behavior when the fruit collides with another game object.
     * If the fruit collides with the Avatar, it adds energy to the Avatar,
     * removes itself from the renderer, and schedules regeneration.
     *
     * @param other The other GameObject involved in the collision.
     * @param collision The Collision instance representing the collision event.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);

        if (other.getTag().equals(Constants.AVATAR) && this.renderer().getRenderable() != null) {
            // Add energy to the Avatar
            Avatar curAvatar = (Avatar) other;
            curAvatar.addEnergy(Constants.FRUIT_ENERGY);

            // Remove fruit from renderer
            this.renderer().setRenderable(null);

            // Schedule regeneration
            addFruit();
        }
    }

    /**
     * Schedules the regeneration of the fruit after a predefined time period.
     * Once the time elapses, the fruit image is restored to make it collectible again.
     */
    public void addFruit() {
        new ScheduledTask(
                this,
                Constants.DAY_LONG,
                false,
                () -> {
                    this.renderer().setRenderable(imageReader.readImage(Constants.FRUIT_IMAGE_PATH, true));
                }
        );
    }
}
