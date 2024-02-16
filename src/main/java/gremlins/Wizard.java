package gremlins;

import processing.core.PImage;
import processing.core.PApplet;
/**
 * Wizard class, player character that is controlled by the user
 */
public class Wizard extends Character {
    //String direction = "right";
    /**
     * Wizard constructor
     * 
     * Direction right by default
     * @param x X position of wizard
     * @param y Y position of wizard
     * @param sprite Sprite of the wizard
     */
    public Wizard(int x, int y, PImage sprite) {
        this.x = x;
        this.y = y;
        this.sprite = sprite;
        this.direction = "right";
    }
    /**
     * Move wizard right
     * 
     * Sets direction to right, and sprite to wizard facing right
     */
    public void right() {
        this.xVel = speed;
        this.direction = "right";
        this.sprite = App.wizRight;
    }
    /**
     * Move wizard left
     * 
     * Sets direction to left, and sprite to wizard facing left
     */
    public void left() {
        this.xVel = -speed;
        this.direction = "left";
        this.sprite = App.wizLeft;
    }
    /**
     * Move wizard up
     * 
     * Sets direction to up, and sprite to wizard facing up
     */
    public void up() {
        this.yVel = -speed;
        //this.setSprite(App.wizUp);
        this.direction = "up";
        this.sprite = App.wizUp;
    }
    /**
     * Move wizard down
     * 
     * Sets direction to down, and sprite to wizard facing down
     */
    public void down() {
        this.yVel = speed;
        this.direction = "down";
        this.sprite = App.wizDown;
    }
    /**
     * Sets wizard sprite
     * @param sprite New sprite of wizard
     */
    public void setSprite(PImage sprite) {
        this.sprite = sprite;
    }

    /**
     * Draw method of wizard
     * 
     * Contains code to move wizard, into a 20X20 game tile.
     * @param app Parsed by App.java class
     */
    public void draw(PApplet app) {
        // Handling graphics
        app.image(this.sprite, this.x, this.y);
        
        if (xVel == 0 && x % 20 != 0) {
            if (direction.equals("right")) {
                x += speed;
            } else if (direction.equals("left")) {
                x -= speed;
            } else {
                System.out.println("Edge case? - Check code for x position");
            }
        } else if (yVel == 0 && y % 20 != 0) {
            if (direction.equals("down")) {
                y += speed;
            } else if (direction.equals("up")) {
                y -= speed;
            } else {
                System.out.println("Edge case? - Check code for y position");
            }
        } else {
            x += xVel;
            y += yVel;
        }
    }
}
