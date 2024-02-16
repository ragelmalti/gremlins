package gremlins;

import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONObject;
import processing.data.JSONArray;
import processing.event.KeyEvent;

import java.lang.Math;

import java.util.*;
import java.util.stream.Collectors;
import java.io.*;

/** 
 * Main class, where the game code is written.
 * 
 * In this class, contains most of the game logic, such as character movement, drawing and loading sprites and objects.
*/
public class App extends PApplet {

    public static final int WIDTH = 720;
    public static final int HEIGHT = 720;
    public static final int SPRITESIZE = 20;
    public static final int BOTTOMBAR = 60;

    public static final int SPACE_KEY = 32;

    public static final int FPS = 60;

    public static final Random randomGenerator = new Random();

    public String configPath;
    public static JSONObject conf;
    private static boolean startCountdown = false;
    
    public static PImage brickwall;
    public static PImage stonewall;
    public static PImage background_tile;

    public static PImage brickwall_destoryed0;
    public static PImage brickwall_destoryed1;
    public static PImage brickwall_destoryed2;
    public static PImage brickwall_destoryed3;
    public static PImage[] destroyed = new PImage[4];

    public static PImage gremlin;
    public static PImage fireball;
    public static PImage slime;
    
    public static PImage door;
    public static PImage potion;

    public static PImage wizUp;
    public static PImage wizDown;
    public static PImage wizRight;
    public static PImage wizLeft;

    private Wizard wizard;
    public int lives;
    private float wiz_cooldown;
    private double wizCooldownTimer;
    public File levelLayout;
    public int levelNum;
    public static int levelCount = 0;
    public static Map map2;
    public ArrayList<Fireball> fireballs = new ArrayList<>();

    public static float enemy_cooldown;

    public App() {
        this.configPath = "config.json";
        this.conf = loadJSONObject(new File(this.configPath));
        this.lives = conf.getInt("lives");
    }

    /**
     * Initialise the setting of the window size.
    */
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * Load all resources such as images. Initialise the elements such as the player, enemies and map elements.
    */
    public void setup() {
        
        fireballs.clear();
        frameRate(FPS);

        // Loading information from the JSON file.
        JSONArray levels = this.conf.getJSONArray("levels");
        println("Number of levels: " + levels.size());
        this.levelNum = levels.size() - 1;
        if(levelCount <= levelNum) {
            JSONObject item = levels.getJSONObject(levelCount);
            String level = item.getString("layout");
            System.out.println(level);
            this.levelLayout = new File(level);
            this.wiz_cooldown = item.getFloat("wizard_cooldown");
            this.enemy_cooldown = item.getFloat("enemy_cooldown");
            this.wizCooldownTimer = Math.ceil(wiz_cooldown * 60);
        }

        // Load images during setup
        this.stonewall = loadImage(this.getClass().getResource("stonewall.png").getPath().replace("%20", " "));
        this.brickwall = loadImage(this.getClass().getResource("brickwall.png").getPath().replace("%20", " "));
        
        this.destroyed[0] = loadImage(this.getClass().getResource("brickwall_destroyed0.png").getPath().replace("%20", " "));
        this.destroyed[1] = loadImage(this.getClass().getResource("brickwall_destroyed1.png").getPath().replace("%20", " "));
        this.destroyed[2] = loadImage(this.getClass().getResource("brickwall_destroyed2.png").getPath().replace("%20", " "));
        this.destroyed[3] = loadImage(this.getClass().getResource("brickwall_destroyed3.png").getPath().replace("%20", " "));

        this.background_tile = loadImage(this.getClass().getResource("background_tile.png").getPath().replace("%20", " "));
        this.gremlin = loadImage(this.getClass().getResource("gremlin.png").getPath().replace("%20", " "));
        this.fireball = loadImage(this.getClass().getResource("fireball.png").getPath().replace("%20", " "));
        this.slime = loadImage(this.getClass().getResource("slime.png").getPath().replace("%20", " "));
        this.wizUp = loadImage(this.getClass().getResource("wizard2.png").getPath().replace("%20", " "));
        this.wizDown = loadImage(this.getClass().getResource("wizard3.png").getPath().replace("%20", " "));
        this.wizLeft = loadImage(this.getClass().getResource("wizard0.png").getPath().replace("%20", " "));
        this.wizRight = loadImage(this.getClass().getResource("wizard1.png").getPath().replace("%20", " "));
        
        this.door = loadImage(this.getClass().getResource("door.png").getPath().replace("%20", " "));
        this.potion = loadImage(this.getClass().getResource("potion.png").getPath().replace("%20", " "));

        // Creating the map object
        this.map2 = new Map();


        //Generating the map from the txt file, or creating an empty map, when the player dies/wins.
        if(levelCount <= levelNum && lives > 0) {
            map2.readMap(levelLayout);
            for (Gremlin g : map2.enemies) {
                if (checkCollision(g) == true) {
                    Random random = new Random();
                    int num = random.nextInt(4);                    
                    switch(num) {
                        case 0:
                            gremlinNewDirection(g, "up");
                            break;
                        case 1:
                            gremlinNewDirection(g, "down");
                            break;
                        case 2:
                            gremlinNewDirection(g, "right");
                            break;
                        case 3:
                            gremlinNewDirection(g, "left");
                            break;
                    }
                }
            }
        }
        else {
            map2.emptyMap();
        }
        // Creating the wizard object
        this.wizard = new Wizard(map2.startX, map2.startY, wizRight);
    }

