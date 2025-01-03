package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
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
import pepse.world.trees.Fruit;
import pepse.world.trees.Tree;

import java.util.List;

public class PepseGameManager extends GameManager {
    
    private int currentLocationX;
    private Terrain terrain;

    // TODO: where is the right place to put it?
    // TODO: MAAYAN ANSWER: I think both locations are good

    @FunctionalInterface
    public interface ObjectFunction {
        void apply(GameObject x, int y);
    }

    // TODO: where is the right place to put it?.
    // TODO: MAAYAN ANSWER: I think both locations are good
    @FunctionalInterface
    public interface FloatFunction {
        float apply(float x);
    }

    private Vector2 windowDimensions;
    private Avatar avatar;
    private Cloud cloud;

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
        Vector2 startLocationAvatar = new Vector2(windowDimensions.x() / 2, 0);
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
        Flora flora = new Flora(this.windowDimensions,
                terrain::groundHeightAt,
                imageReader);

        for (Tree tree: flora.createInRange(0, (int) windowDimensions.x())) {
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
        Vector2 initialAvatarLocation = new Vector2(windowDimensions.x() / 2,
                windowDimensions.y() * Constants.SCALE_HEIGHT_X0) ;

        // add camera movement
        setCamera(new Camera(avatar,
                windowController.getWindowDimensions().mult(0.5f).subtract(initialAvatarLocation),
                windowController.getWindowDimensions(),
                windowController.getWindowDimensions()));

        // TODO: only for tests:
//        Vector2 curCoor = new Vector2(100, windowDimensions.y()-terrain.groundHeightAt(100));
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


        // Clamp the avatar's position within window dimensions
//        clampAvatarPosition();
    }

    private void updateCreateWorld() {
        List<Block> blockList;
        int currentAvatarLocX = (int) avatar.getTopLeftCorner().x();
        if (currentAvatarLocX > currentLocationX) {
//            createUpdateWorld(currentAvatarLocX)
            blockList = terrain.createInRange((int) (currentLocationX + windowDimensions.x()/2),
                    (int) (currentAvatarLocX + windowDimensions.x()/2));


        } else {
            blockList = terrain.createInRange((int) (currentAvatarLocX - windowDimensions.x() / 2),
                    (int) (currentLocationX - windowDimensions.x() / 2));
        }
        currentLocationX = currentAvatarLocX;
        for (Block block : blockList) {
            block.setTag(Constants.GROUND);
            this.gameObjects().addGameObject(block, Constants.GROUND_LAYER);
        }
    }

    public void removeObject(GameObject object, int layerIndex) {
        System.out.println("removed");
        this.gameObjects().removeGameObject(object, layerIndex);
    }

    public void addObject(GameObject object, int layerIndex) {
        System.out.println("added");
        this.gameObjects().addGameObject(object);
    }

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
