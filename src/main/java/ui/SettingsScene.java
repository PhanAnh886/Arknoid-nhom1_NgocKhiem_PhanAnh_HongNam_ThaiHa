package ui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.image.Image;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javagraphicmain.Main;
import sound.SoundManager;

/**
 * Settings Scene với đầy đủ chức năng điều chỉnh âm thanh
 */
public class SettingsScene {
    private Scene scene;
    private SoundManager soundManager;

    public SettingsScene(Main mainApp) {
        soundManager = SoundManager.getInstance();

        // Title
        Text title = new Text("SETTINGS");
        title.setFont(new Font("Arial", 48));
        title.setFill(Color.WHITE);
        title.setStyle("-fx-font-weight: bold;");

        // ========== UI SOUND SETTINGS ==========
        Text uiSoundTitle = new Text("UI Sounds (Button Click/Hover)");
        uiSoundTitle.setFont(new Font("Arial", 24));
        uiSoundTitle.setFill(Color.LIGHTBLUE);

        CheckBox soundCheckBox = new CheckBox("Enable UI Sounds");
        soundCheckBox.setTextFill(Color.WHITE);
        soundCheckBox.setFont(new Font("Arial", 18));
        soundCheckBox.setSelected(soundManager.isSoundEnabled());
        soundCheckBox.setOnAction(e -> {
            soundManager.setSoundEnabled(soundCheckBox.isSelected());
            soundManager.playSound("button_click");
        });

        // Volume slider for UI sounds
        Label soundVolumeLabel = new Label("UI Sound Volume: " +
                String.format("%.0f%%", soundManager.getSoundVolume() * 100));
        soundVolumeLabel.setTextFill(Color.WHITE);
        soundVolumeLabel.setFont(new Font("Arial", 16));

        Slider soundVolumeSlider = new Slider(0, 1, soundManager.getSoundVolume());
        soundVolumeSlider.setShowTickLabels(true);
        soundVolumeSlider.setShowTickMarks(true);
        soundVolumeSlider.setMajorTickUnit(0.25);
        soundVolumeSlider.setBlockIncrement(0.1);
        soundVolumeSlider.setPrefWidth(300);
        soundVolumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            soundManager.setSoundVolume(newVal.doubleValue());
            soundVolumeLabel.setText("UI Sound Volume: " +
                    String.format("%.0f%%", newVal.doubleValue() * 100));
        });

        // ========== GAME SOUND SETTINGS ==========
        Text gameSoundTitle = new Text("Game Sounds (Brick Break, PowerUp...)");
        gameSoundTitle.setFont(new Font("Arial", 24));
        gameSoundTitle.setFill(Color.LIGHTGREEN);

        CheckBox gameSoundCheckBox = new CheckBox("Enable Game Sounds");
        gameSoundCheckBox.setTextFill(Color.WHITE);
        gameSoundCheckBox.setFont(new Font("Arial", 18));
        gameSoundCheckBox.setSelected(soundManager.isGameSoundEnabled());
        gameSoundCheckBox.setOnAction(e -> {
            soundManager.setGameSoundEnabled(gameSoundCheckBox.isSelected());
            if (gameSoundCheckBox.isSelected()) {
                soundManager.playGameSound("brick_break");
            }
        });

        // ========== MUSIC SETTINGS ==========
        Text musicTitle = new Text("Background Music");
        musicTitle.setFont(new Font("Arial", 24));
        musicTitle.setFill(Color.GOLD);

        CheckBox musicCheckBox = new CheckBox("Enable Background Music");
        musicCheckBox.setTextFill(Color.WHITE);
        musicCheckBox.setFont(new Font("Arial", 18));
        musicCheckBox.setSelected(soundManager.isMusicEnabled());
        musicCheckBox.setOnAction(e -> {
            soundManager.setMusicEnabled(musicCheckBox.isSelected());
        });

        // Volume slider for music
        Label musicVolumeLabel = new Label("Music Volume: " +
                String.format("%.0f%%", soundManager.getMusicVolume() * 100));
        musicVolumeLabel.setTextFill(Color.WHITE);
        musicVolumeLabel.setFont(new Font("Arial", 16));

        Slider musicVolumeSlider = new Slider(0, 1, soundManager.getMusicVolume());
        musicVolumeSlider.setShowTickLabels(true);
        musicVolumeSlider.setShowTickMarks(true);
        musicVolumeSlider.setMajorTickUnit(0.25);
        musicVolumeSlider.setBlockIncrement(0.1);
        musicVolumeSlider.setPrefWidth(300);
        musicVolumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            soundManager.setMusicVolume(newVal.doubleValue());
            musicVolumeLabel.setText("Music Volume: " +
                    String.format("%.0f%%", newVal.doubleValue() * 100));
        });

        // ========== BACK BUTTON ==========
        Button backButton = createButton("Back to Menu");
        backButton.setOnMouseEntered(e -> soundManager.playSound("button_hover"));
        backButton.setOnAction(e -> {
            soundManager.playSound("button_click");
            mainApp.showMenu();
        });

        // ========== TEST BUTTONS ==========
        HBox testButtons = new HBox(15);
        testButtons.setAlignment(Pos.CENTER);

        Button testUISound = createSmallButton("Test UI");
        testUISound.setOnAction(e -> soundManager.playSound("button_click"));

        Button testGameSound = createSmallButton("Test Game");
        testGameSound.setOnAction(e -> soundManager.playGameSound("brick_break"));

        testButtons.getChildren().addAll(testUISound, testGameSound);

        // ========== LAYOUT ==========
        VBox layout = new VBox(20);

        layout.setAlignment(Pos.CENTER);

        // Add separators
        VBox uiSoundBox = new VBox(10, uiSoundTitle, soundCheckBox, soundVolumeLabel, soundVolumeSlider);
        uiSoundBox.setAlignment(Pos.CENTER);

        VBox gameSoundBox = new VBox(10, gameSoundTitle, gameSoundCheckBox);
        gameSoundBox.setAlignment(Pos.CENTER);

        VBox musicBox = new VBox(10, musicTitle, musicCheckBox, musicVolumeLabel, musicVolumeSlider);
        musicBox.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(0, 0, 60, 0));

        layout.getChildren().addAll(
                title,
                createSeparator(),
                uiSoundBox,
                createSeparator(),
                gameSoundBox,
                createSeparator(),
                musicBox,
                createSeparator(),
                testButtons,
                backButton
        );

        // Background
        Image bgImage = null;
        try {
            bgImage = new Image(getClass().getResourceAsStream("/image/menuBranchs/menuBranch.png"));
        } catch (Exception e) {
            System.err.println("Không thể tải ảnh nền!");
        }

        if (bgImage != null) {
            BackgroundSize bgSize = new BackgroundSize(800, 800, false, false, false, false);
            BackgroundImage backgroundImage = new BackgroundImage(
                    bgImage,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.DEFAULT,
                    bgSize
            );
            layout.setBackground(new Background(backgroundImage));
        } else {
            layout.setBackground(new Background(new BackgroundFill(
                    Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        }

        scene = new Scene(layout, 800, 600);
    }

    /**
     * Tạo đường phân cách
     */
    private javafx.scene.shape.Line createSeparator() {
        javafx.scene.shape.Line line = new javafx.scene.shape.Line(0, 0, 600, 0);
        line.setStroke(Color.GRAY);
        line.setStrokeWidth(1);
        line.setOpacity(0.3);
        return line;
    }

    private Button createButton(String text) {
        Button button = new Button(text);
        button.setFont(new Font("Arial", 20));
        button.setPrefWidth(250);
        button.setPrefHeight(50);
        button.setStyle(
                "-fx-background-color: #2196F3; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 10; " +
                        "-fx-cursor: hand;"
        );

        button.setOnMouseEntered(e -> {
            button.setStyle(
                    "-fx-background-color: #1976D2; " +
                            "-fx-text-fill: white; " +
                            "-fx-background-radius: 10; " +
                            "-fx-cursor: hand;"
            );
        });
        button.setOnMouseExited(e -> {
            button.setStyle(
                    "-fx-background-color: #2196F3; " +
                            "-fx-text-fill: white; " +
                            "-fx-background-radius: 10; " +
                            "-fx-cursor: hand;"
            );
        });

        return button;
    }

    private Button createSmallButton(String text) {
        Button button = new Button(text);
        button.setFont(new Font("Arial", 14));
        button.setPrefWidth(120);
        button.setPrefHeight(35);
        button.setStyle(
                "-fx-background-color: #4CAF50; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 8; " +
                        "-fx-cursor: hand;"
        );

        button.setOnMouseEntered(e -> {
            button.setStyle(
                    "-fx-background-color: #45a049; " +
                            "-fx-text-fill: white; " +
                            "-fx-background-radius: 8; " +
                            "-fx-cursor: hand;"
            );
        });
        button.setOnMouseExited(e -> {
            button.setStyle(
                    "-fx-background-color: #4CAF50; " +
                            "-fx-text-fill: white; " +
                            "-fx-background-radius: 8; " +
                            "-fx-cursor: hand;"
            );
        });

        return button;
    }

    public Scene getScene() {
        return scene;
    }
}