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
import pepse.world.Avatar;
import pepse.world.Block;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;

import java.util.List;

public class PepseGameManager extends GameManager {
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
        Vector2 windowDimensions = windowController.getWindowDimensions();

        //
        // add sky:
        GameObject sky = Sky.create(windowDimensions);
        this.gameObjects().addGameObject(sky, Constants.SKY_LAYER);

        // add terrain:
        Terrain terrain = new Terrain(windowDimensions, Constants.RANDOM_SEED);
        List<Block> blockList = terrain.createInRange(0, (int) windowDimensions.x());
        for (Block block : blockList) {
            // TODO: LAYERS
            this.gameObjects().addGameObject(block, Layer.STATIC_OBJECTS);
        }

        // add light and darkness cycle:
        GameObject night = Night.create(windowDimensions, Constants.DAY_LONG);
        this.gameObjects().addGameObject(night, Constants.NIGHT_LAYER);

        // add sun:
        GameObject sun = Sun.create(windowDimensions, Constants.DAY_LONG);
        this.gameObjects().addGameObject(sun, Constants.SUN_LAYER);

        // add avatar
        Vector2 startLocationAvatar = new Vector2(windowDimensions.x() / 2 , 0);
        Avatar avatar = new Avatar(startLocationAvatar, inputListener, imageReader);
        this.gameObjects().addGameObject(avatar);


    }

    public static void main(String[] args) {
        new PepseGameManager().run();

    }
}
