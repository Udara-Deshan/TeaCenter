package lk.Tea_Center.Controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.Tea_Center.DB.DBConnection;
import lk.Tea_Center.Model.MonthlyDetails;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MonthController implements Initializable {

    @FXML
    private JFXTextField txtTeaPrice;

    @FXML
    private JFXTextField txtYear;


    @FXML
    private JFXComboBox<String> cmbMonth;

    @FXML
    private JFXComboBox<String> cmbLineNo;

    @FXML
    private TableView<MonthlyDetails> tblMonthDetails;

    @FXML
    private TableColumn<MonthlyDetails, Integer> tblLineNumber;

    @FXML
    private TableColumn<MonthlyDetails, String> tblYear;

    @FXML
    private TableColumn<MonthlyDetails, String> tblMonth;

    @FXML
    private TableColumn<MonthlyDetails, Double> tblTeaPrice;

    @FXML
    private TableColumn<MonthlyDetails, Double> tblTotalTeaAmount;

    private ArrayList<String> months = new ArrayList<>();
    private ArrayList<String> Line;

    private ObservableList<MonthlyDetails> monthlyDetailsObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Line = getAllLine();
        cmbLineNo.getItems().addAll(Line);
        months.add("January");
        months.add("February");
        months.add("March");
        months.add("April");
        months.add("May");
        months.add("June");
        months.add("July");
        months.add("August");
        months.add("September");
        months.add("October");
        months.add("November");
        months.add("December");
        cmbMonth.getItems().addAll(months);
        setTable();

    }

    private void setTable() {
        tblLineNumber.setCellValueFactory(new PropertyValueFactory<>("l_ID"));
        tblYear.setCellValueFactory(new PropertyValueFactory<>("year_Name"));
        tblMonth.setCellValueFactory(new PropertyValueFactory<>("month_Name"));
        tblTeaPrice.setCellValueFactory(new PropertyValueFactory<>("tea_Price"));
        tblTotalTeaAmount.setCellValueFactory(new PropertyValueFactory<>("month_Total"));
        tblMonthDetails.setItems(loadAllMonthlyDetail());
    }

    private ObservableList<MonthlyDetails> loadAllMonthlyDetail() {
        String sql = "SELECT * FROM monthly_details";
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                MonthlyDetails monthlyDetails = new MonthlyDetails();
                monthlyDetails.setM_ID(resultSet.getInt(1));
                monthlyDetails.setL_ID(resultSet.getInt(2));
                monthlyDetails.setYear_Name(resultSet.getString(3));
                monthlyDetails.setMonth_Name(resultSet.getString(4));
                monthlyDetails.setTea_Price(resultSet.getDouble(5));
                monthlyDetails.setMonth_Total(resultSet.getDouble(6));
                monthlyDetailsObservableList.add(monthlyDetails);
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (monthlyDetailsObservableList != null) {
            return monthlyDetailsObservableList;
        } else {
            return monthlyDetailsObservableList;
        }
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

    @FXML
    void btnAddTea(ActionEvent event) {

        MonthlyDetails monthlyDetails = new MonthlyDetails();
        monthlyDetails.setL_ID(Integer.parseInt(cmbLineNo.getSelectionModel().getSelectedItem()));
        monthlyDetails.setMonth_Name(cmbMonth.getSelectionModel().getSelectedItem());
        monthlyDetails.setTea_Price(Double.parseDouble(txtTeaPrice.getText()));
        monthlyDetails.setYear_Name(txtYear.getText());
        Double total = 0.0;
        String yearAndMonth = monthlyDetails.getYear_Name() + "-" + (cmbMonth.getSelectionModel().getSelectedIndex() + 1);
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String sql = "select sum(amount_kg) from tea_ammount inner join supplier " +
                    "on supplier.S_ID=tea_ammount.S_ID and tea_ammount.givenTime between '" + yearAndMonth + "-1'and'" + yearAndMonth + "-31' and supplier.L_ID='" + monthlyDetails.getL_ID() + "'";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                total = resultSet.getDouble(1);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        monthlyDetails.setMonth_Total(total);
        monthlyDetailsObservableList.add(monthlyDetails);
        tblMonthDetails.setItems(monthlyDetailsObservableList);
        tblMonthDetails.refresh();
        if (saveMonthDetail(monthlyDetails)) {
        }
    }

    private boolean saveMonthDetail(MonthlyDetails monthlyDetails) {
        monthlyDetails.setM_ID(CreateID());
        Connection con = null;
        try {
            con = DBConnection.getInstance().getConnection();
            PreparedStatement stm = con.prepareStatement("INSERT INTO monthly_details value (?,?,?,?,?,?)");
            stm.setInt(1, monthlyDetails.getM_ID());
            stm.setInt(2, monthlyDetails.getL_ID());
            stm.setString(3, monthlyDetails.getYear_Name());
            stm.setString(4, monthlyDetails.getMonth_Name());
            stm.setDouble(5, monthlyDetails.getTea_Price());
            stm.setDouble(6, monthlyDetails.getMonth_Total());
            return stm.executeUpdate() > 0;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }
    private int CreateID() {
        String sql = "SELECT M_ID FROM monthly_details Order  by M_ID desc limit 1";
        Connection con = null;
        try {
            con = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = con.prepareStatement(sql);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                int id = rs.getInt(1);
                return id + 1;
            } else {
                return 1;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 1;
    }
    @FXML
    void btnCancelTea(ActionEvent event) {
    }
    @FXML
    void btnDeleteTea(ActionEvent event) {
    }
}
