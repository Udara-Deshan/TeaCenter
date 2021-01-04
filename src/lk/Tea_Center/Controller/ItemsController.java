package lk.Tea_Center.Controller;

import com.jfoenix.controls.JFXComboBox;
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
import lk.Tea_Center.Model.Item;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ItemsController implements Initializable {

    @FXML
    private JFXTextField txtItemID;

    @FXML
    private JFXTextField txtUnitPrice;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtOnStock;

    @FXML
    private TableView<Item> table;

    @FXML
    private TableColumn<Item,Integer> itemId;

    @FXML
    private TableColumn<Item, String> tableName;

    @FXML
    private TableColumn<Item, Double> tableUnitPrice;

    @FXML
    private TableColumn<Item, Integer> tablePaymentUpTo;

    @FXML
    private TableColumn<Item, Integer> tableOnStock;

    @FXML
    private JFXComboBox<String> cmbPaymentUpto;

    private ArrayList<String> paymentUpTo = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setUpTable();
        paymentUpTo.add("1 Month");
        paymentUpTo.add("2 Months");
        paymentUpTo.add("3 Months");
        paymentUpTo.add("4 Months");
        paymentUpTo.add("5 Months");
        paymentUpTo.add("6 Months");
        cmbPaymentUpto.getItems().addAll(paymentUpTo);
        loadTable();
    }
    @FXML
    void btnAdd(ActionEvent event) {
        try {
            Connection con= DBConnection.getInstance().getConnection();

            PreparedStatement stm = con.prepareStatement("INSERT INTO items VALUES(?,?,?,?,?)");
            stm.setInt(1,Integer.parseInt(txtItemID.getText()));
            stm.setString(2,txtName.getText());
            stm.setDouble(3,Double.parseDouble(txtUnitPrice.getText()));
            stm.setInt(4,Integer.parseInt(String.valueOf(cmbPaymentUpto.getSelectionModel().getSelectedIndex())));
            stm.setInt(5,Integer.parseInt(txtOnStock.getText()));

            boolean saved=stm.executeUpdate()>0;
            if (saved){
                Alert alert=new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Items");
                alert.setHeaderText("Items Saved !");
                alert.showAndWait();
                loadTable();
            }else{
                Alert alert=new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Items");
                alert.setHeaderText("Please  Check your Values");
                alert.showAndWait();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        clearAll();
    }
    private void clearAll() {
        txtItemID.setText(null);
        txtName.setText(null);
        txtOnStock.setText(null);
        txtUnitPrice.setText(null);
        cmbPaymentUpto.setItems(null);
    }
    @FXML
    void btnCancel(ActionEvent event) {
        clearAll();
    }

    @FXML
    void btnDelete(ActionEvent event) {
        clearAll();
    }

    private void loadTable(){
        ObservableList<Item> tableItems= FXCollections.observableArrayList();
        String sql="SELECT * FROM items";
        try {
            Connection con= DBConnection.getInstance().getConnection();
            PreparedStatement stm =con.prepareStatement(sql);
            ResultSet rs=stm.executeQuery();
            while(rs.next()){
                Item temp=new Item();
                temp.setiID(rs.getInt(1));
                temp.setName(rs.getString(2));
                temp.setPrice(rs.getDouble(3));
                temp.setPaymentUptoMonth(rs.getInt(4));
                temp.setQuntitiyOnStock(rs.getInt(5));

                tableItems.add(temp);
            }
            if (!tableItems.isEmpty()){
                table.getItems().clear();
                table.setItems(tableItems);
                table.refresh();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    private void setUpTable(){
        itemId.setCellValueFactory(new PropertyValueFactory<>("iID"));
        tableName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableUnitPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        tablePaymentUpTo.setCellValueFactory(new PropertyValueFactory<>("paymentUptoMonth"));
        tableOnStock.setCellValueFactory(new PropertyValueFactory<>("quntitiyOnStock"));

    }

}
