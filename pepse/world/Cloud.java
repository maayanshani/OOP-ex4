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

import static pepse.util.Constants.CLOUD_LAYOUT;
import static pepse.util.Constants.DROP_OPACITY_CYCLE;

/**
 * Represents a cloud composed of blocks. The cloud moves horizontally across the screen
 * and is created using a static layout with randomized white shades for the blocks.
 * This class also manages a pool of raindrops to simulate rainfall during gameplay.
 */
public class Cloud {
    /** Random number generator for color variation. */
    private static final Random RANDOM = new Random();

    /** List of blocks forming the cloud. */
    private final List<Block> cloudBlocks;

    /** Dimensions of the game window. */
    private final Vector2 windowDimensions;

    /** Time it takes for the cloud to complete one movement cycle across the screen. */
    private final int cycleLength;

    /** ImageReader instance for loading textures and images. */
    private final ImageReader imageReader;

    /** Pool of reusable raindrop objects to optimize performance. */
    private final List<GameObject> rainPool = new ArrayList<>();

    /** Number of raindrops to keep in the pool for reuse. */
    private final int poolSize = Constants.MAX_NUM_DROPS * DROP_OPACITY_CYCLE;

    /**
     * Constructs a Cloud object and initializes the cloud blocks and raindrop pool.
     *
     * @param windowDimensions The dimensions of the game window.
     * @param cycleLength The time it takes for the cloud to cross the screen.
     * @param imageReader The ImageReader instance used for loading images.
     */
    public Cloud(
            Vector2 windowDimensions,
            int cycleLength,
            ImageReader imageReader) {
        this.windowDimensions = windowDimensions;
        this.cycleLength = cycleLength;
        this.imageReader = imageReader;
        this.cloudBlocks = new ArrayList<>();

        createCloudBlocks();
        initializeRainPool(); // Initialize the raindrop pool
    }

    /**
     * Randomizes the color of the cloud blocks slightly by adjusting the RGB values.
     *
     * @return A randomized color based on the base cloud color.
     */
    private Color getRandomizedColor() {
        int red = Math.min(Constants.COLOR_MAX, Math.max(Constants.COLOR_MIN,
                Constants.BASE_CLOUD_COLOR.getRed() + RANDOM.nextInt(2 * Constants.COLOR_VARIATION + 1) - Constants.COLOR_VARIATION));
        int green = Math.min(Constants.COLOR_MAX, Math.max(Constants.COLOR_MIN,
                Constants.BASE_CLOUD_COLOR.getGreen() + RANDOM.nextInt(2 * Constants.COLOR_VARIATION + 1) - Constants.COLOR_VARIATION));
        int blue = Math.min(Constants.COLOR_MAX, Math.max(Constants.COLOR_MIN,
                Constants.BASE_CLOUD_COLOR.getBlue() + RANDOM.nextInt(2 * Constants.COLOR_VARIATION + 1) - Constants.COLOR_VARIATION));
        return new Color(red, green, blue);
    }

    /**
     * Creates the cloud blocks based on a predefined layout and adds a horizontal movement transition.
     */
    private void createCloudBlocks() {
        Vector2 topLeftCorner = new Vector2(0, windowDimensions.y() * Constants.CLOUD_LOCATION_X_RATIO);

        for (int i = 0; i < CLOUD_LAYOUT.size(); i++) {
            for (int j = 0; j < CLOUD_LAYOUT.get(i).size(); j++) {
                if (CLOUD_LAYOUT.get(i).get(j) == 1) {
                    Color color = getRandomizedColor();
                    Renderable cloudRenderable = new RectangleRenderable(
                            ColorSupplier.approximateMonoColor(color));
                    Vector2 cloudBlockPosition = new Vector2(
                            topLeftCorner.x() + j * Block.SIZE,
                            topLeftCorner.y() + i * Block.SIZE
                    );
                    Block cloudBlock = new Block(cloudBlockPosition, cloudRenderable);
                    cloudBlock.setTag(Constants.CLOUD);
                    this.cloudBlocks.add(cloudBlock);
                    float endX = windowDimensions.x() + cloudBlockPosition.x();

                    // Add horizontal movement to the cloud
                    new Transition<>(
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
     * Initializes the pool of reusable raindrops.
     */
    private void initializeRainPool() {
        for (int i = 0; i < poolSize; i++) {
            GameObject waterDrop = Rain.create(imageReader);
            waterDrop.renderer().setOpaqueness(0); // Make initially invisible
            rainPool.add(waterDrop);
        }
    }

    /**
     * Returns the list of cloud blocks forming the cloud.
     *
     * @return A list of Block objects representing the cloud.
     */
    public List<Block> getCloudBlocks() {
        return this.cloudBlocks;
    }

    /**
     * Creates raindrops that fall from the cloud. The raindrops are taken from the pool
     * and returned to the pool after falling.
     *
     * @param removeGameObject A consumer for removing raindrops from the game world.
     * @return A list of active raindrops to be added to the game world.
     */
    public List<GameObject> createRain(Consumer<GameObject> removeGameObject) {
        Block middleCloudBlock = cloudBlocks.get(Constants.NUM_CLOUD_BLOCKS / 2);
        Vector2 cloudCenter = middleCloudBlock.getCenter();
        Random random = new Random();

        List<GameObject> activeRain = new ArrayList<>();
        int numDrops = random.nextInt(Constants.MAX_NUM_DROPS) + 1;

        for (int i = 0; i < numDrops; i++) {
            if (rainPool.isEmpty()) break;

            GameObject waterDrop = rainPool.removeFirst();
            if (waterDrop != null) {
                float offsetX = random.nextFloat(-Constants.DROP_LOC_X_VARIATION, Constants.DROP_LOC_X_VARIATION);
                float offsetY = random.nextFloat(-Constants.DROP_LOC_Y_VARIATION, Constants.DROP_LOC_Y_VARIATION);
                Vector2 dropPosition = cloudCenter.add(new Vector2(offsetX, offsetY));

                Rain.initializeRainTransition(waterDrop, dropPosition, drop -> {
                    rainPool.add(drop); // Return drop to pool after use
                    drop.renderer().setOpaqueness(0); // Make it invisible
                    removeGameObject.accept(drop);
                });

                activeRain.add(waterDrop);
            }
        }
        return activeRain;
    }
}
