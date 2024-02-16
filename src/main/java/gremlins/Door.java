package gremlins;

import processing.core.PImage;
import processing.core.PApplet;

import java.util.*;
import java.util.stream.Collectors;
import java.io.*;
/**
 * Door powerup
 * 
 * Door class used both for exiting the level, and teleporting the player to an empty tile.
 */
public class Door extends Powerup{
    public boolean teleportable;
    /**
     * Door constructor
     * @param x X position of the door
     * @param y Y position of the door
     * @param sprite Door's sprite
     * @param teleportable Boolean used to determine if the door has the ability to teleport the player, to another tile.
     */
    public Door(int x, int y, PImage sprite, boolean teleportable) {
        this.x = x;
        this.y = y;
        this.sprite = sprite;
        this.tileState = state.idle;
        this.teleportable = teleportable;
    }
    /**
     * Selects the x and y coords of a random empty tile, for the player to teleport to
     * @return Array of size 2, first entry containing the X value of the empty tile, second entry containing the Y value.
     */
    public int[] teleport() {
        //Filtered Arraylist of empty tiles in the map2 object. 
        List<Brick> emptyTiles= App.map2.inMinecrap.stream().filter(b -> b.getSprite().contains("Empty")).collect(Collectors.toList());
        Random rand = new Random();
        Brick emptyTile = emptyTiles.get(rand.nextInt(emptyTiles.size()));
        int[] coords = new int[2];
        coords[0] = emptyTile.getX();
        coords[1] = emptyTile.getY();
        return coords;
    }
}
