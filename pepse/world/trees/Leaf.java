package pepse.world.trees;

import danogl.GameObject;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.Constants;

import java.util.Random;

/**
 * Utility class for creating and managing leaf objects in the game world.
 * A leaf is a decorative object that exhibits random motion and resizing to simulate natural behavior.
 */
public class Leaf {
    /**
     * Creates a leaf object at the specified coordinates.
     * The leaf exhibits random oscillatory motion and resizing to simulate natural leaf behavior.
     *
     * @param coordinate The initial position of the leaf in the game world.
     * @return A new GameObject representing the leaf.
     */
    public static GameObject create(Vector2 coordinate) {
        // create leaf:
        GameObject leaf = new GameObject(
                coordinate,
                new Vector2(Constants.LEAF_SIZE, Constants.LEAF_SIZE),
                new RectangleRenderable(Constants.LEAF_COLOR));
        leaf.setTag(Constants.LEAF);

        // Add transition, no need to use the object created
        // Create a random delay:
        Random seededRandom = new Random();
        float waitTime = seededRandom.nextFloat(0, Constants.LEAF_MAX_WAIT_TIME);

        // Change the leaf angle:
        new ScheduledTask(
                leaf,
                waitTime,
                true,
                () -> new Transition<Float>(
                        leaf, // the game object being changes
                        (Float angle) -> leaf.renderer().setRenderableAngle(angle), // the method to call
                        Constants.LEAF_ANGLE_MIN, // initial transition value
                        Constants.LEAF_ANGLE_MAX, // final transition value
                        Transition.LINEAR_INTERPOLATOR_FLOAT, // use a cubic interpolator
                        Constants.LEAF_MOVES_TIME, // transition fully over half a day
                        Transition.TransitionType.TRANSITION_BACK_AND_FORTH, // transition ENUM value
                        null));

        // Change the leaf width:
        new ScheduledTask(
                leaf,
                waitTime,
                true,
                () -> new Transition<Float>(
                        leaf, // the game object being changes
                        (Float leafWidth) -> leaf.setDimensions
                                (new Vector2(leafWidth, leaf.getDimensions().y())), // the method to call
                        Constants.LEAF_SIZE, // initial transition value
                        Constants.LEAF_WIDTH_MIN, // final transition value
                        Transition.LINEAR_INTERPOLATOR_FLOAT, // use a cubic interpolator
                        Constants.LEAF_MOVES_TIME, // transition fully over half a day
                        Transition.TransitionType.TRANSITION_BACK_AND_FORTH, // transition ENUM value
                        null));

        return leaf;
    }
}
