/**
 * Represents a single block in the game world.
 * Blocks are immovable objects with a fixed size, used to build terrain and other structures.
 */
package pepse.world;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Represents a single block in the game world.
 * Blocks are the fundamental building units of the terrain and are used to construct the ground.
 *
 * - Each block has a fixed size defined by the `SIZE` constant.
 * - Blocks are immovable and prevent other objects from intersecting through them.
 * - This class provides a simple and reusable representation of static objects in the game world.
 */
public class Block extends GameObject {
    /** The fixed size of a block, in pixels. */
    public static final int SIZE = 30;

    /**
     * Constructs a new block.
     *
     * @param topLeftCorner The top-left corner position of the block in the game world.
     * @param renderable The renderable to represent the block visually.
     */
    public Block(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, Vector2.ONES.mult(SIZE), renderable);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
    }
}
