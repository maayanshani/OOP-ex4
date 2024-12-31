package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
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
    // TODO: where is the right place to put it?
    @FunctionalInterface
    public interface ObjectFunction {
        void apply(GameObject x, int y);
    }

    // TODO: where is the right place to put it?
    @FunctionalInterface
    public interface FloatFunction {
        float apply(float x);
    }

    private Avatar avatar;
    private Vector2 windowDimensions;

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
        Terrain terrain = new Terrain(windowDimensions, Constants.RANDOM_SEED);
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
        avatar = new Avatar(startLocationAvatar, inputListener, imageReader);
        avatar.setTag(Constants.AVATAR);
        this.gameObjects().addGameObject(avatar, Constants.AVATAR_LAYER);

        // Add energy display
        EnergyDisplay energyDisplay = new EnergyDisplay(Vector2.ONES.mult(Constants.ENERGY_TEXT_LOCATION),
                () -> avatar.getEnergy() // Callback to get the avatar's energy
        );
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

        // Add cloud
        // Somewhere in initializeGame():
        Vector2 cloudPosition = new Vector2(100, 50); // Adjust position as needed
        List<Block> cloudBlocks = Cloud.create(windowDimensions, Constants.CLOUD_CYCLE);
        for (Block block : cloudBlocks) {
            this.gameObjects().addGameObject(block, Constants.CLOUD_LAYER);
        }

    }


    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        // Clamp the avatar's position within window dimensions
        clampAvatarPosition();
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
