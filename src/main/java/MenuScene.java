package ui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.scene.text.Text;
import javagraphicmain.Main;

public class MenuScene {
    private Scene scene;

    public MenuScene(Main mainApp) {
        Text title = new Text("ARKANOID");
        Button startButton = new Button("Start Game");
        Button exitButton = new Button("Exit");

        startButton.setOnAction(e -> mainApp.showGame());
        exitButton.setOnAction(e -> System.exit(0));

        VBox layout = new VBox(10, title, startButton, exitButton);
        layout.setAlignment(Pos.CENTER);
        scene = new Scene(layout, 800, 600);
    }

    public Scene getScene() {
        return scene;
    }
}
