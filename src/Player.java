import bagel.DrawOptions;
import bagel.Font;
import bagel.Image;
import bagel.util.Colour;
import bagel.util.Point;
import java.util.ArrayList;
import java.io.*;
import java.text.DecimalFormat;

/**
 *  The player, moves according to Algorithm 1
 *
 *  Some code adapted from project1_samplesolution.zip at:
 *  modules Project-1 SWEN200030-S1-2021
 *
 */

public class Player{

    // Image source file
    public static final String FILENAME = "res/images/player.png";
    // Speed of the player
    public static final double STEP_SIZE = 10;
    // Energy level threshold
    private static final int LOW_ENERGY = 3;
    // Max distance between player and objects
    public static final double MAX = 1000000000000.00;
    // Healthbar font
    private final Font FONT = new Font("res/font/DejaVuSans-Bold.ttf", 20);
    private final DrawOptions OPT = new DrawOptions();

    // Image and type
    private final Image image;
    // Render position of player
    private Point pos;
    // direction of x and y coordinates
    private double directionX;
    private double directionY;

    // For rounding double numbers within output.csv
    private static DecimalFormat df = new DecimalFormat("0.00");
    // Healthbar parameters
    private int energy;
    // Bullet parameter
    private Bullet shot;

    // Arraylist to store bullet positions when firing
    ArrayList <String> bulletList = new ArrayList<String>();

    /**
     * Player constructor to invoke player position and energy
     *
     * @param x double Player's x coordinate
     * @param y double Player's y coordinate
     * @param energy int Player's energy level
     */
    public Player(double x, double y, int energy) {
        this.image = new Image(FILENAME);
        this.pos = new Point(x,y);
        this.energy = energy;
    }

    /**
     * Getter to get player position
     *
     * @return Player's current position
     */
    public Point getPos(){
        return this.pos;
    }

    /**
     * Points to Player destination
     *
     * @param dest point Player destination
     */
    public void pointTo(Point dest){
        this.directionX = dest.x-this.pos.x;
        this.directionY = dest.y-this.pos.y;
        normalizeD();
    }

    /**
     * Normalises player's direction
     */
    public void normalizeD(){
        double len = Math.sqrt(Math.pow(this.directionX,2)+Math.pow(this.directionY,2));
        this.directionX /= len;
        this.directionY /= len;
    }

    /**
     * Looks for the closest sandwich within sandList in relation to player
     *
     * @param tomb The instance of ShadowTreasureComplete
     * @return The closest sandwich in relation to player position
     */
    public Sandwich closestSand(ShadowTreasureComplete tomb){
        double closest = MAX;
        Sandwich closestSand = null;
        // Loop through arraylist and checks
        // each distance for sandwich to player
        for(Sandwich sandwich: tomb.sandList){
            double distance = sandwich.getPos().distanceTo(pos);
            if(distance< closest){
                closest =  distance;
                // Assigns the closest sandwich to player
                closestSand = sandwich;
            }
        }
        return closestSand;
    }

    /**
     * Looks for the closest zombie within zombieList in relation to player
     *
     * @param tomb The instance of ShadowTreasureComplete
     * @return The closest zombie in relation to player position
     */
    public Zombie closestZombie(ShadowTreasureComplete tomb){
        double closest = MAX;
        Zombie closestZombie = null;
        // Loop through arraylist and checks
        // each distance for zombie to player
        for(Zombie zombie: tomb.zombieList){
            double distance = zombie.getPos().distanceTo(pos);
            if(distance< closest && zombie.isVisible()){
                closest =  distance;
                // Assigns the closest zombie to player
                closestZombie = zombie;
            }
        }
        return closestZombie;
    }

    /**
     * Updates player simulation according to Algorithm 1
     *
     * @param tomb The instance of ShadowTreasureComplete
     */
    public void update(ShadowTreasureComplete tomb) {
        // Terminates game if player reaches a zombie
        if (tomb.getZombie().meets(this)) {
            reachZombie();
            tomb.setEndOfGame(true);
        }

        // If player meets a sandwich, she eats it and gains
        // 5 energy. Sandwich disappears when eaten
        for (Sandwich sandwich : tomb.sandList) {
            if (sandwich.meets(this)) {
                eatSandwich();
                sandwich.setVisible(false);
            }
        }

        // If zombie is within shooting range, the player
        // shoots a bullet towards the zombie and reduces
        // energy by 3.
        for (Zombie zombie : tomb.zombieList) {
            if (zombie.inRange(this)) {
                if (shot == null) {
                    Bullet bullet = new Bullet(this.getPos().x, this.getPos().y);
                    bullet.setVisible(true);
                    shootZombie();
                    // Draws the bullet movement and ensures that
                    // only one bullet is being shot at a time
                    this.shot = bullet;
                    shot.draw();
                }
            }

            // If there is no bullet in current existence
            // a bullet is shot. If the bullet enters killing
            // range of the zombie, it is considered shot
            // dead and disappears from the environment
            if (shot != null) {
                if (shot.killsZombie(zombie)) {
                    zombie.setVisible(false);
                    this.shot = null;
                }
            }
        }

        // If all zombies are killed, the treasure is now reachable
        // and the player moves towards the treasure. Upon treasure
        // impact, "Success!" and player energy level is printed
        if (closestZombie(tomb) == null) {
            Treasure treasure = new Treasure(this.getPos().x, this.getPos().y);
            System.out.println("Success!, energy: " + energy);

        // If player is not low energy she moves towards the
        // closest zombie
        } else if (this.energy >= LOW_ENERGY) {
            pointTo(closestZombie(tomb).getPos());
        // When low energy player moves towards the
        // closest sandwich
        } else {
            pointTo(closestSand(tomb).getPos());
        }
        // The player moves one step towards her destination
        this.pos = new Point(this.pos.x + STEP_SIZE * this.directionX, this.pos.y + STEP_SIZE * this.directionY);

        if (shot != null) {
            shot.pointTo(closestZombie(tomb).getPos());
            shot.pos = new Point(shot.pos.x + Bullet.BULLET_STEP_SIZE * shot.getDirectionX(), shot.pos.y + Bullet.BULLET_STEP_SIZE * shot.getDirectionY());
            shot.draw();
            // Saves bullet position output when shot as a String
            String s = (df.format(shot.getPos().x) + "," + df.format(shot.getPos().y));
            // Adds each bullet position to an Arraylist
            bulletList.add(s);
        }

        // Writes Arraylist of bullet coordinates when
        // being fired to "res/IO/output.csv"
        File csvFile = new File("res/IO/output.csv");
        try (PrintWriter csvWriter = new PrintWriter(new FileWriter(csvFile));) {
            for (String item : bulletList) {
                csvWriter.println(item);
            }
        } catch (IOException e) {
            //Handle exception
            e.printStackTrace();
        }
    }

    /**
     * Renders the player's current energy level
     */
    public void render() {
        image.drawFromTopLeft(pos.x, pos.y);
        // Displays Player's current energy level
        FONT.drawString("energy: "+ energy,20,760, OPT.setBlendColour(Colour.BLACK));
    }

    /**
     * Player gains 5 energy when she eats a sandwich
     */
    public void eatSandwich(){ energy += 5; }

    /**
     * Player loses 3 energy when she comes into contact
     * with a zombie
     */
    public void reachZombie(){ energy -= 3; }

    /**
     * Player loses 3 energy when she shoots a bullet
     */
    public void shootZombie(){ energy -= 3; }
}
