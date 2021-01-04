package lk.Tea_Center.Controller;

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
import lk.Tea_Center.Model.Line;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LineController implements Initializable {

    @FXML
    private JFXTextField l_ID;

    @FXML
    private JFXTextField name;

    @FXML
    private JFXTextField profit;

    @FXML
    private TableView<Line> table;

    @FXML
    private TableColumn<Line, Integer> tableLID;

    @FXML
    private TableColumn<Line, String> tableName;

    @FXML
    private TableColumn<Line, Integer> tableProfit;
    private ObservableList<Line> tableItems= FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setorderL_ID();
        setUpTable();
        loadTable();
    }
    private void setorderL_ID() {
        String sql = "SELECT L_ID FROM line Order  by L_ID desc limit 1";
        Connection con = null;
        try {
            con = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = con.prepareStatement(sql);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()){
                int id=rs.getInt(1);
                l_ID.setText(String.valueOf(id+1));
            }else{
                l_ID.setText(String.valueOf(1));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    @FXML
    void btnAddLine(ActionEvent event) {
        Connection con= null;
        try {
            con = DBConnection.getInstance().getConnection();

            PreparedStatement stm = con.prepareStatement("INSERT INTO Line VALUES(?,?,?)");
            stm.setInt(1,Integer.parseInt(l_ID.getText()));
            stm.setString(2,name.getText());
            stm.setInt(3,Integer.parseInt(profit.getText()));
            stm.executeUpdate();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Loan");
            alert.setHeaderText(throwables+"\nTry Again  !");
            alert.show();
        }
        loadTable();
        clearAll();
        setorderL_ID();
    }

    @FXML
    void btnCancel(ActionEvent event) {
        clearAll();
    }

    @FXML
    void btnDelete(ActionEvent event) {
        try {
            Connection con = DBConnection.getInstance().getConnection();
            PreparedStatement stm = con.prepareStatement("DELETE FROM line WHERE L_ID = ? ");
            stm.setInt(1, table.getSelectionModel().getSelectedItem().getProfit());
            boolean result = stm.executeUpdate() > 0;
            tableItems.remove(table.getSelectionModel().getSelectedItem());
            table.refresh();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Loan");
            alert.setHeaderText(throwables+"\nTry Again  !");
            alert.show();
        }

    }
    private void clearAll(){
        l_ID.setText(null);
        name.setText(null);
        profit.setText(null);
    }
    private void loadTable(){
        ObservableList<Line> tableItems= FXCollections.observableArrayList();
        String sql="SELECT * FROM line";
        try {
            Connection con= DBConnection.getInstance().getConnection();
            PreparedStatement stm =con.prepareStatement(sql);
            ResultSet rs=stm.executeQuery();
            while(rs.next()){
                 Line temp=new Line();
                temp.setlID(rs.getInt(1));
                temp.setName(rs.getString(2));
                temp.setProfit(rs.getInt(3));
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
        tableLID.setCellValueFactory(new PropertyValueFactory<>("lID"));
        tableName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableProfit.setCellValueFactory(new PropertyValueFactory<>("profit"));
    }
}
