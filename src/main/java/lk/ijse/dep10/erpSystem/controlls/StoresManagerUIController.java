package lk.ijse.dep10.erpSystem.controlls;

import com.mysql.cj.util.StringInspector;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.ijse.dep10.erpSystem.db.DBConnection;
import lk.ijse.dep10.erpSystem.models.Supplier;
import lk.ijse.dep10.erpSystem.models.SupplierListModel;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class StoresManagerUIController {

    @FXML
    private Button btnIssues;

    @FXML
    private Button btnNewSupplier;

    @FXML
    private Button btnOrders;

    @FXML
    private Button btnPurchase;

    @FXML
    private Button btnQuotations;

    @FXML
    private Button btnStock;

    @FXML
    private Button btnSuppliers;

    @FXML
    private VBox issuesPane;

    @FXML
    private VBox ordersPane;

    @FXML
    private VBox purchasePane;

    @FXML
    private VBox quotationPane;

    @FXML
    private StackPane stkQuotation;

    @FXML
    private VBox stockPane;

    @FXML
    private VBox supplierPane;

    @FXML
    private TableView<?> tblIssue;

    @FXML
    private TableView<?> tblOrders;

    @FXML
    private TableView<?> tblPurchase;

    @FXML
    private TableView<?> tblQuotation;

    @FXML
    private TableView<?> tblStock;

    @FXML
    private TableView<Supplier> tblSuppliers;

    @FXML
    private TextField txtSupplier;
    private SupplierListModel supplierListModel;

    public Button addQuotation;

    public void initialize() {
        supplierListModel=new SupplierListModel();
        tblSuppliers.setItems(supplierListModel.getSupplierList());
        tblSuppliers.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblSuppliers.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblSuppliers.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("address"));
        tblSuppliers.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("contactNumbers"));
        tblSuppliers.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("bank"));
        tblSuppliers.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("accountNumber"));
        tblSuppliers.getColumns().get(6).setCellValueFactory(new PropertyValueFactory<>("addQuotation"));

        Connection connection = DBConnection.getInstance().getConnection();
        try {
            Statement stmSupplier = connection.createStatement();
            ResultSet rst = stmSupplier.executeQuery("SELECT *FROM Supplier");
            PreparedStatement stmContact = connection.prepareStatement("SELECT *FROM contact WHERE supplier_id=?");
            ObservableList<Supplier> supplierList = tblSuppliers.getItems();
            while (rst.next()) {
                int db_id = rst.getInt(1);
                String name = rst.getString(2);
                String address = rst.getString(3);
                String bank = rst.getString(4);
                String accountNumber = rst.getString(5);
                stmContact.setInt(1,db_id);
                ResultSet rstContact = stmContact.executeQuery();
                ArrayList<String> contactList = new ArrayList<>();
                while (rstContact.next()) {
                    String contact = rstContact.getString(1);
                    contactList.add(contact);
                }
                StringBuilder sb = new StringBuilder();
                String id = String.valueOf(sb.append(db_id).append("/").append(name.substring(0,3)));
                Supplier supplier = new Supplier(id, name, address, bank, accountNumber, contactList);
                supplierList.add(supplier);
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,"Failed to connect with Database..check the Connection").showAndWait();
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        tblSuppliers.getSelectionModel().selectedItemProperty().addListener((observableValue, supplier, current) ->{
            if (current!=null) System.out.println("select supplier");
        } );
        tblSuppliers.itemsProperty().addListener((observableValue, suppliers, current) -> {
            if(current!=null) System.out.println("ok");

        });

    }

    @FXML
    void btnQuotationOnAction(ActionEvent event) {
            quotationPane.toFront();
        if (tblQuotation.getItems().isEmpty()) {
            tblQuotation.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>(""));
        }
    }

    @FXML
    void btnSuppliersOnAction(ActionEvent event) {
        supplierPane.toFront();
    }

    public void btnPurchaseOnAction(ActionEvent event) {
        purchasePane.toFront();
    }

    public void btnStockOnAction(ActionEvent event) {
        stockPane.toFront();
    }

    public void btnOrdersOnAction(ActionEvent event) {
        ordersPane.toFront();
    }

    public void btnIssuesOnAction(ActionEvent event) {
        issuesPane.toFront();
    }

    public void btnNewSupplierOnAction(ActionEvent event) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/SupplierRegisterUI.fxml"));
            AnchorPane root = fxmlLoader.load();
            SupplierRegisterUIController controller = fxmlLoader.getController();
            controller.setSupplierListModel(supplierListModel);
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.setResizable(false);
            stage.setTitle("New Supplier Register");
            stage.show();
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR,"Failed to load new Supplier register window. Please contact the technical team").showAndWait();
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
