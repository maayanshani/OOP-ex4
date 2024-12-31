package pepse.world;

import danogl.GameObject;
import danogl.components.Transition;
import danogl.gui.ImageReader;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.Constants;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

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
    private static final Vector2 CLOUD_SIZE = new Vector2(CLOUD_LAYOUT.size(),
            CLOUD_LAYOUT.getFirst().size());
    private static final int NUM_CLOUD_BLOCKS = CLOUD_LAYOUT.size() * CLOUD_LAYOUT.getFirst().size();

    private final List<Block> cloudBlocks;
    private final Vector2 windowDimensions;
    private final int cycleLength;
    private final ImageReader imageReader;

    /**
     * Constructs a Cloud object and initializes the cloud blocks.
     *
     * @param windowDimensions The dimensions of the game window.
     * @param cycleLength The time it takes for the cloud to cross the screen.
     */
    public Cloud(Vector2 windowDimensions, int cycleLength, ImageReader imageReader) {
        this.windowDimensions = windowDimensions;
        this.cycleLength = cycleLength;
        this.imageReader = imageReader;
        this.cloudBlocks = new ArrayList<>();

       createCloudBlocks();
    }

    private void createCloudBlocks(){
        Renderable cloudRenderable = new RectangleRenderable(
                ColorSupplier.approximateMonoColor(BASE_CLOUD_COLOR));
        Vector2 topLeftCorner = new Vector2(0, windowDimensions.y() * Constants.CLOUD_X_LOCATION_RATIO);

        for (int i = 0; i < CLOUD_LAYOUT.size(); i++) {
            for (int j = 0; j < CLOUD_LAYOUT.get(i).size(); j++) {
                if (CLOUD_LAYOUT.get(i).get(j) == 1) {
                    Vector2 cloudBlockPosition = new Vector2(
                            topLeftCorner.x() + j * Block.SIZE,
                            topLeftCorner.y() + i * Block.SIZE
                    );
                    Block cloudBlock = new Block(cloudBlockPosition, cloudRenderable);
                    cloudBlock.setTag(Constants.CLOUD);
                    this.cloudBlocks.add(cloudBlock);
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
    }

    /**
     * Returns the list of cloud blocks.
     *
     * @return A list of Block objects forming the cloud.
     */
    public List<Block> getCloudBlocks() {
        return this.cloudBlocks;
    }

    public List<GameObject> createRain(Consumer<GameObject> removeGameObject) {
        // Get the center of the cloud for rain starting point
        Block middleCloudBlock = cloudBlocks.get(NUM_CLOUD_BLOCKS / 2);
        Vector2 cloudCenter = middleCloudBlock.getCenter();
        Random random = new Random();

        ArrayList<GameObject> rainList = new ArrayList<>();
        int randomNum = random.nextInt(Constants.MAX_NUM_DROPS) + 1; // At least 1 raindrop

        for (int i = 0; i < randomNum; i++) {
            // Randomize drop positions around the cloud center
            float offsetX = random.nextFloat(-Constants.RANDOM_DROP_X, Constants.RANDOM_DROP_X);
            float offsetY = random.nextFloat(-Constants.RANDOM_DROP_Y, Constants.RANDOM_DROP_Y);
            Vector2 dropPosition = new Vector2(cloudCenter.x() + offsetX, cloudCenter.y() + offsetY);

            GameObject waterDrop = Rain.create(imageReader, dropPosition, removeGameObject);
            rainList.add(waterDrop);
        }
        return rainList;
    }

}
