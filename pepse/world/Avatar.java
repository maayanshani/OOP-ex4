package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.Constants;

import java.awt.event.KeyEvent;

public class Avatar extends GameObject {
    private static final float VELOCITY_X = 400;
    private static final float VELOCITY_Y = -650;
    private static final float GRAVITY = 600;
    private static final float ANIMATION_FRAME_DURATION = 0.1f; // Time between animation frames

    private final UserInputListener inputListener;
    private final AnimationRenderable idleAnimation;
    private final AnimationRenderable runAnimation;
    private final AnimationRenderable jumpAnimation;
    private float energy;
    private boolean facingLeft = false;
    private Runnable onJumpCallback;

    public Avatar(Vector2 topLeftCorner, UserInputListener inputListener, ImageReader imageReader) {
        super(topLeftCorner, Vector2.ONES.mult(Constants.AVATAR_SIZE), null);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GRAVITY);

        this.inputListener = inputListener;
        this.energy = Constants.MAX_ENERGY;

        // Load animations
        this.idleAnimation = createAnimation(imageReader, Constants.AVATAR_IDLE_PATH_FORMAT,
                Constants.NUM_IDLE_FRAMES);
        this.runAnimation = createAnimation(imageReader, Constants.AVATAR_RUN_PATH_FORMAT,
                Constants.NUM_RUN_FRAMES);
        this.jumpAnimation = createAnimation(imageReader, Constants.AVATAR_JUMP_PATH_FORMAT,
                Constants.NUM_JUMP_FRAMES);

        // Set initial renderable to idle animation
        this.renderer().setRenderable(idleAnimation);
    }

    private AnimationRenderable createAnimation(ImageReader imageReader, String basePath, int frameCount) {
        Renderable[] frames = new Renderable[frameCount];
        for (int i = 0; i < frameCount; i++) {
            frames[i] = imageReader.readImage(String.format(basePath, i), true);
        }
        return new AnimationRenderable(frames, ANIMATION_FRAME_DURATION);
    }

    private boolean canJump() {
        return energy >= Constants.JUMP_ENERGY;
    }

    private boolean canRun() {
        return energy >= Constants.RUN_ENERGY;
    }

    private boolean runState() {
        return transform().getVelocity().x() != 0;
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

    public float getEnergy() {
        return energy;
    }

    public void addEnergy(float energyToAdd) {
        this.energy += energyToAdd;
    }

    private void updateAnimation(boolean facingLeft) {
        if (transform().getVelocity().x() != 0) {
            renderer().setRenderable(runAnimation);
        } else if (transform().getVelocity().y() != 0) {
            renderer().setRenderable(jumpAnimation);
        } else {
            renderer().setRenderable(idleAnimation);
        }
        renderer().setIsFlippedHorizontally(facingLeft);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        float xVel = 0;

        // Handle horizontal movement
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT) && canRun()) {
            xVel -= VELOCITY_X;
            facingLeft = true;
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT) && canRun()) {
            xVel += VELOCITY_X;
            facingLeft = false;
        }
        transform().setVelocityX(xVel);
        if (runState()){
            updateEnergy(Constants.RUN);
        }

        // Handle jumping
        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) && transform().getVelocity().y() == 0 && canJump()) {
            transform().setVelocityY(VELOCITY_Y);
            updateEnergy(Constants.JUMP);

            if (onJumpCallback != null) {
                onJumpCallback.run();
            }
        }

        // Handle idle state
        if (idleState()) {
            updateEnergy(Constants.IDLE);
        }
        // update the animation
        updateAnimation(facingLeft);
    }

    public void setOnJumpCallback(Runnable onJumpCallback) {
        this.onJumpCallback = onJumpCallback;
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if(other.getTag().equals(Constants.GROUND)){
            this.transform().setVelocityY(0);
        }
    }

}
