package lk.Tea_Center.Controller;

import com.jfoenix.controls.JFXComboBox;
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
import lk.Tea_Center.Model.Supplier;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SupplierController implements Initializable {

    private static ArrayList<String> line = new ArrayList<>();
    @FXML
    private JFXTextField txtSid;
    @FXML
    private JFXTextField txtAddress;
    @FXML
    private JFXTextField txtTelephone;
    @FXML
    private JFXComboBox<String> cmbLineNo;
    @FXML
    private JFXTextField txtName;
    @FXML
    private TableView<Supplier> table;
    @FXML
    private TableColumn<Supplier, Integer> tableSID;
    @FXML
    private TableColumn<Supplier, String> tableName;
    @FXML
    private TableColumn<Supplier, String> tableAddress;
    @FXML
    private TableColumn<Supplier, Integer> tableTelephone;
    @FXML
    private TableColumn<Supplier, String> tableBrithDay;
    @FXML
    private TableColumn<Supplier, Integer> tableLineNo;
    @FXML
    private TableColumn<Supplier, Integer> tableBalance;
    @FXML
    private JFXDatePicker dtBrithDay;
    private ObservableList<Supplier> tableItems = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setUpTable();
        ArrayList<String> namesLine = getAllLine();
        cmbLineNo.getItems().addAll(namesLine);
        loadTable();
        setorderS_ID();
        txtSid.textProperty().addListener((ob, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                Supplier supplier = search(newValue);
                if (supplier != null) {
                    loadDataTOTextFields(supplier);
                } else {
                    txtName.setText(null);
                    txtAddress.setText(null);
                    dtBrithDay.setValue(null);
                    txtTelephone.setText(null);
                    cmbLineNo.getSelectionModel().select(null);
                }
            } else {
                clearAll();
            }
        });
    }
    private void setorderS_ID() {
        String sql = "SELECT S_ID FROM supplier Order  by S_ID desc limit 1";
        Connection con = null;
        try {
            con = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = con.prepareStatement(sql);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()){
                int id=rs.getInt(1);
                txtSid.setText(String.valueOf(id+1));
            }else{
                txtSid.setText(String.valueOf(1));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    private void loadDataTOTextFields(Supplier supplier) {
        txtSid.setText(String.valueOf(supplier.getS_ID()));
        txtTelephone.setText(String.valueOf(supplier.getTelephone()));
        txtAddress.setText(supplier.getAddress());
        txtName.setText(supplier.getName());
        dtBrithDay.setValue(LocalDate.parse(supplier.getdOB()));
        cmbLineNo.getSelectionModel().select(String.valueOf(supplier.getL_ID()));
    }

    private Supplier search(String newValue) {
        try {
            Connection con = DBConnection.getInstance().getConnection();
            PreparedStatement stm = con.prepareStatement("SELECT  * FROM supplier where  s_id=" + newValue);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return new Supplier(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getInt(4),
                        rs.getString(5),
                        rs.getDouble(6),
                        rs.getInt(7)
                );
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @FXML
    void btnCancel(ActionEvent event) {
        clearAll();
    }

    @FXML
    void btnDelete(ActionEvent event) {
        try {
            Connection con = DBConnection.getInstance().getConnection();
            PreparedStatement stm = con.prepareStatement("DELETE FROM supplier WHERE S_ID = ? ");
            stm.setInt(1, table.getSelectionModel().getSelectedItem().getS_ID());
            boolean result = stm.executeUpdate() > 0;
            tableItems.remove(table.getSelectionModel().getSelectedItem());
            table.refresh();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        clearAll();
    }

    private Supplier makeSupplier() {
        return new Supplier(
                Integer.parseInt(txtSid.getText()),
                txtName.getText(), txtAddress.getText(),
                Integer.parseInt(txtTelephone.getText()),
                String.valueOf(dtBrithDay.getValue()),
                0.0,
                Integer.parseInt(cmbLineNo.getSelectionModel().getSelectedItem()));
    }

    @FXML
    void btnSave(ActionEvent event) {
        try {
            Connection con = DBConnection.getInstance().getConnection();
            PreparedStatement stm = con.prepareStatement("INSERT INTO supplier VALUES(?,?,?,?,?,?,?)");
            Supplier supplier = makeSupplier();
            stm.setInt(1, supplier.getS_ID());
            stm.setString(2, supplier.getName());
            stm.setString(3, supplier.getAddress());
            stm.setInt(4, supplier.getTelephone());
            stm.setString(5, supplier.getdOB());
            stm.setDouble(6, supplier.getBalance());
            stm.setInt(7, supplier.getL_ID());
            boolean saved = stm.executeUpdate() > 0;
            if (saved) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Supplier");
                alert.setHeaderText("Supplier Saved !");
                alert.show();
                loadTable();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Supplier");
                alert.setHeaderText("Please  Check your Values");
                alert.show();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        clearAll();
        setorderS_ID();
    }

    private ArrayList<String> getAllLine() {
        ArrayList<String> line = new ArrayList<>();
        try {
            Connection con = DBConnection.getInstance().getConnection();
            PreparedStatement stm = con.prepareStatement("SELECT * FROM LINE");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                line.add(rs.getString(1));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return line;
    }

    private void loadTable() {
        String sql = "SELECT * FROM supplier";
        try {
            Connection con = DBConnection.getInstance().getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Supplier temp = new Supplier();
                temp.setS_ID(rs.getInt(1));
                temp.setName(rs.getString(2));
                temp.setAddress(rs.getString(3));
                temp.setTelephone(rs.getInt(4));
                temp.setdOB(rs.getString(5));
                temp.setBalance(rs.getDouble(6));
                temp.setL_ID(rs.getInt(7));
                tableItems.add(temp);
            }
            if (!tableItems.isEmpty()) {
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

    private void setUpTable() {
        tableSID.setCellValueFactory(new PropertyValueFactory<>("s_ID"));
        tableName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        tableTelephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        tableBalance.setCellValueFactory(new PropertyValueFactory<>("balance"));
        tableLineNo.setCellValueFactory(new PropertyValueFactory<>("l_ID"));
        tableBrithDay.setCellValueFactory(new PropertyValueFactory<>("dOB"));
    }

    private void clearAll() {
        txtName.setText(null);
        txtAddress.setText(null);
        dtBrithDay.setValue(null);
        txtTelephone.setText(null);
        cmbLineNo.getSelectionModel().select(null);
    }
}
