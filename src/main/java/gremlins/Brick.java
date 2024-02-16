package gremlins;

import processing.core.PImage;
import processing.core.PApplet;
import java.util.*;
import java.io.*;
/**
 * Class used for creating instances of bricks and stonewalls
 */
public class Brick {

    private int x;
    private int y;
    private boolean destructable;

    private PImage sprite;

    private String sprite_name;

    // Animation variables
    private int counter; 
    enum state {
        idle,
        start,
        finish
    }
    private state tileState; 
    /**
     * Constructor method, used to create instance of the brick class.
     * @param x x position
     * @param y y position
     * @param sprite_name name of brick (Empty, Stone, Brick)
     * @param sprite Brick's sprite
     */
    public Brick(int x, int y, String sprite_name, PImage sprite) {
        this.x = x;
        this.y = y;
        this.sprite = sprite;
        this.sprite_name = sprite_name;
        if (this.sprite_name.equals("Brick")) {
            this.destructable = true;
            this.counter = 0;
        }
        else {
            this.destructable = false;
        }
        this.tileState = state.idle;
    }
    /**
     * Function used to draw the brick.
     * 
     * Contains code to display brick destroyed animation.
     * @param app Parsed by App.java class
     */
    public void draw(PApplet app) {
        // Handling graphics
        // Code for the brick destroyed animation
        if (this.tileState == state.idle) {
            app.image(this.sprite, this.x, this.y);
            
        } else if (this.tileState == state.start) {
            
            if (this.counter < 4) {
                app.image(App.destroyed[0], this.x, this.y);
            } else if (this.counter < 8) {
                app.image(App.destroyed[1], this.x, this.y);
            } else if (this.counter < 12) {
                app.image(App.destroyed[2], this.x, this.y);
            } else if (this.counter < 16) {
                app.image(App.destroyed[3], this.x, this.y);
            } else {
                this.tileState = state.finish;
                this.sprite = App.background_tile;
                setSpriteName("Empty");
            }
            this.counter++;
        } else if (this.tileState == state.finish) {
            app.image(this.sprite, this.x, this.y);
        }
    }
    /**
     * Returns the name of the sprite (Stone, Brick, Empty)
     * @return String containing the sprite name
     */
    public String getSprite(){
        return this.sprite_name;
    }
    /**
     * Returns x position
     * @return X position integer value
     */
    public int getX(){
        return this.x;
    }
    /**
     * Returns y position
     * @return Y position integer value
     */
    public int getY(){
        return this.y;
    }
    /**
     * Sets the name of the sprite
     * @param s Sprite name (Stone, Brick, Empty)
     */
    public void setSpriteName(String s) {
        this.sprite_name = s;
    }
    /**
     * Sets the sprite to be displayed
     * @param s PImage sprite object.
     */
    public void setSprite(PImage s) {
        this.sprite = s;
    }
    /**
     * Starts the brick destroyed animation
     * 
     * Sets enum 'tileState,' to 'start,' triggering the animation code, in the draw function.
     */
    public void startAnimation() {
        this.tileState = state.start;
    }

}
