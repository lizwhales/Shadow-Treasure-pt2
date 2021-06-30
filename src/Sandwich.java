import bagel.Image;
import bagel.util.Point;

/**
 * A sandwich, can be eaten by the player to gain 3 energy
 *
 * Some code adapted from project1_samplesolution.zip at:
 * modules Project-1 SWEN200030-S1-2021
 *
 */

public class Sandwich{
    // Image of the sandwich
    private final Image image = new Image("res/images/sandwich.png");
    // Renders position
    private Point pos;
    // Visibility state of the sandwich
    private boolean visible;

    /**
     * Constructor for sandwich to initialize x,y
     *
     * @param x double X coordinate of sandwich
     * @param y double Y coordinate of sandwich
     */
    public Sandwich(double x, double y){
        this.pos = new Point(x,y);
        this.visible = true;
    }

    /**
     * Getter to get position of sandwich
     *
     * @return position of the sandwich
     */
    public Point getPos() {
        return pos;
    }

    /**
     * Shows visibility state of sandwich
     *
     * @return Boolean value of sandwich's visibility
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Set's sandwich's visibilty state
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
     * Checks if player has come into contact with a sandwich
     *
     * @param player Player's location
     * @return Boolean if player has met a sandwich
     */
    public boolean meets(Player player) {
        boolean hasMet = false;

        if (isVisible()){
            double distanceToPlayer = player.getPos().distanceTo(pos);
            if (distanceToPlayer < ShadowTreasureComplete.ClOSENESS) {
                hasMet = true;
            }
        }
        return hasMet;
    }
}
