package lk.Tea_Center.Controller;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.Tea_Center.DB.DBConnection;
import lk.Tea_Center.Model.GreenLeafTM;
import lk.Tea_Center.Model.TeaAmount;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class GreenLeafController implements Initializable {
    @FXML
    private JFXTextField txtT_ID;

    @FXML
    private JFXTextField txtSid;

    @FXML
    private TableView<GreenLeafTM> tblTableTeaDetails;

    @FXML
    private TableColumn<GreenLeafTM, Integer> tblSID;



    @FXML
    private TableColumn<GreenLeafTM, Integer> tblTeaAmount;

    @FXML
    private TableColumn<GreenLeafTM, String> tblGivenTime;

    @FXML
    private JFXTextField txtTeaAmount;

    @FXML
    private JFXDatePicker dtAddDate;

    private ObservableList<GreenLeafTM> tableItems = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dtAddDate.setValue(LocalDate.now());
        setUpTable();
        loadTable();
        setorderTID();
    }

    private void setorderTID() {
        String sql = "SELECT T_ID FROM tea_ammount Order  by T_ID desc limit 1";
        Connection con = null;
        try {
            con = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = con.prepareStatement(sql);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                int id = rs.getInt(1);
                txtT_ID .setText(String.valueOf(id+1));
            } else {
                txtT_ID.setText(String.valueOf(1));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void setUpTable() {
        tblSID.setCellValueFactory(new PropertyValueFactory<>("s_ID"));
        tblTeaAmount.setCellValueFactory(new PropertyValueFactory<>("teaAmount"));
        tblGivenTime.setCellValueFactory(new PropertyValueFactory<>("givenTime"));
    }

    private void loadTable() {
        String sql = "SELECT * FROM tea_ammount";
        try {
            Connection con = DBConnection.getInstance().getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                GreenLeafTM temp = new GreenLeafTM();
                temp.setS_ID(rs.getInt(2));
                temp.setTeaAmount(rs.getInt(3));
                temp.setGivenTime(rs.getString(4));
                tableItems.add(temp);
            }
            if (!tableItems.isEmpty()) {
                tblTableTeaDetails.getItems().clear();
                tblTableTeaDetails.setItems(tableItems);
                tblTableTeaDetails.refresh();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @FXML
    void btnAddTea(ActionEvent event) {
        try {
            Connection con = DBConnection.getInstance().getConnection();
            PreparedStatement stm = con.prepareStatement("INSERT INTO tea_ammount VALUES(?,?,?,?)");
            TeaAmount greenLeaf = makeGreenLeaf();
            stm.setInt(1, greenLeaf.getT_ID());
            stm.setInt(2, greenLeaf.getS_ID());
            stm.setDouble(3, greenLeaf.getAmountKg());
            stm.setString(4, greenLeaf.getGivenTime());
            boolean saved = stm.executeUpdate() > 0;
            if (saved) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Green Leaf");
                alert.setHeaderText("Value Add !");
                alert.show();
                loadTable();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Green Leaf");
                alert.setHeaderText("Please  Check your Values");
                alert.show();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Green Leaf");
            alert.setHeaderText(throwables+"\nPlease  Check your Values");
            alert.show();
        }
    }

    private TeaAmount makeGreenLeaf() {
        return new TeaAmount(
                Integer.parseInt(txtT_ID.getText()),
                Integer.parseInt(txtSid.getText()),
                Integer.parseInt(txtTeaAmount.getText()),
                String.valueOf(dtAddDate.getValue()));

    }

    @FXML
    void btnCancelTea(ActionEvent event) {
        clearAll();
    }

    private void clearAll() {
        txtSid.setText(null);
        txtTeaAmount.setText(null);
    }

    @FXML
    void btnDeleteTea(ActionEvent event) {
        try {
            Connection con = DBConnection.getInstance().getConnection();
            PreparedStatement stm = con.prepareStatement("DELETE FROM tea_ammount WHERE T_ID = ? ");
            stm.setInt(1, tblTableTeaDetails.getSelectionModel().getSelectedItem().getS_ID());
            boolean result = stm.executeUpdate() > 0;
            tableItems.remove(tblTableTeaDetails.getSelectionModel().getSelectedItem());
            tblTableTeaDetails.refresh();
            //clearAll();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
