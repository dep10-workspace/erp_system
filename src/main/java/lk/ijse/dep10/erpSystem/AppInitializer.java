package lk.ijse.dep10.erpSystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lk.ijse.dep10.erpSystem.db.DBConnection;
import lk.ijse.dep10.erpSystem.util.PasswordEncoder;

import java.io.IOException;

public class AppInitializer extends Application {

    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setScene(new Scene(new FXMLLoader(getClass().getResource("/view/PMUI.fxml")).load()));
        primaryStage.centerOnScreen();
        primaryStage.setResizable(false);
        primaryStage.show();

    }
}
