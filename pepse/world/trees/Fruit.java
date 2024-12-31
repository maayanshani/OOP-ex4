package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.CoordinateSpace;
import danogl.gui.ImageReader;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.util.Constants;
import pepse.world.Avatar;

import java.util.Objects;

public class Fruit extends GameObject {
//    // TODO: where is the right place to put it?
//    @FunctionalInterface
//    public interface ObjectRemoveFunction {
//        void apply(GameObject x);
//    }

    private final ImageReader imageReader;
    private final Vector2 coordinates;
    private PepseGameManager.ObjectRemoveFunction objectRemoveFunction;

    public Fruit(ImageReader imageReader, Vector2 coordinates, PepseGameManager.ObjectRemoveFunction function) {
        super(coordinates,
                new Vector2(Constants.FRUIT_SIZE, Constants.FRUIT_SIZE),
                imageReader.readImage(Constants.FRUIT_IMAGE_PATH, true));
        this.imageReader = imageReader;
        this.coordinates = coordinates;
        this.objectRemoveFunction = function;

//        // create fruit:
//        Renderable fruitImage = imageReader.readImage(Constants.FRUIT_IMAGE_PATH, true);
//
//        GameObject fruit = new GameObject(
//                coordinate,
//                new Vector2(Constants.FRUIT_SIZE, Constants.FRUIT_SIZE),
//                fruitImage);
        this.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        this.setTag(Constants.FRUIT);

    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        System.out.println("Collision detected here with: " + other.getTag());

        // TODO: problem here: other is fruit instead of avatar
        if (other.getTag().equals(Constants.AVATAR)) {
            // add energy:
            Avatar curAvatar = (Avatar) other;
            curAvatar.addEnergy(Constants.FRUIT_ENERGY);
            // remove fruit:
            System.out.println("Removing fruit: " + this.getTag());
            objectRemoveFunction.apply(this, Constants.AVATAR_LAYER);
        }

    }

//    public static GameObject create(ImageReader imageReader, Vector2 coordinate) {
//        // create fruit:
//        Renderable fruitImage = imageReader.readImage(Constants.FRUIT_IMAGE_PATH, true);
//
//        GameObject fruit = new GameObject(
//                coordinate,
//                new Vector2(Constants.FRUIT_SIZE, Constants.FRUIT_SIZE),
//                fruitImage);
//        fruit.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
//        fruit.setTag(Constants.FRUIT);
//
//        return fruit;

//    }

}
