package lk.Tea_Center.Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        try {
            Parent root = FXMLLoader.load(this.getClass().getResource("/lk/Tea_Center/View/Login.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            //primaryStage.initStyle(StageStyle.TRANSPARENT);
            //primaryStage.setFullScreen(true);
            primaryStage.show();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
