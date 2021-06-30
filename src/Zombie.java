import bagel.Image;
import bagel.util.Point;

/**
 * A zombie, reduces energy and ends game when in contact
 * with player. Can be killed by player with a bullet.
 *
 * Some code adapted from project1_samplesolution.zip at:
 * modules Project-1 SWEN200030-S1-2021
 *
 */

public class Zombie{

    // Image of the zombie
    private final Image image = new Image("res/images/zombie.png");
    // Visibility of the zombie
    private boolean visible;
    // Render position
    private Point pos;

    /**
     * Constructor for zombie to initialize x,y
     *
     * @param x double Zombie's x coordinate
     * @param y double Zombie's y coordinate
     */
    public Zombie(double x, double y){
        this.pos = new Point(x,y);
        this.visible = true;
    }

    /**
     * Getter to gain position of zombie
     *
     * @return Zombie's position
     */
    public Point getPos() {
        return pos;
    }

    /**
     * Shows visibility state of zombie
     *
     * @return Boolean value of zombie's visibility
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Set's zombie's visibilty state
     *
     * @param visiblility boolean Value showing visibility
     */
    public void setVisible(boolean visiblility) {
        this.visible = visiblility;
    }

    /**
     * Draws sandwich image if it hasn't been eaten
     */
    public void draw() {
        if (visible) {
            image.drawFromTopLeft(pos.x, pos.y);
        }
    }

    /**
     * Checks if Zombie is in shooting range of the player,
     * and if the zombie is still alive/ visible
     *
     * @param player Player's location
     * @return Boolean value if player is within shooting range
     * of the zombie
     */
    public boolean inRange(Player player) {
        boolean shootRange = false;
        double distanceToPlayer = player.getPos().distanceTo(pos);

        if (distanceToPlayer <= ShadowTreasureComplete.SHOOTING_RANGE && visible) {
            shootRange = true;
        }
        return shootRange;
    }

    /**
     * Checks if player has come into contact with a zombie
     *
     * @param player Player's location
     * @return Boolean if player has met a zombie
     */
    public boolean meets(Player player) {
        boolean hasMet = false;
        double distanceToPlayer = player.getPos().distanceTo(pos);

        if (distanceToPlayer < ShadowTreasureComplete.ClOSENESS){
            hasMet = true;
        }
        return hasMet;
    }
}
