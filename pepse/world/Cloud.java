package pepse.world;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.Constants;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a moving cloud composed of blocks. The cloud moves across the screen
 * and is created with a predefined layout of blocks.
 */
public class Cloud {
    private static final Color BASE_CLOUD_COLOR = new Color(255, 255, 255);
    private static final List<List<Integer>> CLOUD_LAYOUT = List.of(
            List.of(0, 1, 1, 0, 0, 0),
            List.of(1, 1, 1, 0, 1, 0),
            List.of(1, 1, 1, 1, 1, 1),
            List.of(1, 1, 1, 1, 1, 1),
            List.of(0, 1, 1, 1, 0, 0),
            List.of(0, 0, 0, 0, 0, 0)
    );

    /**
     * Creates and returns a moving cloud object.
     *
     * @param windowDimensions The dimensions of the game window.
     * @param cycleLength The time it takes for the cloud to cross the screen.
     * @return The moving cloud as a GameObject.
     */
    public static GameObject create(Vector2 windowDimensions, float cycleLength) {
        // Define the cloud's initial position outside the left boundary of the screen
        float cloudWidth = CLOUD_LAYOUT.get(0).size() * Block.SIZE;
        float cloudHeight = CLOUD_LAYOUT.size() * Block.SIZE;
        Vector2 initialPosition = new Vector2(-cloudWidth, windowDimensions.y() * 0.2f); // 20% down from
        // the top

        // Create the cloud GameObject
        GameObject cloud = new GameObject(
                initialPosition,
                new Vector2(cloudWidth, cloudHeight),
                new RectangleRenderable(ColorSupplier.approximateMonoColor(BASE_CLOUD_COLOR))
        );

        cloud.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        cloud.setTag(Constants.CLOUD);

        // Add blocks to form the cloud layout
        List<Block> cloudBlocks = createCloudBlocks(initialPosition);
        cloudBlocks.forEach(block -> block.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES));

        // Define the end position of the cloud outside the right boundary of the screen
        float endX = windowDimensions.x() + cloudWidth;

        // Add horizontal movement to the cloud
        new Transition<Float>(
                cloud,
                (Float x) -> cloud.setTopLeftCorner(new Vector2(x, initialPosition.y())),
                initialPosition.x(), // Start outside the left screen boundary
                endX, // Move to outside the right screen boundary
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                cycleLength,
                Transition.TransitionType.TRANSITION_LOOP,
                null // No additional behavior after transition
        );

        return cloud;
    }

    /**
     * Creates a list of blocks forming the cloud based on a predefined layout.
     *
     * @param topLeftCorner The top-left corner of the cloud.
     * @return A list of Block objects forming the cloud.
     */
    private static List<Block> createCloudBlocks(Vector2 topLeftCorner) {
        List<Block> cloudBlocks = new ArrayList<>();
        for (int row = 0; row < CLOUD_LAYOUT.size(); row++) {
            for (int col = 0; col < CLOUD_LAYOUT.get(row).size(); col++) {
                if (CLOUD_LAYOUT.get(row).get(col) == 1) {
                    Vector2 blockPosition = new Vector2(
                            topLeftCorner.x() + col * Block.SIZE,
                            topLeftCorner.y() + row * Block.SIZE
                    );
                    Renderable blockRenderable = new RectangleRenderable(
                            ColorSupplier.approximateMonoColor(BASE_CLOUD_COLOR));
                    Block block = new Block(blockPosition, blockRenderable);
                    block.setTag(Constants.CLOUD);
                    cloudBlocks.add(block);
                }
            }
        }
        return cloudBlocks;
    }
}
