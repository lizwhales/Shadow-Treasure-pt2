import bagel.Image;
import bagel.util.Point;

/**
 *  A bullet, can be shot from player to kill zombies
 *
 *  Some code adapted from project1_samplesolution.zip at:
 *  modules Project-1 SWEN200030-S1-2021
 *
 */

public class Bullet {

    // Image file
    public static final String FILENAME = "res/images/shot.png";
    // Speed
    public static final double BULLET_STEP_SIZE = 25;
    // Direction
    public Point pos;
    private double directionX;
    private double directionY;
    // Sets visibility
    private boolean visible;
    // image and type
    private final Image image;

    /**
     * Getter allows Player to use getDirectionX
     *
     * @return directionX
     */
    public double getDirectionX() {
        return directionX;
    }

    /**
     *  Getter allows Player to use getDirectionY
     *
     * @return directionY
     */
    public double getDirectionY() {
        return directionY;
    }

    /**
     * Gets position of bullet
     * @return this.pos
     */
    public Point getPos() {
        return this.pos;
    }

    /**
     * Constructor for Bullet to initialize x,y & visibility
     *
     * @param x double Bullet's x-coordinate as double
     * @param y double Bullet's y-coordinate as double
     */
    public Bullet(double x, double y) {
        this.image = new Image(FILENAME);
        this.pos = new Point(x, y);
        this.visible = false;
    }

    /**
     * Points to bullet's destination
     *
     * @param dest point Destination of bullet
     */
    public void pointTo(Point dest) {
        this.directionX = dest.x - this.pos.x;
        this.directionY = dest.y - this.pos.y;
        normalizeD();
    }

    /**
     * Normalizes bullet's direction
     */
    public void normalizeD() {
        double len = Math.sqrt(Math.pow(this.directionX, 2) + Math.pow(this.directionY, 2));
        this.directionX /= len;
        this.directionY /= len;
    }

    /**
     * Shows visibility state of bullet
     *
     * @return Boolean value of bullet's visibility
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Set's bullet's visibilty state
     *
     * @param visiblility boolean Value showing visibility
     */
    public void setVisible(boolean visiblility) {
        this.visible = visiblility;
    }

    /**
     * Renders bullet image if being shot/visible
     */
    public void draw() {
        if (visible) {
            image.drawFromTopLeft(pos.x, pos.y);
        }
    }

    /**
     * Checks if zombie is within killing range
     *
     * @param zombie Zombie's location
     * @return Boolean if the zombie is within DEAD_RANGE
     */
    public boolean killsZombie(Zombie zombie) {
        boolean withinRange = false;

        // Checks if zombie is visible
        // If visible, checks if player is within
        // the range to shoot a bullet
        if (isVisible()) {
            double distanceToZombie = zombie.getPos().distanceTo(pos);
            if (distanceToZombie < ShadowTreasureComplete.DEAD_RANGE) {
                withinRange = true;
            }
        }
        return withinRange;
    }
}
