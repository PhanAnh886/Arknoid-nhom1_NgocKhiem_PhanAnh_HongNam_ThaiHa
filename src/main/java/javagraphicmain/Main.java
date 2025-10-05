package javagraphicmain;

import javafx.application.Application;   // Lớp khởi động JavaFX
import javafx.scene.Scene;              // Màn cảnh hiển thị
import javafx.stage.Stage;              // Cửa sổ chính của ứng dụng
import objectsInGame.*;
/**
 * bắt đầu chương trình
 *
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // GameControl là lớp chứa toàn bộ logic game và khung vẽ
        GameControl game = new GameControl();

        // Scene là “màn chơi” chứa GameControl
        Scene scene = new Scene(game);

        // Stage là “cửa sổ ứng dụng”
        primaryStage.setTitle("Arrknoid");
        primaryStage.setScene(scene);
        primaryStage.show(); // Hiển thị cửa sổ
    }

    public static void main(String[] args) {
        // khởi chạy JavaFX
        launch(args);
    }
}
