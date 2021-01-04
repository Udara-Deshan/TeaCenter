package lk.Tea_Center.Controller;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

public class dashBordController {
    JFXButton oldSelected = null;
    @FXML
    private JFXButton btnMonth;

    @FXML
    private JFXButton btnLine;

    @FXML
    private JFXButton btnSupplier;

    @FXML
    private JFXButton btnItems;

    @FXML
    private JFXButton btnGreenLeaf;

    @FXML
    private JFXButton btnOrder;

    @FXML
    private JFXButton btnLoan;

    @FXML
    private JFXButton btnPayment;

    @FXML
    private BorderPane boderPane;

    @FXML
    void greenLeaf(ActionEvent event) {
        loadUI("GreenLeaf");
        setSelected(btnGreenLeaf);
    }

    @FXML
    void items(ActionEvent event) {
        loadUI("Items");
        setSelected(btnItems);
    }

    @FXML
    void order(ActionEvent event) {
        loadUI("Order");
        setSelected(btnOrder);
    }

    @FXML
    void month(ActionEvent event) {
        loadUI("Month");
        setSelected(btnMonth);
    }

    @FXML
    void Loan(ActionEvent event) {
        loadUI("Loan");
        setSelected(btnLoan);
    }

    @FXML
    void exit(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void line(ActionEvent event) {
        loadUI("Line");
        setSelected(btnLine);
    }

    @FXML
    void Payment(ActionEvent event) {
        loadUI("Payment");
        setSelected(btnPayment);
    }

    @FXML
    void supplier(ActionEvent event) {
        setSelected(btnSupplier);
        loadUI("Supplier");
    }

    private void loadUI(String ui) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/lk/Tea_Center/View/" + ui + ".fxml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        boderPane.setCenter(root);
    }

    private void setSelected(JFXButton selected) {
        if (oldSelected != null) {
            oldSelected.setStyle("-fx-background-color: transparent");
        }
        selected.setStyle("-fx-background-color: rgba(0,0,0,0.25)");
        oldSelected = selected;
    }
}
