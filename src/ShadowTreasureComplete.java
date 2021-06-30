import bagel.*;
import java.util.ArrayList;
import java.io.*;
import java.text.DecimalFormat;

/**
 * ShadowTreasure, a graphical simulation.
 *
 * Some code adapted from project1_samplesolution.zip at:
 * modules Project-1 SWEN200030-S1-2021
 *
 * @author Elizabeth Wong
 */

public class ShadowTreasureComplete extends AbstractGame {

    // Background image
    private final Image BACKGROUND = new Image("res/images/background.png");
    // Distance range for player to meet any game objects
    public static final int ClOSENESS = 50;
    // Distance range for player to shoot a zombie
    public static final int SHOOTING_RANGE = 150;
    // Distance range for bullet to kill a zombie
    public static final int DEAD_RANGE = 25;

    // For rounding double number
    private static DecimalFormat df = new DecimalFormat("0.00");

    // Tick cycle and var
    private final int TICK_CYCLE = 10;
    private int tick;

    // List of characters
    private Player player;
    private Sandwich sandwich;
    private Zombie zombie;
    private Treasure treasure;
    private Bullet bullet;

    // End of game indicator
    private boolean endOfGame;

    /**
     * Looks for IO Exception and handles it
     *
     * @throws IOException
     */
    public ShadowTreasureComplete() throws IOException {
        this.loadEnvironment("res/IO/environment.csv");
        this.tick = 1;
        this.endOfGame = false;
    }

    // Arraylist that stores positions of each zombie and sandwich
    ArrayList<Zombie> zombieList = new ArrayList<Zombie>(1);
    ArrayList<Sandwich> sandList = new ArrayList<Sandwich>(1);

    /**
     * Returns zombie from zombieList
     *
     * @return the zombie position
     */
    public Zombie getZombie() {
        for (Zombie zombie : zombieList) {
            return zombie;
        }
        return zombie;
    }

    /**
     * Sets game status as ongoing or ended
     *
     * @param endOfGame boolean State of end of the game
     */
    public void setEndOfGame(boolean endOfGame) {
        this.endOfGame = endOfGame;
    }

    /**
     * Loads input to get positions of zombie, player, sandwich and treasure
     *
     * @param filename string Environment.csv file
     */
    private void loadEnvironment(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String type = parts[0];
                type = type.replaceAll("[^a-zA-Z0-9]", ""); // remove special characters
                int x = Integer.parseInt(parts[1]);
                int y = Integer.parseInt(parts[2]);
                switch (type) {
                    case "Player" -> this.player = new Player(x, y, Integer.parseInt(parts[3]));
                    case "Zombie" -> zombieList.add(new Zombie(x, y)); // adds from file to zombieList
                    case "Sandwich" -> sandList.add(new Sandwich(x, y)); // add from file to sandList
                    case "Treasure" -> this.treasure = new Treasure(x, y);
                    default -> throw new BagelError("Unknown type: " + type);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Performs a state update every 10 ticks
     *
     * @param input The current keyboard state
     */
    @Override
    public void update(Input input) {
        if (this.endOfGame || input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        } else {
            // Draw background
            BACKGROUND.drawFromTopLeft(0, 0);
            // Update status when the TICK_CYCLE is up
            if (tick > TICK_CYCLE) {
                // Update player status
                player.update(this);
                tick = 1;
            }
            tick++;
            // Draws sandwiches and zombies from their arraylists
            for (Sandwich sandwich : sandList) {
                sandwich.draw();
            }
            for (Zombie zombie : zombieList) {
                zombie.draw();
            }
            player.render();
            treasure.draw();
        }
    }

    /**
     * The entry point for the program
     *
     * @param args Optional command-line arguments
     * @throws IOException Handles IO Exceptions
     */
    public static void main(String[] args) throws IOException {
        ShadowTreasureComplete game = new ShadowTreasureComplete();
        game.run();
    }
}

