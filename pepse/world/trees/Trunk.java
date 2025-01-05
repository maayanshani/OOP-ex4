package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.GameObjectPhysics;
import danogl.gui.ImageReader;
import danogl.util.Vector2;
import pepse.util.Constants;
import pepse.world.Avatar;

public class Trunk extends GameObject {
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

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);

        if (other.getTag().equals(Constants.AVATAR)) {
            System.out.println("Collision with avatar");
            other.transform().setVelocityY(0);

        }


    }

}
