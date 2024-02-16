package gremlins;

import processing.core.PImage;
import processing.core.PApplet;

/**
 * Abstract class, used for moving game objects
 * 
 * Includes Wizard, Gremlins and fireballs
 */
public abstract class Character {
    int x;
    int y;
    int xVel = 0;
    int yVel = 0;
    int speed = 2;
    String direction;
    PImage sprite;

    /**
     * Moves the character right
     */
    public void right() {
        this.xVel = speed;
        this.yVel = 0;
        this.direction = "right";
    }
    /**
     * Moves the character left
     */
    public void left() {
        this.xVel = -speed;
        this.yVel = 0;
        this.direction = "left";
    }
    /**
     * Moves the character up
     */
    public void up() {
        this.xVel = 0;
        this.yVel = -speed;
        this.direction = "up";
    }
    /**
     * Moves the character down
     */
    public void down() {
        this.xVel = 0;
        this.yVel = speed;
        this.direction = "down";
    }
    /**
     * Stops the character from moving
     */
    public void stop() {
        this.xVel = 0;
        this.yVel = 0;
    }
    /**
     * Gets character direction
     * @return returns direction as string (up, down, left, right)
     */
    public String getDirection() {
        return this.direction;
    }
     /**
     * Returns x position
     * @return X position integer value
     */
    public int getX() {
        return this.x;
    }
    /**
     * Returns y position
     * @return Y position integer value
     */
    public int getY() {
        return this.y;
    }
    /**
     * Returns x velocity.
     * @return X velocity integer value
     */
    public int getVelX() {
        return this.xVel;
    }
    /**
     * Returns y velocity.
     * @return Y velocity integer value
     */
    public int getVelY() {
        return this.yVel;
    }
    /**
     * Returns the x tile the character is in.
     * @return X divided by 20 (sprites are 20X20)
     */
    public int get_Tile_x() {
        return x / 20;
    }
    /**
     * Sets the speed of the character.
     * @param speed Integer value containing speed.
     */
    public void setSpeed(int speed) {
        this.speed = speed;
    }
    /**
     * Returns the y tile the character is in.
     * @return Y divided by 20 (sprites are 20X20)
     */
    public int get_Tile_y() {
        return y / 20;
    }
}
