package objectsInGame;

import java.io.*;

/**
 * Quản lý High Score.
 * Singleton pattern để đảm bảo chỉ có 1 instance.
 */
public class HighScoreManager {
    private static HighScoreManager instance;
    private static final String HIGHSCORE_FILE = "highscore.dat";

    private int highScore;

    private HighScoreManager() {
        loadHighScore();
    }

    public static HighScoreManager getInstance() {
        if (instance == null) {
            instance = new HighScoreManager();
        }
        return instance;
    }

    /**
     * Load high score từ file.
     */
    private void loadHighScore() {
        try {
            File file = new File(HIGHSCORE_FILE);
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line = reader.readLine();
                if (line != null) {
                    highScore = Integer.parseInt(line.trim());
                }
                reader.close();
                System.out.println("✓ Loaded high score: " + highScore);
            } else {
                highScore = 0;
                System.out.println("No high score file found, starting with 0");
            }
        } catch (Exception e) {
            System.err.println("Error loading high score: " + e.getMessage());
            highScore = 0;
        }
    }

    /**
     * Lưu high score vào file.
     */
    private void saveHighScore() {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(HIGHSCORE_FILE));
            writer.println(highScore);
            writer.close();
            System.out.println("✓ Saved high score: " + highScore);
        } catch (Exception e) {
            System.err.println("Error saving high score: " + e.getMessage());
        }
    }

    /**
<<<<<<< HEAD
     * Cập nhật high score nếu score mới cao hơn
     *
     * @param newScore điểm mới
     * @return true nếu đạt high score mới
=======
     * Cập nhật high score nếu score mới cao hơn.
     * @param newScore điểm mới.
     * @return true nếu đạt high score mới.
>>>>>>> 029424d0357d73b2758c8b869a52cc700f771e56
     */
    public boolean updateHighScore(int newScore) {
        if (newScore > highScore) {
            highScore = newScore;
            saveHighScore();
            return true;
        }
        return false;
    }

    /**
     * Lấy high score hiện tại.
     */
    public int getHighScore() {
        return highScore;
    }

    /**
     * Reset high score về 0.
     */
    public void resetHighScore() {
        highScore = 0;
        saveHighScore();
    }
}
