package sound;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Quản lý tất cả âm thanh trong game.
 * Singleton pattern để đảm bảo chỉ có 1 instance.
 */
public class SoundManager {
    private static SoundManager instance;

    // Map lưu trữ các sound effects (âm thanh ngắn).
    private Map<String, AudioClip> soundEffects;

    // MediaPlayer cho background music.
    private MediaPlayer bgMusicPlayer;

    // Settings.
    private boolean soundEnabled = true;
    private boolean musicEnabled = true;
    private boolean gameSoundEnabled = true; // Âm thanh phá gạch, powerup...

    // Volume levels.
    private double soundVolume = 0.5;
    private double musicVolume = 0.3;

    /**
     * Private constructor cho Singleton.
     */
    private SoundManager() {
        soundEffects = new HashMap<>();
        loadSounds();
    }

    /**
     * Lấy instance duy nhất của SoundManager.
     */
    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    /**
     * Load tất cả âm thanh vào memory.
     * AudioClip phù hợp cho sound effects vì:
     * - Load toàn bộ vào memory.
     * - Play ngay lập tức, latency thấp.
     * - Có thể play nhiều lần đồng thời.
     */
    private void loadSounds() {
        try {
            // === UI SOUNDS ===
            loadSound("button_hover", "/sound/Menu Selection Click.wav");
            loadSound("button_click", "/sound/click.wav");

            // === GAME SOUNDS ===
            loadSound("brick_break_normal", "/sound/click.wav");
            loadSound("brick_break_metal", "/sound/Metal Click.wav");

            loadSound("ball_bounce", "/sounds/game/bounce.wav");
            loadSound("powerup_drop", "/sounds/game/powerup_drop.wav");
            loadSound("powerup_pickup", "/sounds/game/powerup_pickup.wav");
            loadSound("shoot", "/sounds/game/shoot.wav");
            loadSound("explosion", "/sounds/game/explosion.wav");
            loadSound("game_over", "/sounds/game/game_over.wav");
            loadSound("level_complete", "/sounds/game/level_complete.wav");

            System.out.println("Loaded all sound effects successfully");

        } catch (Exception e) {
            System.err.println("Warning: Some sounds could not be loaded");
            e.printStackTrace();
        }
    }

    /**
     * Load một sound effect vào map.
     */
    private void loadSound(String name, String path) {
        try {
            URL resource = getClass().getResource(path);
            if (resource != null) {
                AudioClip clip = new AudioClip(resource.toString());
                soundEffects.put(name, clip);
            } else {
                System.err.println("Sound not found: " + path);
            }
        } catch (Exception e) {
            System.err.println("Failed to load sound: " + path);
        }
    }

    /**
     * Load background music
     * MediaPlayer phù hợp cho music vì:
     * - Stream từ file, tiết kiệm memory.
     * - Hỗ trợ pause/resume/loop.
     * - Phù hợp với file audio dài.
     */
    public void loadBackgroundMusic(String path) {
        try {
            URL resource = getClass().getResource(path);
            if (resource != null) {
                // Cleanup old player nếu có.
                if (bgMusicPlayer != null) {
                    bgMusicPlayer.stop();
                    bgMusicPlayer.dispose();
                }

                Media media = new Media(resource.toString());
                bgMusicPlayer = new MediaPlayer(media);
                bgMusicPlayer.setVolume(musicVolume);
                bgMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop vô hạn.

                System.out.println("Loaded background music: " + path);
            }
        } catch (Exception e) {
            System.err.println("Failed to load background music: " + path);
            e.printStackTrace();
        }
    }

    // ==================== PLAY METHODS ====================

    /**
     * Play sound effect (UI sounds).
     * Chỉ play nếu soundEnabled = true.
     */
    public void playSound(String name) {
        if (!soundEnabled) return;

        AudioClip clip = soundEffects.get(name);
        if (clip != null) {
            clip.setVolume(soundVolume);
            clip.play();
        }
    }

    /**
     * Play game sound effect (phá gạch, powerup...).
     * Chỉ play nếu gameSoundEnabled = true.
     */
    public void playGameSound(String name) {
        if (!gameSoundEnabled || !soundEnabled) return;

        AudioClip clip = soundEffects.get(name);
        if (clip != null) {
            clip.setVolume(soundVolume);
            clip.play();
        }
    }

    /**
     * Play background music.
     */
    public void playBackgroundMusic() {
        if (bgMusicPlayer != null && musicEnabled) {
            bgMusicPlayer.play();
        }
    }

    /**
     * Stop background music.
     */
    public void stopBackgroundMusic() {
        if (bgMusicPlayer != null) {
            bgMusicPlayer.stop();
        }
    }

    /**
     * Pause background music.
     */
    public void pauseBackgroundMusic() {
        if (bgMusicPlayer != null) {
            bgMusicPlayer.pause();
        }
    }

    /**
     * Resume background music.
     */
    public void resumeBackgroundMusic() {
        if (bgMusicPlayer != null && musicEnabled) {
            bgMusicPlayer.play();
        }
    }

    // ==================== SETTINGS ====================

    public void setSoundEnabled(boolean enabled) {
        this.soundEnabled = enabled;
    }

    public void setMusicEnabled(boolean enabled) {
        this.musicEnabled = enabled;
        if (bgMusicPlayer != null) {
            if (enabled) {
                bgMusicPlayer.play();
            } else {
                bgMusicPlayer.pause();
            }
        }
    }

    public void setGameSoundEnabled(boolean enabled) {
        this.gameSoundEnabled = enabled;
    }

    public void setSoundVolume(double volume) {
        this.soundVolume = Math.max(0, Math.min(1, volume));
    }

    public void setMusicVolume(double volume) {
        this.musicVolume = Math.max(0, Math.min(1, volume));
        if (bgMusicPlayer != null) {
            bgMusicPlayer.setVolume(this.musicVolume);
        }
    }

    // ==================== GETTERS ====================

    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    public boolean isMusicEnabled() {
        return musicEnabled;
    }

    public boolean isGameSoundEnabled() {
        return gameSoundEnabled;
    }

    public double getSoundVolume() {
        return soundVolume;
    }

    public double getMusicVolume() {
        return musicVolume;
    }

    /**
     * Cleanup khi thoát game.
     */
    public void dispose() {
        if (bgMusicPlayer != null) {
            bgMusicPlayer.stop();
            bgMusicPlayer.dispose();
        }
        soundEffects.clear();
    }
}