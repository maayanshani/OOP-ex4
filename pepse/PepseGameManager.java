package pepse;

import danogl.GameManager;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import pepse.util.Constants;
import pepse.world.Block;
import pepse.world.Terrain;

import java.util.List;

public class PepseGameManager extends GameManager {
    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        System.out.println(windowController.getWindowDimensions());
        Terrain terrain = new Terrain(windowController.getWindowDimensions(), Constants.RANDOM_SEED);
        List<Block> blockList = terrain.createInRange(0, (int) windowController.getWindowDimensions().x());
        System.out.println(blockList.size());
        for (Block block : blockList) {
            this.gameObjects().addGameObject(block, Layer.STATIC_OBJECTS);
        }
    }

    public static void main(String[] args) {
        new PepseGameManager().run();

    }
}
