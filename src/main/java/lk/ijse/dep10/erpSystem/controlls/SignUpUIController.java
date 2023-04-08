package lk.ijse.dep10.erpSystem.controlls;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import lk.ijse.dep10.erpSystem.Role;
import lk.ijse.dep10.erpSystem.db.DBConnection;
import lk.ijse.dep10.erpSystem.util.PasswordEncoder;

import javax.crypto.spec.GCMParameterSpec;
import java.sql.*;

public class SignUpUIController {

    public ComboBox cmbRole;
    @FXML
    private Button btnCreate;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private PasswordField txtRePassword;

    public void initialize() {
        cmbRole.getItems().addAll("Admin", "Project Manager", "Site Manager", "Stores Manager", "Document Controller");
        cmbRole.getSelectionModel().select("Admin");
        cmbRole.setDisable(true);
        txtName.textProperty().addListener((observableValue, s, current) -> {
            txtName.getStyleClass().remove("invalid");
            if (!current.matches("[A-Za-z ]{3,}" )&& !txtName.getStyleClass().contains("invalid")) txtName.getStyleClass().add("invalid");

        });
        txtId.textProperty().addListener((observableValue, s, current) -> {
            txtId.getStyleClass().remove("invalid");
            if (!current.matches("E-[0-9]{3,}" )&& !txtId.getStyleClass().contains("invalid")) txtId.getStyleClass().add("invalid");

        });
        txtPassword.textProperty().addListener((observableValue, s, current) -> {
            txtPassword.getStyleClass().remove("invalid");
            if (!current.matches("[\\S]{8,}" )&& !txtPassword.getStyleClass().contains("invalid")) txtPassword.getStyleClass().add("invalid");

        });
        txtRePassword.textProperty().addListener((observableValue, s, current) -> {
            txtRePassword.getStyleClass().remove("invalid");
            if (!current.matches("[\\S]{8,}" ) && !txtRePassword.getStyleClass().contains("invalid")) txtRePassword.getStyleClass().add("invalid");

        });
    }

    @FXML
    void btnCreateOnAction(ActionEvent event) {
        if(!isDataValid())return;
        String name = txtName.getText().strip();
        String id = txtId.getText().strip();
        String password =txtPassword.getText();
        Connection connection = DBConnection.getInstance().getConnection();
        boolean adminExist = false;
        try {
            Statement stm = connection.createStatement();
            String sql = "SELECT *FROM User";
            ResultSet resultSet = stm.executeQuery(sql);
            while (resultSet.next()) {
                if (resultSet.getString(2).equals(id)) {
                    new Alert(Alert.AlertType.ERROR, "Employee id is already registered").showAndWait();
                    return;
                }
                if(resultSet.getString(3).equals(Role.ADMIN.toString())) adminExist = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Network Connection issue......try again");
            throw new RuntimeException(e);
        }
        try {
            PreparedStatement stm = connection.prepareStatement("INSERT INTO User(full_name, id, role, password) VALUES (?,?,?,?)");
            stm.setString(1, name);
            stm.setString(2,id);
            stm.setString(3,Role.ADMIN.toString());
            stm.setString(4, PasswordEncoder.encode(password));
            stm.executeUpdate();
            clearField();

        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Failed to save in database....try again").showAndWait();
            throw new RuntimeException(e);
        }
    }

    private void clearField() {
        for (Node node:new Node[]{txtId,txtName,txtPassword,txtRePassword}) {
            if (node instanceof TextField) ((TextField) node).clear();
            if (node instanceof PasswordField) ((PasswordField) node).clear();
            node.getStyleClass().remove("invalid");
        }
        txtName.requestFocus();
    }

    private boolean isDataValid() {
        boolean dataValid=true;

        String password = txtPassword.getText().strip();
        String repeatPassword = txtRePassword.getText().strip();

        if (!password.equals(repeatPassword)) {
            if (!txtRePassword.getStyleClass().contains("invalid")) txtRePassword.getStyleClass().add("invalid");
            txtRePassword.requestFocus();
            txtRePassword.selectAll();
            dataValid=false;
        }

        for (PasswordField pswrdField:new PasswordField[]{txtRePassword,txtPassword}) {
            if (pswrdField.getText().strip().isEmpty() || pswrdField.getStyleClass().contains("invalid")) {
                if (!pswrdField.getStyleClass().contains("invalid")) pswrdField.getStyleClass().add("invalid");
                pswrdField.selectAll();
                pswrdField.requestFocus();
                dataValid=false;
            }
        }
        for (TextField txtField:new TextField[]{txtId,txtName}) {
            if (txtField.getText().strip().isEmpty() || txtField.getStyleClass().contains("invalid")) {
                if (!txtField.getStyleClass().contains("invalid")) txtField.getStyleClass().add("invalid");
                txtField.selectAll();
                txtField.requestFocus();
                dataValid=false;
            }
        }
        return dataValid;
    }

    public void cmbRoleOnAction(ActionEvent event) {
    }
}
