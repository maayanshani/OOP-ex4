package pepse.world;

import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.Constants;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a cloud composed of blocks. The cloud is created with a static layout
 * and randomized white shades for the blocks.
 */
public class Cloud {
    private static final Color BASE_CLOUD_COLOR = new Color(255, 255, 255);
    private static final List<List<Integer>> CLOUD_LAYOUT = List.of(
            List.of(0, 0, 1, 1, 0, 0, 0, 0, 0),
            List.of(0, 1, 1, 1, 0, 0, 1, 0, 0),
            List.of(1, 1, 1, 1, 1, 1, 1, 1, 0),
            List.of(1, 1, 1, 1, 1, 1, 1, 1, 1),
            List.of(0, 1, 1, 1, 1, 1, 1, 1, 0),
            List.of(0, 0, 1, 1, 1, 1, 1, 0, 0),
            List.of(0, 0, 0, 1, 1, 0, 0, 0, 0)
    );

    /**
     * Creates and returns a moving cloud object.
     *
     * @param windowDimensions The dimensions of the game window.
     * @param cycleLength The time it takes for the cloud to cross the screen.
     * @return The cloud as a GameObject with individual blocks preserved.
     */
    public static List<Block> create(Vector2 windowDimensions, int cycleLength) {
        List<Block> cloudBlocks = new ArrayList<>();
        Renderable cloudRenderable = new RectangleRenderable(
                ColorSupplier.approximateMonoColor(BASE_CLOUD_COLOR));
        Vector2 topLeftCorner = new Vector2(0 , windowDimensions.y()*Constants.CLOUD_X_LOCATION_RATIO);

        for (int i = 0; i < CLOUD_LAYOUT.size(); i++) {
            for (int j = 0; j < CLOUD_LAYOUT.get(i).size(); j++) {
                if (CLOUD_LAYOUT.get(i).get(j) == 1) {
                    Vector2 cloudBlockPosition = new Vector2(
                            topLeftCorner.x() + j * Block.SIZE,
                            topLeftCorner.y() + i * Block.SIZE
                    );
                    Block cloudBlock = new Block(cloudBlockPosition, cloudRenderable);
                    cloudBlock.setTag(Constants.CLOUD);
                    cloudBlocks.add(cloudBlock);
                    float endX = windowDimensions.x() + cloudBlockPosition.x();


                    // Add horizontal movement to the cloud
                    new Transition<Float>(
                            cloudBlock,
                            (Float x) -> cloudBlock.setTopLeftCorner(new Vector2(x, cloudBlockPosition.y())),
                            cloudBlockPosition.x(), // Start outside the left screen boundary
                            endX, // Move to outside the right screen boundary
                            Transition.LINEAR_INTERPOLATOR_FLOAT,
                            cycleLength,
                            Transition.TransitionType.TRANSITION_LOOP,
                            null // No additional behavior after transition
                    );

                }
            }
        }
        return cloudBlocks;
    }
}
