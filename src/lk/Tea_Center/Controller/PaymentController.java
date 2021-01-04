package lk.Tea_Center.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.Tea_Center.DB.DBConnection;
import lk.Tea_Center.Model.OrderDetails;
import lk.Tea_Center.Model.Supplier;
import lk.Tea_Center.Model.TeaAmount;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PaymentController implements Initializable {

    @FXML
    private JFXTextField txtS_ID;

    @FXML
    private Label lblSupplierName;

    @FXML
    private Label lblSupplierTelephone;

    @FXML
    private Label lblBalaenceForTea;

    @FXML
    private Label lblSupplierLineNumber;

    @FXML
    private JFXTextArea lblSupplierAddress;

    @FXML
    private TableView<OrderDetails> tblOderDetails;

    @FXML
    private TableColumn<OrderDetails, Integer> tblItem;

    @FXML
    private TableColumn<OrderDetails, Integer> tblQuntity;

    @FXML
    private TableColumn<OrderDetails, Double> tblPrice;

    @FXML
    private Label lblTotalPrice;

    @FXML
    private Label lblLoanBalance;

    @FXML
    private Label lblOrdersBalance;

    @FXML
    private TableView<TeaAmount> tblTeaAmount;

    @FXML
    private TableColumn<TeaAmount, String> tblDay;

    @FXML
    private TableColumn<TeaAmount, Double> tblAmount;

    @FXML
    private Label lblTotalTeaAmount;

    @FXML
    private Label lblTeaRate;

    @FXML
    private Label lblBalance;

    @FXML
    private JFXComboBox<String> cmbMonth;

    @FXML
    private JFXTextField txtYear;
    @FXML
    private JFXButton btnCalculate;


    ArrayList<String> months = new ArrayList<>();
    ObservableList<OrderDetails> orderDetailsObservableList= FXCollections.observableArrayList();
    ObservableList<TeaAmount> teaAmountObservableList= FXCollections.observableArrayList();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
        setSupplierLable();
        txtYear.setText(String.valueOf(LocalDate.parse(String.valueOf(LocalDate.now())).getYear()));
        setOrderDetail();
        setTblTeaAmount();
    }

    private void setOrderDetail() {
        tblItem.setCellValueFactory(new PropertyValueFactory<>("i_ID"));
        tblQuntity.setCellValueFactory(new PropertyValueFactory<>("quntitiy"));
        tblPrice.setCellValueFactory(new PropertyValueFactory<>("priceTotal"));

    }

    private void setTblTeaAmount(){
        tblDay.setCellValueFactory(new PropertyValueFactory<>("givenTime"));
        tblAmount.setCellValueFactory(new  PropertyValueFactory<>("amountKg"));
    }

    private void setSupplierLable() {
        txtS_ID.textProperty().addListener((ob, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                Supplier supplier = searchSupplier(newValue);
                if (supplier != null) {
                    lodeSupplierLable(supplier);
                    monthlyDetailsLableClear();
                    clearTable();
                } else {
                    clearLableSupplier();
                    monthlyDetailsLableClear();
                    clearTable();
                }
            } else {
                clearLableSupplier();
                monthlyDetailsLableClear();
                clearTable();
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
        lblSupplierLineNumber.setText(String.valueOf(s.getL_ID()));
    }
    private void clearLableSupplier() {
        lblSupplierName.setText(null);
        lblSupplierAddress.setText(null);
        lblSupplierTelephone.setText(null);
        lblSupplierLineNumber.setText(null);
    }
    private void clearTable(){
        teaAmountObservableList.clear();
        tblTeaAmount.setItems(teaAmountObservableList);
        orderDetailsObservableList.clear();
        tblOderDetails.setItems(orderDetailsObservableList);
        lblTotalPrice.setText(null);
        lblTotalTeaAmount.setText(null);
    }
    @FXML
    void Calculate(ActionEvent event) {
        if (txtYear.getText()!=null && !txtYear.getText().isEmpty()&&cmbMonth.getSelectionModel().getSelectedItem()!=null && !cmbMonth.getSelectionModel().isEmpty()){
            getOrders();
            getTeaAmount();
            getMonthlyDetails();
            lblOrdersBalance.setText(lblTotalPrice.getText());
            getLoanAmount();
            setBalance();

        }
    }

    private void setBalance() {

         double totaltodeduct=Double.parseDouble(lblOrdersBalance.getText())+Double.parseDouble(lblLoanBalance.getText());
        double totalIncome=Double.parseDouble(lblBalaenceForTea.getText());
        lblBalance.setText(String.valueOf(totalIncome-totaltodeduct));
    }

    private void getLoanAmount() {
        String sql="select PaymentForMonth ,MonthPeriod ,OpenDate from loan where S_ID=? and MonthPeriod>0";
        Connection connection=null;
        boolean monthperiod=false;
        String opendate=null;
        int months=0;
        try {
            connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement(sql);

            pstm.setString(1,txtS_ID.getText());
            ResultSet rs = pstm.executeQuery();
            while(rs.next()){
                lblLoanBalance.setText(rs.getString(1));
                months=rs.getInt(2);
                monthperiod=true;
                opendate=rs.getString(3);
            }
            if (monthperiod){
                String sql2="update loan set monthperiod=? where  S_ID=? and OpenDate=?";
                PreparedStatement pstm2 = connection.prepareStatement(sql2);
                pstm2.setInt(1,(months-1));
                pstm2.setString(2,txtS_ID.getText());
                pstm2.setString(3,opendate);
                int  rs2 = pstm2.executeUpdate();
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void getMonthlyDetails() {
        String yearAndMonth =txtYear.getText() + "-" + (cmbMonth.getSelectionModel().getSelectedIndex() + 1);
        String sql="select tea_Price from monthly_details inner join supplier on supplier.L_ID=monthly_details.L_ID and monthly_details.Year_=? and  monthly_details.month_=?  and supplier.S_ID=?";
        Connection connection= null;
        try {
        connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1,txtYear.getText());
        pstm.setString(2,cmbMonth.getSelectionModel().getSelectedItem());
        pstm.setString(3,txtS_ID.getText());
        ResultSet rs = pstm.executeQuery();
        if(rs.next()){
            lblTeaRate.setText(rs.getString(1));
            lblBalaenceForTea.setText(String.valueOf((double)(Integer.parseInt(lblTotalTeaAmount.getText())*rs.getDouble(1))));
        }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void monthlyDetailsLableClear() {
        lblTeaRate.setText(null);
        lblBalaenceForTea.setText(null);
        lblLoanBalance.setText(null);
        lblOrdersBalance.setText(null);
    }

    private void getTeaAmount() {
        String yearAndMonth =txtYear.getText() + "-" + (cmbMonth.getSelectionModel().getSelectedIndex() + 1);
        String sql="SELECT givenTime,amount_kg FROM  tea_ammount WHERE S_ID=? AND givenTime BETWEEN '"+yearAndMonth+"-1' AND '"+yearAndMonth+"-31'";
        teaAmountObservableList.clear();
        Connection connection= null;
        try {
            int teaAmountKG=0;
            connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement(sql);
            pstm.setString(1,txtS_ID.getText());
            ResultSet rs = pstm.executeQuery();
            while(rs.next()){
                TeaAmount teaAmount=new TeaAmount();
                teaAmount.setGivenTime(rs.getString(1));
                teaAmount.setAmountKg(rs.getInt(2));
                teaAmountKG+=teaAmount.getAmountKg();
                teaAmountObservableList.add(teaAmount);
            }
            tblTeaAmount.setItems(teaAmountObservableList);
            lblTotalTeaAmount.setText(String.valueOf(teaAmountKG));

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    private void getOrders() {
        String yearAndMonth =txtYear.getText() + "-" + (cmbMonth.getSelectionModel().getSelectedIndex() + 1);
        orderDetailsObservableList.clear();
        String sql= "select * from order_details inner join orders on orders.O_ID=order_details.O_ID and orders.S_Id=? and orders.order_date BETWEEN '"+yearAndMonth+"-1' and '"+yearAndMonth+"-31' ";
        try {
            double price =0.0;
            Connection connection=DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement(sql);
            pstm.setString(1,txtS_ID.getText());
            ResultSet rs = pstm.executeQuery();
            while(rs.next()){
                OrderDetails orderDetaiil=new OrderDetails();
                orderDetaiil.setI_ID(rs.getInt(2));
                orderDetaiil.setQuntitiy(rs.getInt(3));
                orderDetaiil.setPriceTotal(rs.getDouble(4));
                price+=orderDetaiil.getPriceTotal();
                orderDetailsObservableList.add(orderDetaiil);
            }
        tblOderDetails.setItems(orderDetailsObservableList);
            lblTotalPrice.setText(String.valueOf(price));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}