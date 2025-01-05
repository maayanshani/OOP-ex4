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

/**
 * Represents the player's avatar in the game world.
 * The avatar can move, jump, and interact with the environment, while managing its energy and animations.
 *
 * - Movement includes running and jumping, with configurable velocities and gravity.
 * - Energy management allows for depletion and regeneration based on actions like running, jumping, or idling.
 * - Animations dynamically update based on the avatar's current state (idle, running, or jumping).
 * - Supports a callback mechanism to notify other components when the avatar jumps.
 *
 * This class provides a comprehensive implementation for controlling the player's character
 * in a dynamic and interactive environment.
 */
public class Avatar extends GameObject {
    /** The horizontal velocity of the avatar when moving left or right. */
    private static final float VELOCITY_X = 400;

    /** The initial vertical velocity of the avatar when jumping. */
    private static final float VELOCITY_Y = -650;

    /** The acceleration due to gravity applied to the avatar. */
    private static final float GRAVITY = 600;

    /** The duration of each frame in the animation sequences. */
    private static final float ANIMATION_FRAME_DURATION = 0.1f;

    /** The input listener to capture user input for controlling the avatar. */
    private final UserInputListener inputListener;

    /** Animation for the idle state of the avatar. */
    private final AnimationRenderable idleAnimation;

    /** Animation for the running state of the avatar. */
    private final AnimationRenderable runAnimation;

    /** Animation for the jumping state of the avatar. */
    private final AnimationRenderable jumpAnimation;

    /** The current energy level of the avatar, used for managing actions like running and jumping. */
    private float energy;

    /** Indicates whether the avatar is currently facing left. */
    private boolean facingLeft = false;

    /** A callback to be executed when the avatar performs a jump. */
    private Runnable onJumpCallback;


    /**
     * Constructs an Avatar object.
     *
     * @param topLeftCorner The initial position of the avatar.
     * @param inputListener Handles user input for controlling the avatar.
     * @param imageReader Reads images for creating animations.
     */
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

    /**
     * Creates an animation renderable using the given images.
     *
     * @param imageReader Reads the animation frames.
     * @param basePath The base path for the animation images.
     * @param frameCount The number of frames in the animation.
     * @return The constructed AnimationRenderable.
     */
    private AnimationRenderable createAnimation(ImageReader imageReader, String basePath, int frameCount) {
        Renderable[] frames = new Renderable[frameCount];
        for (int i = 0; i < frameCount; i++) {
            frames[i] = imageReader.readImage(String.format(basePath, i), true);
        }
        return new AnimationRenderable(frames, ANIMATION_FRAME_DURATION);
    }

    /**
     * Checks if the avatar has enough energy to jump.
     *
     * @return True if the avatar can jump, false otherwise.
     */
    private boolean canJump() {
        return energy >= Constants.JUMP_ENERGY;
    }

    /**
     * Checks if the avatar has enough energy to run.
     *
     * @return True if the avatar can run, false otherwise.
     */
    private boolean canRun() {
        return energy >= Constants.RUN_ENERGY;
    }

    /**
     * Checks if the avatar is in a running state.
     *
     * @return True if the avatar is running, false otherwise.
     */
    private boolean runState() {
        return transform().getVelocity().x() != 0;
    }

    /**
     * Checks if the avatar is in an idle state.
     *
     * @return True if the avatar is idle, false otherwise.
     */
    private boolean idleState() {
        return transform().getVelocity().equals(Vector2.ZERO);
    }

    /**
     * Updates the avatar's energy based on the given action.
     *
     * @param action The action being performed (e.g., run, jump, idle).
     */
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

    /**
     * Gets the current energy level of the avatar.
     *
     * @return The avatar's energy level.
     */
    public float getEnergy() {
        return energy;
    }

    /**
     * Adds energy to the avatar's energy level.
     *
     * @param energyToAdd The amount of energy to add.
     */
    public void addEnergy(float energyToAdd) {
        this.energy += energyToAdd;
    }

    /**
     * Updates the animation based on the avatar's current state and direction.
     *
     * @param facingLeft True if the avatar is facing left, false otherwise.
     */
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

    /**
     * Updates the avatar's state each frame, handling movement, energy updates, and animations.
     *
     * @param deltaTime The time elapsed since the last frame.
     */
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

    /**
     * Sets a callback to be executed when the avatar jumps.
     *
     * @param onJumpCallback The callback to execute on a jump event.
     */
    public void setOnJumpCallback(Runnable onJumpCallback) {
        this.onJumpCallback = onJumpCallback;
    }

    /**
     * Handles collisions with other game objects.
     *
     * @param other The other game object involved in the collision.
     * @param collision Details about the collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if(other.getTag().equals(Constants.GROUND) || other.getTag().equals(Constants.TRUNK)){
            this.transform().setVelocityY(0);
        }
        if(other.getTag().equals(Constants.TRUNK)){
            System.out.println("Stopping avatar on " + other.getTag()); // Debug log
//            Vector2 avatarPosition = this.transform().getTopLeftCorner();
//            avatarPosition = new Vector2(avatarPosition.x(), other.transform().getTopLeftCorner().y() - this.getDimensions().y());
//            this.transform().setTopLeftCorner(avatarPosition);
        }
    }
}
