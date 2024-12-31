package pepse.world;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.ImageReader;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.Constants;

import java.awt.*;

public class Rain{

    public static GameObject create(ImageReader imageReader, Vector2 topLeftCorner, float cycleLength) {
        // TODO: maybe get callback instead of creat Terrain?
        Renderable renderable = imageReader.readImage(Constants.WATER_DROP_IMAGE_PATH, true);

        GameObject waterDrop = new GameObject(
                topLeftCorner,
                new Vector2(Constants.WATER_DROP_SIZE, Constants.WATER_DROP_SIZE),
                renderable);
        waterDrop.setTag(Constants.WATER_DROP);
        Vector2 waterDropPosition = topLeftCorner;

        // Add vertical movement to the cloud
        new Transition<Float>(
                waterDrop,
                (Float y) -> waterDrop.setTopLeftCorner(new Vector2(waterDropPosition.x(), y)),
                waterDropPosition.y(), // Start at the initial vertical position
                waterDropPosition.y()*4, // Move down by a range
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                cycleLength,
                Transition.TransitionType.TRANSITION_ONCE, // Move up and down
                null // No additional behavior after transition
        );

        // add transition, no need to use the object created
        new Transition<Float>(
                waterDrop, // the game object being changes
                waterDrop.renderer()::setOpaqueness, // the method to call
                1f, // initial transition value
                Constants.WATER_DROP_FINAL_OPACITY, // final transition value
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                cycleLength*Constants.HALF,
                Transition.TransitionType.TRANSITION_ONCE,
//                () -> waterDrop.getGameObjectsCollection().removeGameObject(waterDrop) // Remove drop when opacity is zero
                null
        );


        return waterDrop;
    }

}
