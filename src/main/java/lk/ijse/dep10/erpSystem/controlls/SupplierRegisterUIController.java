package lk.ijse.dep10.erpSystem.controlls;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.sun.prism.shader.Mask_TextureSuper_Loader;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import lk.ijse.dep10.erpSystem.db.DBConnection;
import lk.ijse.dep10.erpSystem.models.Supplier;
import lk.ijse.dep10.erpSystem.models.SupplierListModel;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class SupplierRegisterUIController {

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnRegister;

    @FXML
    private ListView<String> lstContacts;

    @FXML
    private TextField txtAccountNo;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtBank;

    @FXML
    private TextField txtContact;

    @FXML
    private TextField txtID;

    @FXML
    private TextField txtName;
    private SupplierListModel supplierListModel;

    public void initialize() {
        txtID.setDisable(true);
        clearAll();

        txtContact.textProperty().addListener((observableValue, s, current) ->{
            txtContact.getStyleClass().remove("invalid");
            if (!current.matches("[0-9]{3}-[0-9]{7}")) {
                txtContact.getStyleClass().add("invalid");
                txtContact.selectAll();
                txtContact.requestFocus();
            }
        } );

        txtAccountNo.textProperty().addListener((observableValue, s, current) ->{
            txtAccountNo.getStyleClass().remove("invalid");
            if (!current.matches("[0-9]{5,}")) {
                txtAccountNo.getStyleClass().add("invalid");
                txtAccountNo.selectAll();
                txtAccountNo.requestFocus();
            }
        } );

        txtBank.textProperty().addListener((observableValue, s, current) ->{
            txtBank.getStyleClass().remove("invalid");
            if (!current.matches("[A-Za-z ]{3,}")) {
                txtBank.getStyleClass().add("invalid");
                txtBank.selectAll();
                txtBank.requestFocus();
            }
        } );

        txtAddress.textProperty().addListener((observableValue, s, current) ->{
            txtAddress.getStyleClass().remove("invalid");
            if (!current.matches("[A-Za-z 1-9,]{4,}")) {
                txtAddress.getStyleClass().add("invalid");
                txtAddress.selectAll();
                txtAddress.requestFocus();
            }
        } );

        txtName.textProperty().addListener((observableValue, s, current) ->{
            txtName.getStyleClass().remove("invalid");
            if (!current.matches("[A-Za-z ]{3,}")) {
                txtName.getStyleClass().add("invalid");
                txtName.selectAll();
                txtName.requestFocus();
            }
        } );

        lstContacts.getSelectionModel().selectedItemProperty().addListener((observableValue, s, current) -> {
            if(current!=null) btnDelete.setDisable(false);
        });


    }

    private void clearAll() {
        for (TextField textField : new TextField[]{txtAccountNo,txtBank,txtAddress,txtName,txtContact}) {
            textField.clear();
            textField.getStyleClass().remove("invalid");
        }
        lstContacts.getItems().clear();
        lstContacts.refresh();

    }

    @FXML
    void btnAddOnAction(ActionEvent event) {
        String contact = txtContact.getText().strip();
        if (lstContacts.getItems().contains(contact)) {
            new Alert(Alert.AlertType.ERROR,"Contact Number already entered").showAndWait();
            if(!txtContact.getStyleClass().contains("invalid")) txtContact.getStyleClass().add("invalid");
            txtContact.selectAll();
            txtContact.requestFocus();
            return;
        }

        if (txtContact.getStyleClass().contains("invalid") || contact.isEmpty()) {
            if(!txtContact.getStyleClass().contains("invalid")) txtContact.getStyleClass().add("invalid");
            txtContact.selectAll();
            txtContact.requestFocus();
            return;
        }

        lstContacts.getItems().add(contact);
        txtContact.clear();
        txtContact.requestFocus();
        txtContact.getStyleClass().remove("invalid");
        lstContacts.getStyleClass().remove("invalid");
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        lstContacts.getItems().remove(lstContacts.getSelectionModel().getSelectedItem());
        if (lstContacts.getItems().isEmpty()) btnDelete.setDisable(true);
    }

    @FXML
    void btnRegisterOnAction(ActionEvent event) {
        String name = txtName.getText().strip();
        String address = txtAddress.getText().strip();
        String bank = txtBank.getText().strip();
        String accountNo = txtAccountNo.getText().strip();
        ObservableList<String> contactList = lstContacts.getItems();
        if (!isDataValid()) return;
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            PreparedStatement stmSupplier = connection.prepareStatement("INSERT INTO Supplier(name, address, bank, account_number) VALUES (?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
            PreparedStatement stmContact = connection.prepareStatement("INSERT INTO contact(contact, supplier_id) VALUES (?,?)");
            stmSupplier.setString(1,name);
            stmSupplier.setString(2,address);
            stmSupplier.setString(3,bank);
            stmSupplier.setString(4,accountNo);
            stmSupplier.executeUpdate();
            ResultSet rst = stmSupplier.getGeneratedKeys();
            boolean next = rst.next();
            int key = rst.getInt(1);
            System.out.println(key);
            StringBuilder sb = new StringBuilder();
            String id = String.valueOf(sb.append(key).append("/").append(name.substring(0,3)));
            for (String contact : contactList) {
                stmContact.setString(1, contact);
                stmContact.setInt(2, key);
                stmContact.executeUpdate();
            }
            Supplier supplier = new Supplier(id, name, address, bank, accountNo, new ArrayList<>(contactList));
            System.out.println(supplier);
            supplierListModel.getSupplierList().add(supplier);
            clearAll();
            Stage stage = (Stage) btnRegister.getScene().getWindow();
            stage.close();


        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Failed to connect with the dataBase check the network connection..").showAndWait();
            throw new RuntimeException(e);
        }

    }

    public void setSupplierListModel(SupplierListModel supplierListModel) {
        this.supplierListModel=supplierListModel;
    }

    private boolean isDataValid() {
        boolean dataValid=true;
        if (lstContacts.getItems().isEmpty()) {
            lstContacts.getStyleClass().add("invalid");
            dataValid=false;
        }
        for (TextField textField : new TextField[]{txtAccountNo,txtBank,txtAddress,txtName}) {

            if (textField.getText().isEmpty()||textField.getStyleClass().contains("invalid")) {
                textField.selectAll();
                if (!textField.getStyleClass().contains("invalid"))textField.getStyleClass().add("invalid");
                textField.requestFocus();
                dataValid=false;
            }
        }

        return dataValid;
    }
}
