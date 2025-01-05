package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;
import pepse.util.Constants;
import pepse.world.*;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Flora;
import pepse.world.trees.Tree;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the game, including initialization and updates of game objects such as terrain,
 * avatar, clouds, flora and environmental elements like the sun and night cycle.
 */
public class PepseGameManager extends GameManager {

    /**
     * The current X-coordinate location of the avatar.
     */
    private int currentLocationX;

    /**
     * The terrain manager responsible for generating and managing terrain blocks.
     */
    private Terrain terrain;

    /**
     * The dimensions of the game window.
     */
    private Vector2 windowDimensions;

    /**
     * The avatar controlled by the player.
     */
    private Avatar avatar;

    /**
     * The cloud manager responsible for generating clouds and rain.
     */
    private Cloud cloud;

    /**
     * The image reader for loading textures and sprites.
     */
    private ImageReader imageReader;

    /**
     * Functional interface for a function that takes a float and returns a float.
     */
    @FunctionalInterface
    public interface FloatFunction {
        float apply(float x);
    }

    /**
     * Default constructor for the PepseGameManager.
     */
    public PepseGameManager() {
        super();
    }

    /**
     * Initializes the game by setting up all game objects, layers, and the environment.
     *
     * @param imageReader Used to read images and textures.
     * @param soundReader Used to read sound files.
     * @param inputListener Listens for user input events.
     * @param windowController Controls the game window.
     */
    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        windowDimensions = windowController.getWindowDimensions();
        this.imageReader = imageReader;

        gameObjects().layers().shouldLayersCollide(Constants.FRUITS_LAYER, Constants.AVATAR_LAYER, true);
        gameObjects().layers().shouldLayersCollide(Constants.TREES_TRUNKS_LAYER, Constants.AVATAR_LAYER, true);

        // Add sky
        GameObject sky = Sky.create(windowDimensions);
        this.gameObjects().addGameObject(sky, Constants.SKY_LAYER);

        // Add terrain
        this.terrain = new Terrain(windowDimensions, Constants.RANDOM_SEED);
        List<Block> blockList = terrain.createInRange(0, (int) windowDimensions.x());
        for (Block block : blockList) {
            block.setTag(Constants.GROUND);
            this.gameObjects().addGameObject(block, Constants.GROUND_LAYER);
        }

        // Add light and darkness cycle
        GameObject night = Night.create(windowDimensions, Constants.DAY_LONG);
        this.gameObjects().addGameObject(night, Constants.NIGHT_LAYER);

        // Add sun and sun halo
        GameObject sun = Sun.create(imageReader, windowDimensions, Constants.DAY_LONG);
        this.gameObjects().addGameObject(sun, Constants.SUN_LAYER);

        GameObject sunHalo = SunHalo.create(sun);
        this.gameObjects().addGameObject(sunHalo, Constants.SUN_HALO_LAYER);

        // Add avatar
        Vector2 startLocationAvatar = new Vector2(windowDimensions.x() * Constants.HALF, 0);
        currentLocationX = (int) startLocationAvatar.x();
        avatar = new Avatar(startLocationAvatar, inputListener, imageReader);
        avatar.setTag(Constants.AVATAR);
        this.gameObjects().addGameObject(avatar, Constants.AVATAR_LAYER);

        // Add energy display
        EnergyDisplay energyDisplay = new EnergyDisplay(Vector2.ONES.mult(Constants.ENERGY_TEXT_LOCATION),
                () -> avatar.getEnergy()); // Callback to get the avatar's energy
        energyDisplay.setCoordinateSpace(danogl.components.CoordinateSpace.CAMERA_COORDINATES);
        this.gameObjects().addGameObject(energyDisplay, Constants.ENERGY_LAYER);

        // Add trees
        addFlora(0, (int) windowDimensions.x());

        // Add cloud
        this.cloud = new Cloud(windowDimensions, Constants.CLOUD_CYCLE, imageReader);
        List<Block> cloudBlocks = cloud.getCloudBlocks();
        for (Block block : cloudBlocks) {
            this.gameObjects().addGameObject(block, Constants.CLOUD_LAYER);
            block.setCoordinateSpace(danogl.components.CoordinateSpace.CAMERA_COORDINATES);
        }

        // Add callback for rain
        avatar.setOnJumpCallback(this::createRainJump);

        // Add camera movement
        Vector2 initialAvatarLocation = new Vector2(windowDimensions.x() * Constants.HALF,
                windowDimensions.y() * Constants.SCALE_HEIGHT_X0);

        // Create the AvatarX GameObject with a callback to get the avatar's x-coordinate
        AvatarLocationX avatarLocationX = new AvatarLocationX(initialAvatarLocation, () -> avatar.getTopLeftCorner().x());
        this.gameObjects().addGameObject(avatarLocationX);

