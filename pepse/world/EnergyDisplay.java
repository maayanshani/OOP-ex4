/**
 * Represents an energy display in the game, showing the current energy level of an entity.
 * The display dynamically updates its text and color based on the energy level.
 */
package pepse.world;

import danogl.GameObject;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import pepse.util.Constants;

import java.awt.*;
import java.util.function.Supplier;

/**
 * Represents a visual display for tracking energy levels in the game.
 * The EnergyDisplay dynamically updates its displayed text and color
 * based on the energy level provided by an external supplier.
 *
 * - The text changes to reflect the current energy level.
 * - The color of the text changes to:
 *   - Green: High energy (>= 50% of max energy).
 *   - Yellow: Medium energy (< 50% and >= 20% of max energy).
 *   - Red: Low energy (< 20% of max energy).
 *
 * This class is designed to be flexible, allowing integration with any entity
 * that provides an energy level via a `Supplier<Float>` interface.
 */
public class EnergyDisplay extends GameObject {
    /** Supplier providing the current energy level. */
    private final Supplier<Float> energySupplier;

    /** The text renderable used to display the energy level. */
    private final TextRenderable textRenderable;

    /**
     * Constructs a new EnergyDisplay object.
     *
     * @param topLeftCorner The top-left corner of the energy display's position.
     * @param energySupplier A supplier providing the current energy level.
     */
    public EnergyDisplay(Vector2 topLeftCorner, Supplier<Float> energySupplier) {
        super(topLeftCorner, Vector2.ONES.mult(Constants.ENERGY_TEXT_SIZE), null);

        // Initialize the TextRenderable
        this.textRenderable = new TextRenderable(
                String.format(Constants.ENERGY_TEXT_FORMAT, Constants.MAX_ENERGY),
                Font.SANS_SERIF, false, true);
        this.textRenderable.setColor(Color.GREEN); // Default color
        this.renderer().setRenderable(textRenderable);

        this.energySupplier = energySupplier;

        // Initial update
        updateText(energySupplier.get());
    }

    /**
     * Updates the energy display each frame, dynamically adjusting the text and color.
     *
     * @param deltaTime The time elapsed since the last frame.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        // Dynamically update the text and color
        updateText(energySupplier.get());
    }

    /**
     * Updates the text and color of the energy display based on the current energy level.
     *
     * @param energy The current energy level to display.
     */
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
