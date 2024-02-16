package gremlins;

import processing.core.PImage;
import processing.core.PApplet;

import java.util.*;
import java.util.stream.Collectors;
import java.io.*;

/**
 * Speed potion powerup
 */
public class Speed extends Powerup{
    /**
     * Speed potion constructor
     * @param x X position of speed potion
     * @param y Y position of speed potion
     * @param sprite Speed potion sprite
     */
    public Speed(int x, int y, PImage sprite) {
        this.x = x;
        this.y = y;
        this.sprite = sprite;
        this.tileState = state.idle;
    }
    /**
     * Sets the wizard's speed to 4
     * @param w Wizard class, that's speed must be changed.
     */
    public void speedBoost(Wizard w) {
        w.setSpeed(4);
    }
}
