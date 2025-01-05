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

public class PepseGameManager extends GameManager {
    
    private int currentLocationX;
    private Terrain terrain;
    private Vector2 windowDimensions;
    private Avatar avatar;
    private Cloud cloud;
    private ImageReader imageReader;

    @FunctionalInterface
    public interface FloatFunction {
        float apply(float x);
    }

    //    private Vector2 windowDimensions;
    public PepseGameManager() {
        super();
    }

    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        windowDimensions = windowController.getWindowDimensions();
        this.imageReader = imageReader;

        gameObjects().layers().shouldLayersCollide(Constants.FRUITS_LAYER, Constants.AVATAR_LAYER,true);
        gameObjects().layers().shouldLayersCollide(Constants.TREES_TRUNKS_LAYER, Constants.AVATAR_LAYER,true);

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

        // Add sun and sun halo:
        GameObject sun = Sun.create(windowDimensions, Constants.DAY_LONG);
        this.gameObjects().addGameObject(sun, Constants.SUN_LAYER);

        GameObject sunHalo = SunHalo.create(sun);
        this.gameObjects().addGameObject(sunHalo, Constants.SUN_HALO_LAYER);

        // Add avatar
        Vector2 startLocationAvatar = new Vector2(windowDimensions.x() *Constants.HALF, 0);
        currentLocationX = (int) startLocationAvatar.x();
        avatar = new Avatar(startLocationAvatar, inputListener, imageReader);
        avatar.setTag(Constants.AVATAR);
        this.gameObjects().addGameObject(avatar, Constants.AVATAR_LAYER);

        // Add energy display
        EnergyDisplay energyDisplay = new EnergyDisplay(Vector2.ONES.mult(Constants.ENERGY_TEXT_LOCATION),
                () -> avatar.getEnergy() // Callback to get the avatar's energy
        );
        energyDisplay.setCoordinateSpace(danogl.components.CoordinateSpace.CAMERA_COORDINATES);
        this.gameObjects().addGameObject(energyDisplay, Constants.ENERGY_LAYER);

        // Add trees:
        addFlora(0, (int) windowDimensions.x());

        // Add cloud
        this.cloud = new Cloud(windowDimensions, Constants.CLOUD_CYCLE, imageReader);
        List<Block> cloudBlocks = cloud.getCloudBlocks();
        for (Block block : cloudBlocks) {
            this.gameObjects().addGameObject(block, Constants.CLOUD_LAYER);
            block.setCoordinateSpace(danogl.components.CoordinateSpace.CAMERA_COORDINATES);
        }

        // add callback for rain
        avatar.setOnJumpCallback(this::createRainJump);

        // add camera movement
        Vector2 initialAvatarLocation = new Vector2(windowDimensions.x()*Constants.HALF,
                windowDimensions.y() * Constants.SCALE_HEIGHT_X0) ;


        // Create the AvatarX GameObject with a callback to get the avatar's x-coordinate
        AvatarLocationX avatarLocationX = new AvatarLocationX(initialAvatarLocation, () -> avatar.getTopLeftCorner().x());
        this.gameObjects().addGameObject(avatarLocationX);

        // add camera movement
        setCamera(new Camera(avatarLocationX,
                windowController.getWindowDimensions().mult(0.5f).subtract(initialAvatarLocation),
                windowController.getWindowDimensions(),
                windowController.getWindowDimensions()));


