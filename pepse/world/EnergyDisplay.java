package pepse.world;

import danogl.GameObject;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import pepse.util.Constants;

import java.awt.*;
import java.util.function.Supplier;

public class EnergyDisplay extends GameObject {
    private final Supplier<Float> energySupplier;
    private final TextRenderable textRenderable;

    public EnergyDisplay(Vector2 topLeftCorner, Supplier<Float> energySupplier) {
        super(topLeftCorner, Vector2.ONES.mult(Constants.ENERGY_TEXT_SIZE), null);

        // Initialize the TextRenderable
        this.textRenderable = new TextRenderable(
                String.format(Constants.ENERGY_TEXT_FORMAT,Constants.MAX_ENERGY),
                Font.SANS_SERIF, false, true);
        this.textRenderable.setColor(Color.GREEN); // Default color
        this.renderer().setRenderable(textRenderable);

        this.energySupplier = energySupplier;

        // Initial update
        updateText(energySupplier.get());
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        // Dynamically update the text and color
        updateText(energySupplier.get());
    }

    private void updateText(float energy) {
        // Update the text value
        textRenderable.setString(String.format(Constants.ENERGY_TEXT_FORMAT, energy));

        // Update the text color based on energy level
        if (energy < 20) {
            textRenderable.setColor(Color.RED); // Low energy
        } else if (energy < 50) {
            textRenderable.setColor(Color.YELLOW); // Medium energy
        } else {
            textRenderable.setColor(Color.GREEN); // High energy
        }
    }
}
