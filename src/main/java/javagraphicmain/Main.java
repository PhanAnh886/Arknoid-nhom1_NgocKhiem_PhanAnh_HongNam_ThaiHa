package javagraphicmain;

import javafx.application.Application ;  // Lớp khởi động JavaFX
import javafx.scene.Scene;              // Màn cảnh hiển thị
import javafx.stage.Stage;              // Cửa sổ chính của ứng dụng
import javafx.scene.paint.Color;        // dùng để đổi mảu background
import objectsInGame.*;
/**
 * bắt đầu chương trình
 *phan anh
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // GameControl là lớp chứa toàn bộ logic game và khung vẽ
        GameControl game = new GameControl(); // GameControl đc xem là 1 Pane

        // khởi tạo scene
        Scene scene = new Scene(game);


        // khởi tạo stage
        primaryStage.setTitle("Arrknoid");
        primaryStage.setScene(scene);
        primaryStage.show(); // Hiển thị cửa sổ
    }

    /**
     * start point
     * @param args nhu binh thuong
     */
    public static void main(String[] args) {
        // khởi chạy JavaFX
        launch(args);
    }
}
