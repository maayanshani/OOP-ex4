package pepse.world;

import danogl.GameObject;
import danogl.util.Vector2;
import pepse.util.Constants;

import java.util.function.Supplier;

/**
 * A GameObject that tracks the x-coordinate of the avatar.
 */
public class AvatarLocationX extends GameObject {

    private final Supplier<Float> xPositionSupplier;

    /**
     * Constructs a new AvatarX object.
     *
     * @param topLeftCorner   The initial top-left corner of the GameObject.
     * @param xPositionSupplier A callback function that supplies the x-coordinate of the avatar.
     */
    public AvatarLocationX(Vector2 topLeftCorner, Supplier<Float> xPositionSupplier) {
        super(topLeftCorner, Vector2.ONES.mult(Constants.AVATAR_SIZE), null);
        this.xPositionSupplier = xPositionSupplier;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        // Update only the x-coordinate of this GameObject
        float newX = xPositionSupplier.get();
        transform().setTopLeftCorner(new Vector2(newX, getTopLeftCorner().y()));
    }
}
