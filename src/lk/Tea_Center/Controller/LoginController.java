package lk.Tea_Center.Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private AnchorPane logWindow;

    @FXML
    void cancel(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void log(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(this.getClass().getResource("/lk/Tea_Center/View/DashBord.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) logWindow.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setFullScreen(true);
        stage.setResizable(false);
    }
}

