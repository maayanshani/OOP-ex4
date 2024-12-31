package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.CoordinateSpace;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.ImageReader;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.util.Constants;
import pepse.world.Avatar;

import java.util.Objects;

public class Fruit extends GameObject {

    private final ImageReader imageReader;

    public Fruit(ImageReader imageReader,
                 Vector2 coordinates) {
        super(coordinates,
                new Vector2(Constants.FRUIT_SIZE, Constants.FRUIT_SIZE),
                imageReader.readImage(Constants.FRUIT_IMAGE_PATH, true));
        this.imageReader = imageReader;

        this.setTag(Constants.FRUIT);

    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        System.out.println("Fruit class Collision detected with: " + other.getTag());

        if (other.getTag().equals(Constants.AVATAR) && this.renderer().getRenderable()!=null) {
            // add energy:
            Avatar curAvatar = (Avatar) other;
            curAvatar.addEnergy(Constants.FRUIT_ENERGY);

            // remove fruit from renderer:
            System.out.println("Removing fruit: " + this.getTag());
            this.renderer().setRenderable(null);

            // add the next day:
            addFruit();

        }

    }

    public void addFruit() {
        // add fruit after 30 ms:
        System.out.println("Scheduling task to add fruit back after delay...");

        new ScheduledTask(
                this,
                Constants.DAY_LONG,
                false,
                () -> {
                    System.out.println("Adding new fruit back to the game.");
                    this.renderer().setRenderable(imageReader.readImage(Constants.FRUIT_IMAGE_PATH, true));
                }
        );
    }

}
