package gremlins;

import java.util.*;

import processing.core.PImage;
import processing.core.PApplet;

import java.io.*;
/**
 * Map class
 * 
 * Generates the map, and stores game objects found in the map txt files (Bricks, gremlins, powerups)
 */
public class Map {
    public ArrayList<Brick> inMinecrap = new ArrayList<>();
    public ArrayList<Gremlin> enemies = new ArrayList<>();
    public ArrayList<Fireball> enemyFireballs = new ArrayList<Fireball>();
    public ArrayList<Speed> speedPotions = new ArrayList<Speed>();
    public ArrayList<Door> doors = new ArrayList<Door>();

    public int startX = 0;
    public int startY = 0;
    /**
     * Reads and generates the game map, based on a level.txt file
     * @param levelLayout txt file, containing the map layout, that the function generates.
     */
    public void readMap(File levelLayout) {
        Scanner scan;
        int lineNum;
        inMinecrap.clear();
        enemies.clear();
        enemyFireballs.clear();
        try {
            scan = new Scanner(levelLayout);
            lineNum = 0;
            while(scan.hasNextLine()) {
                String[] line = scan.nextLine().split("");
                for(int i = 0; i < line.length; i++) {
                    if (line[i].equals("W")) {
                        startX = i*20;
                        startY = lineNum*20;
                        this.inMinecrap.add(new Brick(i*20, lineNum*20, "Empty", App.background_tile));
                    }
                    else if(line[i].equals("X")) {
                        this.inMinecrap.add(new Brick(i*20, lineNum*20, "Stone", App.stonewall));
                    }
                    else if(line[i].equals("B")) {
                        this.inMinecrap.add(new Brick(i*20, lineNum*20, "Brick", App.brickwall));
                    }
                    else if(line[i].equals("G")) {
                        Gremlin g = new Gremlin(i*20, lineNum*20, App.gremlin, this.enemyFireballs);
                        this.enemies.add(g);
                        this.inMinecrap.add(new Brick(i*20, lineNum*20, "Empty",App.background_tile));
                    } else if (line[i].equals(" ")) {
                        this.inMinecrap.add(new Brick(i*20, lineNum*20, "Empty",App.background_tile));
                    } else if (line[i].equals("E")) {
                        this.doors.add(new Door(i*20, lineNum*20, App.door, false));
                        this.inMinecrap.add(new Brick(i*20, lineNum*20, "Empty", App.background_tile));
                    }
                    else if (line[i].equals("T")) {
                        this.doors.add(new Door(i*20, lineNum*20, App.door, true));
                        this.inMinecrap.add(new Brick(i*20, lineNum*20, "Empty", App.background_tile));
                    }
                    else if (line[i].equals("S")) {
                        this.speedPotions.add(new Speed(i*20, lineNum*20, App.potion));
                        this.inMinecrap.add(new Brick(i*20, lineNum*20, "Empty", App.background_tile));
                    }
                }
                lineNum += 1;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
    }
    /**
     * Creates an empty map with background tiles
     * 
     * Function called when the player wins the game, or there is a game over.
     */
    public void emptyMap() {
        startX = 9*20;
        startY = 9*20;
        for(int i = 0; i < 36; i++) {
            for(int j = 0; j < 33; j ++) {
                this.inMinecrap.add(new Brick(i*20, j*20, "Stone", App.background_tile));
            }
        }
    }
    /**
     * Draws all the objects in the map file.
     * @param app Parsed by App.java class
     */
    public void draw(PApplet app) {
        for(Brick b : this.inMinecrap) {
            b.draw(app);
        }
        for(Gremlin g : this.enemies) {
            g.draw(app);
        }
        for(Speed s : this.speedPotions) {
            s.draw(app);
        }
        for(Door d : this.doors) {
            d.draw(app);
        }
    }
}
