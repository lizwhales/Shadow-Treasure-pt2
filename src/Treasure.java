import bagel.Image;
import bagel.util.Point;

/**
 * A treasure, becomes reachable when all zombies are killed
 *
 * Some code adapted from project1_samplesolution.zip at:
 * modules Project-1 SWEN200030-S1-2021
 *
 */

public class Treasure {
    // Image of the treasure
    private final Image image = new Image("res/images/treasure.png");
    // Renders position of treasure
    private Point pos;

    /**
     * Constructor for treasure to initialize x,y
     *
     * @param x double Treasure's x coordinate
     * @param y double Treasure's y coordinate
     */
    public Treasure(double x, double y){
        this.pos = new Point(x,y);
    }

    /**
     * Draws the image of the treasure from given location
     */
    public void draw() {
            image.drawFromTopLeft(pos.x, pos.y);
    }
}
