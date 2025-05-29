

import java.io.*;
import java.util.*;
import javax.swing.JOptionPane;
public class Leaderboard {
	private static final String FILENAME = "leaderboard.txt";
    private static final int MAX_SCORES = 10;

    public void addScore(String name, int score) {
        ArrayList<Score> scores = loadScores();
        scores.add(new Score(name, score));
        Collections.sort(scores);

        while (scores.size() > MAX_SCORES) {
            scores.remove(scores.size() - 1);
        }

        saveScores(scores);
    }

    public void showLeaderboard() {
        ArrayList<Score> scores = loadScores();
        StringBuilder sb = new StringBuilder("Top Scores:\n\n");

        for (int i = 0; i < scores.size(); i++) {
            Score score = scores.get(i);
            sb.append(String.format("%d. %s: %d\n", i + 1, score.name, score.score));
        }

        JOptionPane.showMessageDialog(null, sb.toString(), "Leaderboard", JOptionPane.INFORMATION_MESSAGE);
    }

    private ArrayList<Score> loadScores() {
        ArrayList<Score> scores = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILENAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                scores.add(new Score(parts[0], Integer.parseInt(parts[1])));
            }
        } catch (IOException e) {
            // File doesn't exist yet or error reading
        }
        return scores;
    }

    private void saveScores(ArrayList<Score> scores) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILENAME))) {
            for (Score score : scores) {
                writer.println(score.name + "," + score.score);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class Score implements Comparable<Score> {
        String name;
        int score;

        public Score(String name, int score) {
            this.name = name;
            this.score = score;
        }

        @Override
        public int compareTo(Score other) {
            return other.score - this.score; // Sort in descending order
        }
    }
}