        // TODO: only for tests:
//        Vector2 curCoor = new Vector2(1000, terrain.groundHeightAt(1000)+50);
//        Tree tree = new Tree(imageReader, curCoor);
//        // add trunk:
//        this.gameObjects().addGameObject(tree.getTrunk(), Constants.TREES_TRUNKS_LAYER);
//
//        // add leaves:
//        for (GameObject leaf: tree.getLeaves()) {
//            this.gameObjects().addGameObject(leaf, Constants.TREE_LEAVES_LAYER);
//        }
//
//        // add fruits:
//        for (GameObject fruit: tree.getFruits()) {
//            this.gameObjects().addGameObject(fruit, Constants.FRUITS_LAYER);
//        }
    }

    private void addFlora(int startRange, int endRange) {

        Flora flora = new Flora(terrain::groundHeightAt, imageReader);

        List<Tree> trees = flora.createInRange(startRange, endRange);

        for (Tree tree: trees) {
            // add trunk:
            this.gameObjects().addGameObject(tree.getTrunk(), Constants.TREES_TRUNKS_LAYER);

            // add leaves:
            for (GameObject leaf: tree.getLeaves()) {
                this.gameObjects().addGameObject(leaf, Constants.TREE_LEAVES_LAYER);
            }

            // add fruits:
            for (GameObject fruit: tree.getFruits()) {
                this.gameObjects().addGameObject(fruit, Constants.FRUITS_LAYER);
            }

        }

    }

    public void createRainJump(){
        List<GameObject> waterDrops = cloud.createRain(this.gameObjects()::removeGameObject);
        for (GameObject waterDrop: waterDrops) {
            this.gameObjects().addGameObject(waterDrop, Constants.CLOUD_LAYER);
            waterDrop.setCoordinateSpace(danogl.components.CoordinateSpace.CAMERA_COORDINATES);

        }
    }


    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        updateCreateWorld();
    }

    private void updateCreateWorldTEST() {
        List<Block> blockList;

        // Current location of the avatar
        int currentAvatarLocX = (int) avatar.getTopLeftCorner().x();

        // Define the visible range
        int visibleStart = (int) (currentAvatarLocX - windowDimensions.x() * Constants.HALF);
        int visibleEnd = (int) (currentAvatarLocX + windowDimensions.x() *Constants.HALF);

        // Create new terrain in the visible range
        if (currentAvatarLocX > currentLocationX) {
            blockList = terrain.createInRange(currentLocationX + (int) (windowDimensions.x()*Constants.HALF), visibleEnd);
        } else {
            blockList = terrain.createInRange(visibleStart, currentLocationX - (int) (windowDimensions.x() * Constants.HALF));
        }

        // Add newly created blocks to the game
        for (Block block : blockList) {
            block.setTag(Constants.GROUND);
            this.gameObjects().addGameObject(block, Constants.GROUND_LAYER);
        }

        // Remove blocks outside the visible range
        int removalMargin = 50; // Allow some buffer beyond the visible range
        for (GameObject gameObject : gameObjects().objectsInLayer(Constants.GROUND_LAYER)) {
            if (gameObject instanceof Block) {
                float blockX = gameObject.getTopLeftCorner().x();
                if (blockX < visibleStart - removalMargin || blockX > visibleEnd + removalMargin) {
                    this.gameObjects().removeGameObject(gameObject, Constants.GROUND_LAYER);
                }
            }
        }

        // Update the current location
        currentLocationX = currentAvatarLocX;
    }


    private void updateCreateWorld() {
        // Current location of the avatar
        int currentAvatarLocX = (int) avatar.getTopLeftCorner().x();
        float updateThreshold = windowDimensions.x()*Constants.HALF - Constants.VISIBLE_WORLD_MARGIN;

        // visible range:
        int visibleStart = (int) (currentAvatarLocX - windowDimensions.x() * Constants.HALF);
        int visibleEnd = (int) (currentAvatarLocX + windowDimensions.x() * Constants.HALF);

        // Create new world in visible range:
        if (currentAvatarLocX > currentLocationX) {
            addVisibleRange(currentAvatarLocX, currentLocationX + (int) (windowDimensions.x() * Constants.HALF), visibleEnd);

        } else {
            addVisibleRange(currentAvatarLocX, visibleStart, currentLocationX - (int) (windowDimensions.x() *Constants.HALF));

        }
        removeInvisibleRange(visibleStart, visibleEnd);

        currentLocationX = currentAvatarLocX;
    }

    private void addVisibleRange(int currentAvatarLocX, int visibleStart, int visibleEnd) {
        // add new blocks:
        List<Block> blockList = terrain.createInRange(visibleStart, visibleEnd);
        for (Block block : blockList) {
            block.setTag(Constants.GROUND);
            this.gameObjects().addGameObject(block, Constants.GROUND_LAYER);
        }
        // add new Flora:
        addFlora(visibleStart, visibleEnd);

    }

    private void removeInvisibleRange(int visibleStart, int visibleEnd) {
        List<GameObject> objectsToRemove = new ArrayList<>();

        // remove blocks:
        for (GameObject gameObject : gameObjects().objectsInLayer(Constants.GROUND_LAYER)) {
            if (gameObject.getTag().equals(Constants.GROUND)) {
                float blockX = gameObject.getTopLeftCorner().x();
                if (blockX < visibleStart - Constants.VISIBLE_WORLD_MARGIN ||
                        blockX > visibleEnd + Constants.VISIBLE_WORLD_MARGIN) {
//                    this.gameObjects().removeGameObject(gameObject, Constants.GROUND_LAYER);
                    objectsToRemove.add(gameObject);

                }
            }
        }

        // remove Flora:

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

        // Remove all trees that are outside the visible range
        for (GameObject gameObject : objectsToRemove) {
            this.gameObjects().removeGameObject(gameObject);
        }

    }

    // TODO: needed?
    public void removeObject(GameObject object, int layerIndex) {
        this.gameObjects().removeGameObject(object, layerIndex);
    }

    // TODO: needed?
    public void addObject(GameObject object, int layerIndex) {
        this.gameObjects().addGameObject(object);
    }

    // Only for tests
    private void clampAvatarPosition() {
        Vector2 avatarPosition = avatar.getTopLeftCorner();

        // Limit avatar's position within the screen boundaries
        float clampedX = Math.max(0, Math.min(avatarPosition.x(),
                windowDimensions.x() - avatar.getDimensions().x()));
        float clampedY = Math.max(0, avatarPosition.y()); // Allow falling below screen if needed

        // Update the avatar's position
        avatar.transform().setTopLeftCorner(new Vector2(clampedX, clampedY));
    }

    public static void main(String[] args) {
        new PepseGameManager().run();

    }
}
