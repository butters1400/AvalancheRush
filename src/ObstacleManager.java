
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
public class ObstacleManager {
	 private ArrayList<Obstacle> obstacles;
	    private Random random;
	    private int frameCount;
	    private int passedCount;
	    private double currentSpeedMultiplier = 1.0;

	    public ObstacleManager() {
	        obstacles = new ArrayList<>();
	        random = new Random();
	        frameCount = 0;
	    }

	    public void update() {
	        frameCount++;
	        if (frameCount >= 30) {
	            spawnObstacle();
	            frameCount = 0;
	        }

	        passedCount = 0;
	        Iterator<Obstacle> it = obstacles.iterator();
	        while (it.hasNext()) {
	            Obstacle obstacle = it.next();
	            obstacle.update();

	            if (obstacle.hasPassedPlayer(500)) { // Approximate player Y position
	                passedCount++;
	            }

	            if (obstacle.isOffscreen()) {
	                it.remove();
	            }
	        }
	    }

	    public void draw(Graphics g) {
	        for (Obstacle obstacle : obstacles) {
	            obstacle.draw(g);
	        }
	    }

	    private void spawnObstacle() {
	        int x = random.nextInt(760); // 800 - obstacle width
	        Obstacle obstacle = new Obstacle(x);
	        obstacle.setSpeedMultiplier(currentSpeedMultiplier);
	        obstacles.add(obstacle);
	    }

	    public boolean checkCollision(Player player) {
	        for (Obstacle obstacle : obstacles) {
	            if (obstacle.getBounds().intersects(player.getBounds())) {
	                return true;
	            }
	        }
	        return false;
	    }

	    public int getPassedObstacles() {
	        return passedCount;
	    }

	    public void clear() {
	        obstacles.clear();
	        frameCount = 0;
	        passedCount = 0;
	    }

	    public void setSpeed(double multiplier) {
	        this.currentSpeedMultiplier = multiplier;
	        // Update speed for existing obstacles
	        for (Obstacle obstacle : obstacles) {
	            obstacle.setSpeedMultiplier(multiplier);
	        }
	    }
	}

