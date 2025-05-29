
import java.awt.*;
import java.awt.event.KeyEvent;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;

// Represents the player character that can move left and right
public class Player {
	private int x, y; // Position coordinates
    private int width, height; // Player dimensions
    private int speed = 12; // Movement speed (pixels per frame)
    private boolean movingLeft, movingRight; // Movement state flags
    private BufferedImage sprite; // Player image

    public Player(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        try {
            sprite = ImageIO.read(new File("images/player sprite.png"));
        } catch (IOException e) {
            System.err.println("Error loading player sprite: " + e.getMessage());
        }
    }

    // Update player position based on movement flags
    public void update() {
        if (movingLeft && x > 0) {
            x -= speed; // Move left if not at screen edge
        }
        if (movingRight && x < 800 - width) {
            x += speed; // Move right if not at screen edge
        }
    }

    // Draw the player sprite or fallback rectangle
    public void draw(Graphics g) {
        if (sprite != null) {
            g.drawImage(sprite, x, y, width, height, null);
        } else {
            // Fallback to rectangle if image fails to load
            g.setColor(Color.BLUE);
            g.fillRect(x, y, width, height);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            movingLeft = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            movingRight = true;
        }
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            movingLeft = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            movingRight = false;
        }
    }
}

