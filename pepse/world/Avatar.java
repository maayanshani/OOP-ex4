package pepse.world;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.Constants;
import java.awt.*;
import java.awt.event.KeyEvent;


// todo: need to stop at the edges? (walls)

public class Avatar extends GameObject {
    private static final float VELOCITY_X = 400;
    private static final float VELOCITY_Y = -650;
    private static final float GRAVITY = 600;
    private static final Color AVATAR_COLOR = Color.DARK_GRAY;

    private UserInputListener inputListener;
    private float energy;

    public Avatar(Vector2 topLeftCorner,
                  UserInputListener inputListener,
                  ImageReader imageReader) {

        super(topLeftCorner, Vector2.ONES.mult(50),
                imageReader.readImage(Constants.AVATAR_IMAGE_PATH, false));
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GRAVITY);
        this.inputListener = inputListener;
        this.energy = Constants.MAX_ENERGY;
    }

    private boolean canJump() {
        return energy >= Constants.JUMP_ENERGY;
    }

    private boolean canRun() {
        return energy >= Constants.RUN_ENERGY;
    }

    private boolean idleState() {
        return transform().getVelocity().equals(Vector2.ZERO);
    }

    private void updateEnergy(String action) {
        switch (action) {
            case Constants.RUN -> energy -= Constants.RUN_ENERGY;
            case Constants.JUMP -> energy -= Constants.JUMP_ENERGY;
            case Constants.IDLE -> energy += Constants.IDLE_ENERGY;
            default -> {}
        }
        if (energy > Constants.MAX_ENERGY) {
            energy = Constants.MAX_ENERGY;
        }
    }

    // TODO: added to API
    public float getEnergy() {
        return energy;
    }

    public void addEnergy(float energyToAdd) {
        this.energy += energyToAdd;
    }


    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float xVel = 0;
        if(inputListener.isKeyPressed(KeyEvent.VK_LEFT) && canRun()){
            xVel -= VELOCITY_X;
            updateEnergy(Constants.RUN);
        }
        if(inputListener.isKeyPressed(KeyEvent.VK_RIGHT) && canRun()){
            xVel += VELOCITY_X;
            updateEnergy(Constants.RUN);
        }
        transform().setVelocityX(xVel);
        if(inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() == 0 && canJump()) {
            transform().setVelocityY(VELOCITY_Y);
            updateEnergy(Constants.JUMP);
        }
        if (idleState()) {
            updateEnergy(Constants.IDLE);
        }
    }

}