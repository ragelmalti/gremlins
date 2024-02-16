package gremlins;

import processing.core.PImage;
import processing.core.PApplet;
import java.util.*;
import java.io.*;

/**
 * Gremlin class, the enemy of the wizard
 */
public class Gremlin extends Character {
    
    private float cooldownTimer;
    public ArrayList<Fireball> fireballs;
    /**
     * Gremlin constructor
     * 
     * By default, the Gremlin's speed is 1, and it to the right.
     * @param x X position of the Gremlin
     * @param y X position of the Gremlin
     * @param sprite Sprite of Gremlin
     * @param fireballs Arraylist storing the Gremlins fireballs
     */
    public Gremlin(int x, int y, PImage sprite, ArrayList<Fireball> fireballs) {
        this.x = x;
        this.y = y;
        this.yVel = 0;
        this.xVel = 1;
        this.sprite = sprite;
        this.direction = "right";
        this.speed = 1;
        this.cooldownTimer = App.enemy_cooldown * 60;
        this.fireballs = fireballs;
    }
    /**
     * Draws the gremlin
     * 
     * Contains code for calculating the Gremlin's slimeball cooldown
     * @param app Parsed by App.java class
     */
    public void draw(PApplet app) {
        // Handling graphics
        app.image(this.sprite, this.x, this.y);
        if (yVel > 0) {
            this.direction = "down";
        } else if (yVel < 0) {
            this.direction = "up";
        } else if (xVel > 0) {
            this.direction = "right";
        } else if (xVel < 0) {
            this.direction = "left";
        }


        y += yVel;
        x += xVel;
        this.cooldownTimer--;
        if (this.cooldownTimer <= 0 && this.x % 20 == 0 && this.y % 20 == 0) {
            this.cooldownTimer = App.enemy_cooldown * 60;
            this.fireballs.add(new Fireball(getX(), getY(), getDirection(), App.slime));
        }
    }
    /**
     * Changes the direction of the Gremlin, when it hits a tile.
     */
    public void next(){
        if(this.direction.equals("up")) {
            this.right();
        } else if (this.direction.equals("right")){
            this.down();
        } else if (this.direction.equals("down")){
            this.left();
        } else if (this.direction.equals("left")){
            this.up();
        }
    }
    /**
     * Moves the gremlin, based on the direction picked
     * @param direction String direction (up, down, left, right)
     */
    public void pickDirection(String direction) {
        if (direction.equals("up")) {
            up();
        } else if (direction.equals("right")) {
            right();
        } else if (direction.equals("down")) {
            down();
        } else if (direction.equals("left")) {
            left();
        } else {
            System.out.println("We never should be here");
        }
    }
}