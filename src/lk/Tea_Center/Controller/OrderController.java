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
import lk.Tea_Center.Model.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class OrderController implements Initializable {

    @FXML
    private JFXTextField txtSid;

    @FXML
    private JFXTextField txtQuntitiy;

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
    private JFXComboBox<String> cmbItem;

    @FXML
    private Label lblItemPrice;

    @FXML
    private Label lblItemOnStock;

    @FXML
    private JFXDatePicker orderDate;

    @FXML
    private TableView<OrderTM> table;

    @FXML
    private TableColumn<OrderTM, String> tableItemCode;

    @FXML
    private TableColumn<OrderTM, String> tableItem;

    @FXML
    private TableColumn<OrderTM, Integer> tableQuntity;

    @FXML
    private TableColumn<OrderTM, Double> tablePrice;

    @FXML
    private JFXTextField txtOrderId;

    private ObservableList<OrderTM> tableItems = FXCollections.observableArrayList();
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        orderDate.setValue(LocalDate.now());
        setUpTable();
        setorderId();
        ArrayList<String> namesItems = getAllItems();
        cmbItem.getItems().addAll(namesItems);
        setSupplierLable();
        setItemDetails();
    }

    private void setorderId() {
        String sql = "SELECT O_ID FROM orders Order  by O_ID desc limit 1";
        Connection con = null;
        try {
            con = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = con.prepareStatement(sql);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()){
                int id=rs.getInt(1);
                txtOrderId.setText(String.valueOf(id+1));
            }else{
                txtOrderId.setText(String.valueOf(1));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void setUpTable() {
        tableItem.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableQuntity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tablePrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        tableItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
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

    private void setItemDetails() {
        cmbItem.getSelectionModel().selectedItemProperty().addListener((ob, oldValue, newValue) -> {
            if (newValue!=null && !newValue.isEmpty()) {
                Item itme = searchItem(newValue);
                System.out.println(newValue);

                if (itme != null) {
                    lodeItemLable(itme);
                } else {
                    clearLableItem();
                }
            } else {
                clearLableItem();
            }
        });
    }

    private Item searchItem(String newValue) {
        try {
            Connection con = DBConnection.getInstance().getConnection();
            PreparedStatement stm = con.prepareStatement("SELECT  * FROM items where name = ?");
            stm.setString(1, newValue);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                return new Item(
                        rs.getDouble(3),
                        rs.getInt(5)
                );
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    private ArrayList<String> getAllItems() {
        ArrayList<String> Item=new ArrayList<>();
        try {
            Connection connection=DBConnection.getInstance().getConnection();
            PreparedStatement stm = connection.prepareStatement("select*from items");
            ResultSet resultSet=stm.executeQuery();
            while(resultSet.next()){
                Item.add(resultSet.getString(2));
            }
            return Item;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Item;
    }
    @FXML
    void btnPlaceOrder(ActionEvent event) {
        Order order=new Order();
        order.setO_ID(Integer.parseInt(txtOrderId.getText()));
        order.setS_ID(Integer.parseInt(txtSid.getText()));
        order.setOrder_date(String.valueOf(LocalDate.now()));

        ArrayList<OrderDetails> orderDetails=new ArrayList<>();
         for (OrderTM temp:tableItems){
             OrderDetails orderDetail=new OrderDetails();
             orderDetail.setO_ID(Integer.parseInt(txtOrderId.getText()));
             orderDetail.setI_ID(temp.getItemCode());
             orderDetail.setQuntitiy(temp.getQuantity());
             orderDetail.setPriceTotal(temp.getPrice());
             orderDetails.add(orderDetail);
        }
         ArrayList<Item> items=new ArrayList<>();
         for (OrderTM temp:tableItems){
             Item item=new Item();
             item.setiID(temp.getItemCode());
             item.setQuntitiyOnStock(temp.getQuantity());
             items.add(item);
         }
        Connection con=null;
        try {
            con =DBConnection.getInstance().getConnection();
            con.setAutoCommit(false);
            String sql="INSERT INTO orders VALUES (?,?,?)";
            PreparedStatement pstm=con.prepareStatement(sql);
            pstm.setInt(1,order.getO_ID());
            pstm.setInt(2,order.getS_ID());
            pstm.setString(3,order.getOrder_date());
            if (pstm.executeUpdate()>0){
                String sql1="INSERT INTO order_details VALUES (?,?,?,?)";
                PreparedStatement pstm2=con.prepareStatement(sql1);
                boolean orderDetail=false;
                for (OrderDetails temp: orderDetails){
                    pstm2.setInt(1,temp.getO_ID());
                    pstm2.setInt(2,temp.getI_ID());
                    pstm2.setInt(3,temp.getQuntitiy());
                    pstm2.setDouble(4,temp.getPriceTotal());
                    orderDetail=pstm2.executeUpdate()>0;
                    if (!orderDetail){
                        con.rollback();
                    }
                }
                if (orderDetail){
                    boolean itemQTY =false;
                    String sql2 ="UPDATE items,(SELECT quntitiy_on_stock as qtyNow FROM items WHERE I_ID=?) as qty" +
                            " SET items.quntitiy_on_stock=(qty.qtyNow-?) WHERE items.I_ID=?";
                    PreparedStatement pstm3 =con.prepareStatement(sql2);
                    for (Item item: items){
                        pstm3.setInt(1,item.getiID());
                        pstm3.setInt(2,item.getQuntitiyOnStock());
                        pstm3.setInt(3,item.getiID());
                        itemQTY=pstm3.executeUpdate()>0;
                        if (!itemQTY){
                            con.rollback();
                        }
                    }
                    if (itemQTY){
                        con.commit();
                        table.setItems(null);
                        Alert alert=new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText("Order Is Added!");
                        alert.showAndWait();
                        resetField();
                    }else {
                        con.rollback();;
                    }
                }else {
                    con.rollback();
                }
            }else{
                con.rollback();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    @FXML
    void btnCancelOrder(ActionEvent event) {

    }


    @FXML
    void btnDeleteOrder(ActionEvent event) {

    }

    @FXML
    void btnOrderItem(ActionEvent event) {
        OrderTM temp = new OrderTM();
        temp.setName(cmbItem.getSelectionModel().getSelectedItem());
        temp.setQuantity(Integer.parseInt(txtQuntitiy.getText()));
        temp.setPrice(Double.parseDouble(lblItemPrice.getText()) * temp.getQuantity());
        Connection con = null;
        try {
            con = DBConnection.getInstance().getConnection();
            PreparedStatement stm = con.prepareStatement("SELECT I_ID FROM items where Name=?" );
            stm.setString(1,temp.getName());
            ResultSet rs = stm.executeQuery();
            if (rs.next())
            temp.setItemCode(rs.getInt(1));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        tableItems.add(temp);
        table.getItems().removeAll();
        table.setItems(tableItems);
        table.refresh();
        cmbItem.getSelectionModel().select(null);
        txtQuntitiy.setText(null);
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

    private void lodeItemLable(Item i) {
        lblItemPrice.setText(String.valueOf(i.getPrice()));
        lblItemOnStock.setText(String.valueOf(i.getQuntitiyOnStock()));
    }

    private void clearLableItem() {
        lblItemPrice.setText(null);
        lblItemOnStock.setText(null);
    }
    public void resetField(){
        setorderId();
        txtSid.setText(null);
        cmbItem.getSelectionModel().select(null);
        txtQuntitiy.setText(null);
    }
}