        setCamera(new Camera(avatarLocationX,
                windowController.getWindowDimensions().mult(0.5f).subtract(initialAvatarLocation),
                windowController.getWindowDimensions(),
                windowController.getWindowDimensions()));
    }

    /**
     * Adds flora (trees and related elements) to the specified range in the game world.
     *
     * @param startRange The start of the range.
     * @param endRange The end of the range.
     */
    private void addFlora(int startRange, int endRange) {
        Flora flora = new Flora(terrain::groundHeightAt, imageReader);

        List<Tree> trees = flora.createInRange(startRange, endRange);

        for (Tree tree : trees) {
            // Add trunk
            this.gameObjects().addGameObject(tree.getTrunk(), Constants.TREES_TRUNKS_LAYER);

            // Add leaves
            for (GameObject leaf : tree.getLeaves()) {
                this.gameObjects().addGameObject(leaf, Constants.TREE_LEAVES_LAYER);
            }

            // Add fruits
            for (GameObject fruit : tree.getFruits()) {
                this.gameObjects().addGameObject(fruit, Constants.FRUITS_LAYER);
            }
        }
    }

    /**
     * Creates rain when the avatar jumps.
     */
    public void createRainJump() {
        List<GameObject> waterDrops = cloud.createRain(this.gameObjects()::removeGameObject);
        for (GameObject waterDrop : waterDrops) {
            this.gameObjects().addGameObject(waterDrop, Constants.CLOUD_LAYER);
            waterDrop.setCoordinateSpace(danogl.components.CoordinateSpace.CAMERA_COORDINATES);
        }
    }

    /**
     * Updates the game state in each frame.
     *
     * @param deltaTime Time elapsed since the last frame.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        updateCreateWorld();
    }

    /**
     * Updates the creation of new game world elements based on the avatar's current location.
     */
    private void updateCreateWorld() {
        int currentAvatarLocX = (int) avatar.getTopLeftCorner().x();

        int visibleStart = (int) (currentAvatarLocX - windowDimensions.x() * Constants.HALF);
        int visibleEnd = (int) (currentAvatarLocX + windowDimensions.x() * Constants.HALF);

        if (currentAvatarLocX > currentLocationX) {
            addVisibleRange(currentLocationX + (int) (windowDimensions.x() * Constants.HALF), visibleEnd);
        } else {
            addVisibleRange(visibleStart, currentLocationX - (int) (windowDimensions.x() * Constants.HALF));
        }
        removeInvisibleRange(visibleStart, visibleEnd);

        currentLocationX = currentAvatarLocX;
    }

    /**
     * Adds visible game elements to the specified range based on the avatar's location.
     *
     * @param visibleStart The start of the visible range.
     * @param visibleEnd The end of the visible range.
     */
    private void addVisibleRange(int visibleStart, int visibleEnd) {
        List<Block> blockList = terrain.createInRange(visibleStart, visibleEnd);
        for (Block block : blockList) {
            block.setTag(Constants.GROUND);
            this.gameObjects().addGameObject(block, Constants.GROUND_LAYER);
        }
        addFlora(visibleStart, visibleEnd);
    }

    /**
     * Removes game elements that are outside the visible range.
     *
     * @param visibleStart The start of the visible range.
     * @param visibleEnd The end of the visible range.
     */
    private void removeInvisibleRange(int visibleStart, int visibleEnd) {
        List<GameObject> objectsToRemove = new ArrayList<>();

        // Remove blocks
        for (GameObject gameObject : gameObjects().objectsInLayer(Constants.GROUND_LAYER)) {
            if (gameObject.getTag().equals(Constants.GROUND)) {
                float blockX = gameObject.getTopLeftCorner().x();
                if (blockX < visibleStart - Constants.VISIBLE_WORLD_MARGIN ||
                        blockX > visibleEnd + Constants.VISIBLE_WORLD_MARGIN) {
                    objectsToRemove.add(gameObject);
                }
            }
        }

        // Remove flora
        for (GameObject gameObject : gameObjects().objectsInLayer(Constants.TREES_TRUNKS_LAYER)) {
            if (gameObject instanceof Tree) {
                Tree tree = (Tree) gameObject;
                float treeX = tree.getTrunk().getTopLeftCorner().x();
                if (treeX < visibleStart - Constants.VISIBLE_WORLD_MARGIN ||
                        treeX > visibleEnd + Constants.VISIBLE_WORLD_MARGIN) {
                    objectsToRemove.add(tree.getTrunk());
                    objectsToRemove.addAll(tree.getLeaves());
                    objectsToRemove.addAll(tree.getFruits());
                }
            }
        }

        for (GameObject gameObject : objectsToRemove) {
            this.gameObjects().removeGameObject(gameObject);
        }
    }

    /**
     * Entry point for the Pepse game application.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        new PepseGameManager().run();
    }
}