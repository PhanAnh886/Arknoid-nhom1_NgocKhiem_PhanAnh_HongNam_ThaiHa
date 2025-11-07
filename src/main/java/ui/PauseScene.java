package ui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.scene.text.Text;
import javagraphicmain.Main;

public class PauseScene {
    private Scene scene;

    public PauseScene(Main mainApp) {
        Text title = new Text("PAUSED");
        Button resumeButton = new Button("Resume");
        Button menuButton = new Button("Main Menu");
        Button exitButton = new Button("Exit");

        // ✅ Resume lại game
        resumeButton.setOnAction(e -> mainApp.resumeGame());

        // ✅ Trở về menu chính
        menuButton.setOnAction(e -> mainApp.showMenu());

        // ✅ Thoát game
        exitButton.setOnAction(e -> System.exit(0));

        VBox layout = new VBox(10, title, resumeButton, menuButton, exitButton);
        layout.setAlignment(Pos.CENTER);
        scene = new Scene(layout, 1000, 600);
    }

    public Scene getScene() {
        return scene;
    }
}
