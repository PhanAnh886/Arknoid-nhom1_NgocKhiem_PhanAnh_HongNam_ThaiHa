package ui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.scene.text.Text;
import javagraphicmain.Main;

public class GameOverScene {
    private Scene scene;

    public GameOverScene(Main mainApp) {
        Text title = new Text("GAME OVER");
        Button retryButton = new Button("Retry");
        Button menuButton = new Button("Main Menu");
        Button exitButton = new Button("Exit");

        //  Khi bấm Retry → quay lại chơi game (bắt đầu lại)
        retryButton.setOnAction(e -> {
            mainApp.showGame();  // gọi lại scene game
        });

        //  Khi bấm Menu → quay lại menu chính
        menuButton.setOnAction(e -> {
            mainApp.showMenu();
        });

        //  Khi bấm Exit → thoát game
        exitButton.setOnAction(e -> System.exit(0));

        VBox layout = new VBox(15, title, retryButton, menuButton, exitButton);
        layout.setAlignment(Pos.CENTER);
        scene = new Scene(layout, 800, 600);
    }

    public Scene getScene() {
        return scene;
    }
}