    /**
     * Receive key pressed signal from the keyboard.
     * 
     * Used to control an instance of the wizard.
    */

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        int x_position = this.wizard.get_Tile_x();
        int y_position = this.wizard.get_Tile_y();
        int index = y_position * 36 + x_position;
        int right_of_player = index + 1;
        int left_of_player = index - 1;
        int up_of_player = index - 36 ;
        int down_of_player = index + 36;
        String rightSprite = map2.inMinecrap.get(right_of_player).getSprite();
        String leftSprite = map2.inMinecrap.get(left_of_player).getSprite();
        String upSprite = map2.inMinecrap.get(up_of_player).getSprite();
        String downSprite = map2.inMinecrap.get(down_of_player).getSprite();

        // Code used to check, if the wizard is between a 20X20 tile.
        if (wizard.getX() % 20 != 0 || wizard.getY() % 20 != 0) {
            return;
        }
        if (wizard.getVelX() != 0 || wizard.getVelY() != 0) {
            return;
        }

        if (key == 37) { //left arrow
            if (leftSprite.equals("Empty")) {
                wizard.left();
            }
            else {
                wizard.direction = "left";
                wizard.setSprite(wizLeft);
            }
        } else if (key == 39) { //right arrow
            if (rightSprite.equals("Empty")) {
                wizard.right();
            }
            else {
                wizard.direction = "right";
                wizard.setSprite(wizRight);
            }
        } else if (key == 38) { //up
            if (upSprite.equals("Empty")) {
                wizard.up();
            }
            else {
                wizard.direction = "up";
                wizard.setSprite(wizUp);
            }
        } else if (key == 40) { //down
            if (downSprite.equals("Empty")) {
                wizard.down();
            }
            else {
                wizard.direction = "down";
                wizard.setSprite(wizDown);
            }
        } 
        else if (key == SPACE_KEY) {
            if(startCountdown == false) {
                this.fireballs.add(new Fireball(wizard.getX(), wizard.getY(), wizard.getDirection(), this.fireball));
                startCountdown = true;
            }
        }
    }

    /**
     * Receive key released signal from the keyboard.
    */
    public void keyReleased(KeyEvent e){
        int key = e.getKeyCode();
        if (key == 37 && wizard.getDirection().equals("left") || key == 39 && wizard.getDirection().equals("right") || key == 38 && wizard.getDirection().equals("up") || key == 40 && wizard.getDirection().equals("down")) {
            wizard.stop();
        }
    }


    /**
     * Checks if a character has collided with any of the brick tiles. 
     * 
     * The function checks 1 tile above the character, below the character, to the right of the character, and too the left of the character.
     * @param c Character to check collision with
     * @return Returns True if collision detected, or false if no collision detected.
	 */
    public boolean checkCollision(Character c) {
        if (c.getX() % 20 != 0 || c.getY() % 20 != 0) {
            return false;
        } 
        int index = c.get_Tile_y() * 36 + c.get_Tile_x();

        return (checkCollisionDirection(c, this.map2, index + 1, "right") ||
                checkCollisionDirection(c, this.map2, index - 1, "left") ||
                checkCollisionDirection(c, this.map2, index - 36, "up") ||
                checkCollisionDirection(c, this.map2, index + 36, "down"));
    }
    /**
     * Checks if there is a brick tile in the direction that the character is moving. 
     * @param c Character to check collision with
     * @param m Map object
     * @param index Index used to detect collision
     * @param direction Direction (up, down, left, right)
     * @return True if collision, false if not
     */
    public boolean checkCollisionDirection(Character c, Map m, int index, String direction) {
        if (c.getDirection().equals(direction)) {
            String sprite = m.inMinecrap.get(index).getSprite();
            if (sprite.equals("Brick") || sprite.equals("Stone")) {
                return true;
            } 
            else if (sprite.equals("Door")) {
                return false;
            }
            else {
                return false;
            }
        } else {
            return false;
        }
    }
    /** 
     * Draws the game elements onto the screen.
     * 
     * Elements include the map, the wizard, enemies, fireballs, and powerups. Function also contains functions to check for collision with interacting game elements.
    */
     public void draw() {
         // Collision checking for multiple game objects
        if (checkCollision(this.wizard) == true) {
            wizard.stop();
        }

        for(Gremlin g : map2.enemies) {    
            if(nonGridCollision(g, wizard)) {
                setup();
                lives--;
            }
            for(Fireball f : fireballs) {
                if(nonGridCollision(f, g)) {

                    do {
                        List<Brick> emptyTiles= map2.inMinecrap.stream().filter(b -> b.getSprite().contains("Empty")).collect(Collectors.toList());
                        Random rand = new Random();
                        Brick emptyTile = emptyTiles.get(rand.nextInt(emptyTiles.size()));
                        g.x = emptyTile.getX();
                        g.y = emptyTile.getY();
                    } while (distanceBetweenTwoCharacters(g, wizard) < 20 * 10);
                    
                }
            }    
            if (checkCollision(g) == true) {
                
                Random random = new Random();
                int num = random.nextInt(4);                
                switch(num) {
                    case 0:
                        gremlinNewDirection(g, "up");
                        break;
                    case 1:
                        gremlinNewDirection(g, "down");
                        break;
                    case 2:
                        gremlinNewDirection(g, "right");
                        break;
                    case 3:
                        gremlinNewDirection(g, "left");
                        break;
                }
            }
        }

        for(Fireball f : map2.enemyFireballs) {
            if(nonGridCollision(f, wizard)) {
                setup();
                lives--;
            }
        }

        int k = 0;
        while (k < fireballs.size()) {
            int l = 0;
            boolean colliding = false;
            while (l < map2.enemyFireballs.size()) {
                if (nonGridCollision(fireballs.get(k), map2.enemyFireballs.get(l))) {
                    colliding = true;
                    map2.enemyFireballs.remove(l);
                    break;
                } else {
                    l++;
                }
            }
            if (colliding == true) {
                fireballs.remove(k);
            } else {
                k++;
            } 
        }

        for(Door d : map2.doors) {
            if(d.checkPowerUpCollision(wizard) && d.getActivation() == false && d.teleportable == false) {
                fireballs.clear();
                levelCount += 1;
                setup();
            }
            else if(d.checkPowerUpCollision(wizard) && d.getActivation() == false && d.teleportable == true) {
                int[] newWizCoords = d.teleport();
                wizard.x = newWizCoords[0];
                wizard.y = newWizCoords[1];
            }
        }

        for(Speed s: map2.speedPotions) {
            if(s.checkPowerUpCollision(wizard) && s.getActivation() == false) {
                System.out.println("lol");
                s.speedBoost(wizard);
            }
        }

        // Drawing game objects
        background(191, 153, 114);

        map2.draw(this);
        // Check player fireballs if they are colliding with the world
        fireballsCheckCollision(this.fireballs, true);
        
        // Check gremlin fireballs (same as player)
        fireballsCheckCollision(this.map2.enemyFireballs, false);
        
        
        /*   grid code - Used for debugging:
        for (int i = 0; i < 36; i++) { // Vertical lines
            line(i*20,0,i*20,660);
        }
        for (int i = 0; i <= 33; i++) { // Horizontal Lines
            line(0,i*20,720,i*20);
        }
        */
                // Draw lives counter:
                fill(255,255,255);
                text("Lives:", 20, 695);
                textSize(18);
                for (int life = 1; life <= lives; life++) {
                    image(wizRight, 50 + (life*20), 680);
                }
                //Draw level counter:
                fill(255,255,255);
                text(String.format("Level: %s/%s", levelCount + 1,levelNum + 1), 600, 695);
                fill(255,255,255);
                rect(400,680,wiz_cooldown * 60,20);
                //Wizard fireball cooldown bar
                if(this.startCountdown == true) {
        
                    fill(0,0,0);
                    rect(400,680,(float) wizCooldownTimer,20);
                    this.wizCooldownTimer--;
                    if (this.wizCooldownTimer <= 0) {
                        this.wizCooldownTimer = wiz_cooldown * 60;
                        this.startCountdown = false;
                    }
                }
        this.wizard.draw(this);
    
        // Draw game over and win screen
        if(lives<=0) {
            background(191, 153, 114);
            fill(255,255,255);
            textSize(100);
            text("GAME OVER", 75,350);
        }
        else if(levelCount > levelNum) {
            background(191, 153, 114);
            fill(255,255,255);
            textSize(100);
            text("YOU WIN", 120,350);
        }
    }

    /**
     * Checks if the fireball has hit a brick tile.
     * @param fireballs An arraylist of all the fireballs that collision needs to be checked for.
     * @param destructable Boolean that determines if fireballs should destroy red bricks (E.g. Gremlin fireballs can't destroy redbricks, whilst the wizard's can.)
     */
    public void fireballsCheckCollision(ArrayList<Fireball> fireballs, boolean destructable) {
        int i = 0;
        while (i < fireballs.size()) {
            Fireball f = fireballs.get(i);
            f.draw(this);
            
            f.move();
            if (checkCollision(f) == true) {
                f.stop();
                fireballs.remove(i);
                if (f.getDirection().equals("right")) {
                    int index = f.get_Tile_y() * 36 + f.get_Tile_x();
                    Brick wall = map2.inMinecrap.get(index + 1);
                    if(wall.getSprite().equals("Brick") && destructable == true) {
                        wall.startAnimation();
                    }
                }
                else if (f.getDirection().equals("left")) {
                    int index = f.get_Tile_y() * 36 + f.get_Tile_x();
                    Brick wall = map2.inMinecrap.get(index - 1);
                    if(wall.getSprite().equals("Brick") && destructable == true) {
                        wall.startAnimation();
                    }
                }
                else if (f.getDirection().equals("up")) {
                    int index = f.get_Tile_y() * 36 + f.get_Tile_x();
                    Brick wall = map2.inMinecrap.get(index - 36);
                    if(wall.getSprite().equals("Brick") && destructable == true) {
                        wall.startAnimation();
                    }
                }
                else if (f.getDirection().equals("down")) {
                    int index = f.get_Tile_y() * 36 + f.get_Tile_x();
                    Brick wall = map2.inMinecrap.get(index + 36);
                    if(wall.getSprite().equals("Brick") && destructable == true) {
                        wall.startAnimation();
                    }
                }

              
            } else {
                i++;
            }
        }
    }
    /**
     * Non Grid based collision, used for moving entities (characters).
     * 
     * Utilises the Axis-Aligned Bounding Box (AABB) collision method, to check for collision between moving characters.
     * @param c1 The first character
     * @param c2 The second character
     * @return True if characters have collided, false if they haven't.
     */
    public boolean nonGridCollision(Character c1, Character c2) {
        int sprite_width = 20;
        return (c1.x < c2.x + sprite_width &&
                c1.x + sprite_width > c2.x &&
                c1.y < c2.y + sprite_width &&
                c1.y + sprite_width > c2.y);
    }


    /**
     * Returns the distance between two characters
     * 
     * Uses the distance formula
     * @param c1 First character
     * @param c2 Second character
     * @return The result of the distance formula calculation
     */
    public float distanceBetweenTwoCharacters(Character c1, Character c2) {
        int dx = c1.x - c2.x;
        int dy = c1.y - c2.y;
        float result = (float) Math.sqrt((dx * dx) + (dy * dy));
        return result;
    }

    /**
     * Selects a new direction, for the Gremlin to move to.
     * @param g Gremlin who's direction needs changing.
     * @param direction The Gremlin's current direction.
     */
    public void gremlinNewDirection(Gremlin g, String direction){
        g.stop();
        g.pickDirection(direction);
        while (checkCollision(g) == true) {
            g.stop();
            g.next();
        } 
    }

    public static void main(String[] args) {
        PApplet.main("gremlins.App");     
    }
}
