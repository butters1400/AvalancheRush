
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class AvalancheRush extends JPanel implements ActionListener, KeyListener  {
	 // Window and game object dimensions
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private static final int PLAYER_WIDTH = 80;
    private static final int PLAYER_HEIGHT = 80;
    private static final int OBSTACLE_SPAWN_RATE = 30; // Spawn new obstacle every 30 frames

    // Game objects
    private Player player; // The player character
    private ObstacleManager obstacleManager;// Manages all obstacles in the game
    private Timer gameTimer; // Controls the game loop timing
    private int score; // Current game score
    private boolean gameOver; // Game state flag
    private Leaderboard leaderboard; // Handles high scores
    private BufferedImage backgroundImage; // Background image

    // Difficulty scaling
    private double difficultyMultiplier = 1.0;
    private static final double DIFFICULTY_INCREASE = 0.2; // Doubled from 0.1 to 0.2 for faster difficulty scaling
    private int framesSinceStart = 0;

    public AvalancheRush() {
        // Initialize the game window
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        setBackground(Color.WHITE);
        setFocusable(true);
        addKeyListener(this);

        // Create the player in the center bottom of the screen
        player = new Player(WINDOW_WIDTH / 2 - PLAYER_WIDTH / 2,
                WINDOW_HEIGHT - PLAYER_HEIGHT - 20,
                PLAYER_WIDTH,
                PLAYER_HEIGHT);

        // Initialize game components
        obstacleManager = new ObstacleManager();
        leaderboard = new Leaderboard();
        gameTimer = new Timer(16, this); // ~60 FPS (1000ms/60 ≈ 16ms)

        // Start the game
        startGame();

        // Load the background image
        try {
            backgroundImage = ImageIO.read(new File("images/snow background.png"));
        } catch (IOException e) {
            System.err.println("Error loading background image: " + e.getMessage());
        }
    }

    // Reset game state for a new game
    private void startGame() {
        score = 0;
        gameOver = false;
        obstacleManager.clear();
        gameTimer.start();
    }

    // Main game loop - called every frame
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            framesSinceStart++;

            // Increase difficulty every 5 seconds (300 frames at 60 FPS)
            if (framesSinceStart % 300 == 0) { // Changed from 600 to 300 frames
                difficultyMultiplier += DIFFICULTY_INCREASE;
                obstacleManager.setSpeed(difficultyMultiplier);
            }

            // Update game objects
            player.update();
            obstacleManager.update();

            // Check for collisions
            if (obstacleManager.checkCollision(player)) {
                gameOver();
            }

            // Update score based on passed obstacles
            score += obstacleManager.getPassedObstacles();

            // Redraw the game
            repaint();
        }
    }

    // Handle game over state
    private void gameOver() {
        gameOver = true;
        gameTimer.stop();

        // Get player name and save score
        String name = JOptionPane.showInputDialog("Game Over! Enter your name:");
        if (name != null && !name.trim().isEmpty()) {
            leaderboard.addScore(name, score);
            leaderboard.showLeaderboard();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw background
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT, null);
        }

        // Draw player
        player.draw(g);

        // Draw obstacles
        obstacleManager.draw(g);

        // Draw score
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 20, 30);

        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            String gameOverText = "Game Over! Score: " + score;
            int textWidth = g.getFontMetrics().stringWidth(gameOverText);
            g.drawString(gameOverText, WINDOW_WIDTH / 2 - textWidth / 2, WINDOW_HEIGHT / 2);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        player.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        player.keyReleased(e);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Avalanche Rush");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new AvalancheRush());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}


