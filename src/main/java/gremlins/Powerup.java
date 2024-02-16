package gremlins;

import processing.core.PImage;
import processing.core.PApplet;
/**
 * Abstract class for powerups in the game
 */
public abstract class Powerup {
    int x;
    int y;
    PImage sprite;
    boolean activated = false;
    // Animation variables
    private int counter; 
    enum state {
        idle,
        start,
        finish
    }
    state tileState; 
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
     * Returns the x tile the character is in.
     * @return X divided by 20 (sprites are 20X20)
     */
    public int get_Tile_x() {
        return x / 20;
    }
    /**
     * Returns the y tile the character is in.
     * @return Y divided by 20 (sprites are 20X20)
     */
    public int get_Tile_y() {
        return y / 20;
    }
    /**
     * Returns activated boolean
     * @return Boolean that states if the powerup has been activated.
     */
    public boolean getActivation() {
        return activated;
    }
    /**
     * Draws the powerup
     * 
     * Contains code to calculate powerup activation for 10 seconds, using an enum.
     * @param app Parsed by App.java class
     */
    public void draw(PApplet app) {
        // Handling graphics
        if (this.tileState == state.idle) {
            app.image(this.sprite, this.x, this.y);
        } else if (this.tileState == state.start) {
            this.activated = true;
            if (this.counter < 10 * 60) {
                app.image(App.background_tile, this.x, this.y);
            } else {
                this.tileState = state.finish;
            }
            this.counter++;
        } else if (this.tileState == state.finish) {
            app.image(this.sprite, this.x, this.y);
            this.counter = 0;
            this.activated = false;
        }
    }
    /**
     * Powerup collision method with wizard
     * 
     * Checks if the wizard tile, and the powerup tile, are both equal to each other 
     * @param w Wizard class
     * @return True, there was collision, False, there was no collision
     */
    public boolean checkPowerUpCollision(Wizard w) {
        if(w.get_Tile_x() == this.get_Tile_x() && w.get_Tile_y() == this.get_Tile_y()) {
            this.tileState = state.start;
            return true;
        }
        else {
            return false;
        }
    }
}
