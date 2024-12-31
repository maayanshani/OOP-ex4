package pepse.world;

import danogl.GameObject;
import danogl.components.Transition;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.Constants;

import java.util.function.Consumer;

public class Rain{

    public static GameObject create(ImageReader imageReader, Vector2 topLeftCorner,
                                    Consumer<GameObject> removeGameObject) {
        // Create a water drop
        Renderable renderable = imageReader.readImage(Constants.WATER_DROP_IMAGE_PATH, true);
        GameObject waterDrop = new GameObject(
                topLeftCorner,
                new Vector2(Constants.WATER_DROP_SIZE, Constants.WATER_DROP_SIZE),
                renderable
        );
        waterDrop.setTag(Constants.WATER_DROP);
        Vector2 waterDropPosition = topLeftCorner;

        // Add vertical movement to the drop
        new Transition<>(
                waterDrop,
                (Float y) -> waterDrop.setTopLeftCorner(new Vector2(waterDropPosition.x(), y)),
                waterDropPosition.y(), // Start at the initial vertical position
                waterDropPosition.y() + Constants.WATER_DROP_FALL_DISTANCE, // Move down by a range
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                Constants.DROP_VEL_CYCLE,
                Transition.TransitionType.TRANSITION_ONCE,
                null // End of vertical movement
        );

        // Fade out and remove the drop when opacity reaches 0
        new Transition<>(
                waterDrop,
                waterDrop.renderer()::setOpaqueness, // Adjust the opaqueness
                1f, // Fully visible
                Constants.WATER_DROP_FINAL_OPACITY, // Fully transparent
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                Constants.DROP_OPACITY_CYCLE, // Fading duration
                Transition.TransitionType.TRANSITION_ONCE,
                () -> removeGameObject.accept(waterDrop) // Remove when opacity is 0
        );

        return waterDrop;
    }

}
