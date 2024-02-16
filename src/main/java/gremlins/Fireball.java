package gremlins;
import processing.core.PImage;
import processing.core.PApplet;
/**
 * Fireball/Slimeball class, used for the wizard and gremlins
 */
public class Fireball extends Character {
    /**
     * Fireball constructor
     * @param x X position of the fireball
     * @param y Y position of the fireball
     * @param direction String direction of the fireball (up, down, left, right)
     * @param sprite Sprite of fireball
     */
    public Fireball(int x, int y, String direction, PImage sprite) {
        this.x = x;
        this.y = y;
        this.sprite = sprite;
        this.direction = direction;
        this.speed = 4;
    }
    /**
     * Draws the fireball, and moves it across the screen
     * @param app Parsed by App.java class
     */
    public void draw(PApplet app) {
        app.image(this.sprite, this.x, this.y);
        x += xVel;
        y += yVel;
    }

    /**
     * Automatically moves the fireball, based on the direction it was created in (up, down, left, right).
     */
    public void move() {
        if (this.direction == "right") {
            right();
        }
        else if (this.direction == "left") {
            left();
        }
        if (this.direction == "down") {
            down();
        }
        if (this.direction == "up") {
            up();
        }
    }
}
