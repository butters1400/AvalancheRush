import java.awt.Rectangle;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import java.io.IOException;
import javax.imageio.ImageIO;

// Represents obstacles (rocks and trees) that fall from the top of the screen
public class Obstacle {
	 private int x, y; // Position coordinates
	    private int width = 60; // Obstacle width
	    private int height = 60; // Obstacle height
	    private int speed = 8; // Increased from 5 to 8 for faster falling obstacles
	    private boolean counted = false; // Flag for score counting
	    private BufferedImage sprite; // Current obstacle sprite
	    private double speedMultiplier = 1.0;// For difficulty scaling
	    private static BufferedImage rockSprite; // Shared rock image
	    private static BufferedImage treeSprite; // Shared tree image
	    private boolean isRock; // Type of obstacle

	    public Obstacle(int x) {
	        this.x = x;
	        this.y = -height;
	        // Randomly choose between rock and tree
	        isRock = Math.random() < 0.5;

	        // Load sprites if not already loaded
	        if (rockSprite == null) {
	            try {
	                rockSprite = ImageIO.read(new File("images/2dRock.png"));
	            } catch (IOException e) {
	                System.err.println("Error loading rock sprite: " + e.getMessage());
	            }
	        }
	        if (treeSprite == null) {
	            try {
	                treeSprite = ImageIO.read(new File("images/AR tree.png"));
	            } catch (IOException e) {
	                System.err.println("Error loading tree sprite: " + e.getMessage());
	                e.printStackTrace();
	            }
	        }

	        sprite = isRock ? rockSprite : treeSprite;
	    }

	    // Update obstacle position
	    public void update() {
	        y += speed * speedMultiplier; // Move downward at current speed
	    }

	    public void setSpeedMultiplier(double multiplier) {
	        this.speedMultiplier = multiplier;
	    }

	    public void draw(Graphics g) {
	        if (sprite != null) {
	            g.drawImage(sprite, x, y, width, height, null);
	        } else {
	            // Fallback to rectangle if image fails to load
	            g.setColor(isRock ? Color.GRAY : Color.GREEN);
	            g.fillRect(x, y, width, height);
	        }
	    }

	    public Rectangle getBounds() {
	        return new Rectangle(x, y, width, height);
	    }

	    // Check if obstacle has moved off screen
	    public boolean isOffscreen() {
	        return y > 600; // Past bottom of screen
	    }

	    // Check if obstacle has passed the player (for scoring)
	    public boolean hasPassedPlayer(int playerY) {
	        if (!counted && y > playerY) {
	            counted = true;
	            return true;
	        }
	        return false;
	    }
	}

