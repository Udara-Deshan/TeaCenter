package lk.Tea_Center.Controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.Tea_Center.DB.DBConnection;
import lk.Tea_Center.Model.Loan;
import lk.Tea_Center.Model.Supplier;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class LoanController implements Initializable {

    @FXML
    private JFXTextField txtSid;

    @FXML
    private Label lblSupplierName;

    @FXML
    private Label lblSupplierTelephone;

    @FXML
    private Label lblSupplierDOB;

    @FXML
    private Label lblSupplierBalance;

    @FXML
    private Label lblSupplierLineNumber;

    @FXML
    private JFXTextArea lblSupplierAddress;

    @FXML
    private JFXTextField txtLoan;

    @FXML
    private JFXComboBox<String> cmbMonthPeriod;

    @FXML
    private JFXDatePicker dtOpenDate;

    @FXML
    private JFXTextField txtPaymentForMonth;

    @FXML
    private TableView<Loan> tblLoanDetails;

    @FXML
    private TableColumn<Loan, Integer> tblS_ID;

    @FXML
    private TableColumn<Loan, Integer> tblLoanID;

    @FXML
    private TableColumn<Loan, Integer> tblLoan;

    @FXML
    private TableColumn<Loan, Integer> tblDate;

    @FXML
    private TableColumn<Loan, Integer> tblMonthPeriod;

    @FXML
    private TableColumn<Loan, Integer> tblPaymentForMonth;

    @FXML
    private JFXTextField txtLoanID;

    private ObservableList<Loan> tableItems = FXCollections.observableArrayList();
    private ArrayList<String> paymentUpTo = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        paymentUpTo.add("1 Month");
        paymentUpTo.add("2 Months");
        paymentUpTo.add("3 Months");
        paymentUpTo.add("4 Months");
        paymentUpTo.add("5 Months");
        paymentUpTo.add("6 Months");
        cmbMonthPeriod.getItems().addAll(paymentUpTo);
        setUpTable();
        setSupplierLable();
        loadTable();
    }

    private void setUpTable() {
        tblLoanID.setCellValueFactory(new PropertyValueFactory<>("loan_ID"));
        tblS_ID.setCellValueFactory(new PropertyValueFactory<>("s_ID"));
        tblLoan.setCellValueFactory(new PropertyValueFactory<>("loan"));
        tblDate.setCellValueFactory(new PropertyValueFactory<>("openDate"));
        tblMonthPeriod.setCellValueFactory(new PropertyValueFactory<>("monthPeriod"));
        tblPaymentForMonth.setCellValueFactory(new PropertyValueFactory<>("paymentForMonth"));
    }

    private void loadTable() {
        String sql = "SELECT * FROM loan";
        try {
            Connection con = DBConnection.getInstance().getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Loan temp = new Loan();
                temp.setLoan_ID(rs.getInt(1));
                temp.setS_ID(rs.getInt(2));
                temp.setLoan(rs.getDouble(3));
                temp.setOpenDate(rs.getString(4));
                temp.setMonthPeriod(rs.getInt(5));
                temp.setPaymentForMonth(rs.getDouble(6));
                tableItems.add(temp);
            }
            if (!tableItems.isEmpty()) {
                tblLoanDetails.getItems().clear();
                tblLoanDetails.setItems(tableItems);
                tblLoanDetails.refresh();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private Loan makeLoan() {
        return new Loan(
                Integer.parseInt(txtLoanID.getText()),
                Integer.parseInt(txtSid.getText()),
                Double.parseDouble(txtLoan.getText()),
                String.valueOf(dtOpenDate.getValue()),
                Integer.parseInt(String.valueOf(cmbMonthPeriod.getSelectionModel().getSelectedIndex())),
                Double.parseDouble(txtPaymentForMonth.getText()));
    }

    @FXML
    void btnAddLoan(ActionEvent event) {
        try {
            Connection con = DBConnection.getInstance().getConnection();
            PreparedStatement stm = con.prepareStatement("INSERT INTO loan VALUES(?,?,?,?,?,?)");
            Loan loan = makeLoan();
            stm.setInt(1, loan.getLoan_ID());
            stm.setInt(2, loan.getS_ID());
            stm.setDouble(3, loan.getLoan());
            stm.setString(4, loan.getOpenDate());
            stm.setInt(5, loan.getMonthPeriod());
            stm.setDouble(6, loan.getPaymentForMonth());
            boolean saved = stm.executeUpdate() > 0;
            if (saved) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Loan");
                alert.setHeaderText("Loan Add !");
                alert.show();
                loadTable();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Loan");
                alert.setHeaderText("Please  Check your Values");
                alert.show();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        clearAll();
    }

    private void clearAll() {
        txtLoanID.setText(null);
        txtSid.setText(null);
        txtLoan.setText(null);
        txtPaymentForMonth.setText(null);
        dtOpenDate.setValue(null);
        cmbMonthPeriod.getSelectionModel().select(null);

    }


    @FXML
    void btnCancel(ActionEvent event) {
        clearAll();
    }

    @FXML
    void btnDeleteLoan(ActionEvent event) {
        try {
            Connection con = DBConnection.getInstance().getConnection();
            PreparedStatement stm = con.prepareStatement("DELETE FROM loan WHERE S_ID = ? ");
            stm.setInt(1, tblLoanDetails.getSelectionModel().getSelectedItem().getS_ID());
            boolean result = stm.executeUpdate() > 0;
            tableItems.remove(tblLoanDetails.getSelectionModel().getSelectedItem());
            tblLoanDetails.refresh();
            //clearAll();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    private void setSupplierLable() {
        txtSid.textProperty().addListener((ob, oldValue, newValue) -> {
            if (newValue!=null && !newValue.isEmpty()) {
                Supplier supplier = searchSupplier(newValue);
                if (supplier != null) {
                    lodeSupplierLable(supplier);
                } else {
                    clearLableSupplier();
                }
            } else {
                clearLableSupplier();
            }
        });
    }

    private Supplier searchSupplier(String newValue) {
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

    private void lodeSupplierLable(Supplier s) {
        lblSupplierName.setText(s.getName());
        lblSupplierAddress.setText(s.getAddress());
        lblSupplierTelephone.setText(String.valueOf(s.getTelephone()));
        lblSupplierDOB.setText(s.getdOB());
        lblSupplierBalance.setText(String.valueOf(s.getBalance()));
        lblSupplierLineNumber.setText(String.valueOf(s.getL_ID()));
    }

    private void clearLableSupplier() {
        lblSupplierName.setText(null);
        lblSupplierAddress.setText(null);
        lblSupplierTelephone.setText(null);
        lblSupplierDOB.setText(null);
        lblSupplierBalance.setText(null);
        lblSupplierLineNumber.setText(null);
    }


}
