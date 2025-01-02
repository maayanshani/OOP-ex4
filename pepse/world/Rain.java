package pepse.world;

import danogl.GameObject;
import danogl.components.Transition;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.Constants;

import java.util.function.Consumer;

/**
 * Handles the creation and behavior of raindrops in the game.
 * Provides reusable raindrops with transitions for falling and fading out.
 * Optimized for performance with pooling and callback mechanisms.
 */
public class Rain {
    /**
     * Creates a reusable water drop object.
     *
     * @param imageReader      Image reader for rendering the water drop.
     * @return A reusable GameObject representing the water drop.
     */
    public static GameObject create(ImageReader imageReader) {
        // Create a water drop
        Renderable renderable = imageReader.readImage(Constants.WATER_DROP_IMAGE_PATH, true);
        GameObject waterDrop = new GameObject(
                Vector2.ZERO, // doesn't matter because they haven't added to the game
                new Vector2(Constants.WATER_DROP_SIZE, Constants.WATER_DROP_SIZE),
                renderable
        );
        waterDrop.setTag(Constants.WATER_DROP);

        // Add a placeholder for transition; actual transition is set later
        waterDrop.renderer().setOpaqueness(0); // Make it initially invisible
        return waterDrop;
    }

    /**
     * Initializes the transition for the water drop.
     * set for better flow of the game. seperate the creation and transitiopn makes the game faster when
     * the avatar jumps
     *
     * @param waterDrop The water drop GameObject.
     * @param startPosition The starting position of the water drop.
     * @param callback Callback to remove the water drop when done.
     */
    public static void initializeRainTransition(GameObject waterDrop, Vector2 startPosition,
                                                Consumer<GameObject> callback) {
        Vector2 endPosition = startPosition.add(new Vector2(0, Constants.WATER_DROP_FALL_DISTANCE));
        waterDrop.transform().setTopLeftCorner(startPosition);
        waterDrop.renderer().setOpaqueness(1); // Make it visible

        // Add vertical movement to the drop
        new Transition<>(
                waterDrop,
                (Float y) -> waterDrop.setTopLeftCorner(new Vector2(startPosition.x(), y)),
                startPosition.y(),
                endPosition.y(),
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                Constants.DROP_VEL_CYCLE,
                Transition.TransitionType.TRANSITION_ONCE,
                null
        );

        // Fade out and return to pool when opacity reaches 0
        new Transition<>(
                waterDrop,
                waterDrop.renderer()::setOpaqueness,
                1f,
                Constants.WATER_DROP_FINAL_OPACITY,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                Constants.DROP_OPACITY_CYCLE,
                Transition.TransitionType.TRANSITION_ONCE,
                () -> callback.accept(waterDrop) // Return to pool
        );
    }
}
