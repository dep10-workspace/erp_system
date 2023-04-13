package lk.ijse.dep10.erpSystem.controlls;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import lk.ijse.dep10.erpSystem.db.DBConnection;
import lk.ijse.dep10.erpSystem.model.Unit;
import lk.ijse.dep10.erpSystem.util.Approved;
import lk.ijse.dep10.erpSystem.util.NotApproved;
import lk.ijse.dep10.erpSystem.util.Supplier;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class PMUIController {

    public TableView<Approved> tblApproved;
    public Button btnRejected;
    public Button btnSaveChanges;
    @FXML
    private Button btnApproved;

    @FXML
    private Button btnPending;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblTime;

    @FXML
    private TableView<Approved> lstApproved;

    @FXML
    private StackPane stkApproved;

    @FXML
    private StackPane stkNotApproved;

    @FXML
    private TableView<NotApproved> tblNotApproved;

    @FXML
    private TableView<Supplier> tblSupplier;

    @FXML
    void btnApprovedOnAction(ActionEvent event) {

    }

    @FXML
    void btnPendingOnAction(ActionEvent event) {

    }

    public void initialize() {
        KeyFrame key = new KeyFrame(Duration.seconds(1), event -> {
            lblTime.setText("Time: " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            lblDate.setText("Date: " + LocalDate.now());

        });
        Timeline timeline = new Timeline(key);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        tblSupplier.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblSupplier.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblSupplier.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("address"));
        tblSupplier.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("ContactComb"));

        tblApproved.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("QuotationNumber"));
        tblApproved.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblApproved.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("type"));
        tblApproved.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("unit"));
        tblApproved.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        tblApproved.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("balanceQuantity"));

        loadDataForTableSupplier();
        loadDataForTableApproved();
        loadDataForTableRejected();

        stkNotApproved.setDisable(true);
        stkApproved.setDisable(true);

    }

    private void loadDataForTableRejected() {
        try {


            Connection connection = DBConnection.getInstance().getConnection();
            Statement statement = connection.createStatement();
            String sql = "Select * from Item where approval='NOT APPROVED'";
            ResultSet resultSet = statement.executeQuery(sql);
            PreparedStatement suplierStm = connection.prepareStatement("Select supplier_id from Quotation where quotation_number=?");
            PreparedStatement suplierStm2 = connection.prepareStatement("select name from Supplier where id=?");



            while (resultSet.next()) {
                String quatationNumber = resultSet.getString("quotation_number");
                String unit = resultSet.getString("unit");
                BigDecimal unitPrice = resultSet.getBigDecimal("price");
                String type = resultSet.getString("item_type");
                String itemNumber = resultSet.getString("item_number");

                suplierStm.setString(1, quatationNumber);
                ResultSet resultSet1 = suplierStm.executeQuery();
                resultSet1.next();
                String suplierid = resultSet1.getString(1);

                suplierStm2.setString(1, suplierid);
                ResultSet resultSet2 = suplierStm2.executeQuery();
                resultSet2.next();
                String supplierName = resultSet2.getString(1);

                String balanced_quantity="-";

                Approved approvedItem = new Approved(quatationNumber, supplierName, type, Unit.valueOf(unit), unitPrice, balanced_quantity);
                tblApproved.getItems().add(approvedItem);


            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load approved items").showAndWait();
            throw new RuntimeException(e);
        }
    }

    private void loadDataForTableApproved() {
        try {


            Connection connection = DBConnection.getInstance().getConnection();
            Statement statement = connection.createStatement();
            String sql = "Select * from Item where approval='APPROVED'";
            ResultSet resultSet = statement.executeQuery(sql);
            PreparedStatement suplierStm = connection.prepareStatement("Select supplier_id from Quotation where quotation_number=?");
            PreparedStatement suplierStm2 = connection.prepareStatement("select name from Supplier where id=?");
            PreparedStatement stockStm = connection.prepareStatement("select * from Stock where item_id=?");


            while (resultSet.next()) {
                String quatationNumber = resultSet.getString("quotation_number");
                String unit = resultSet.getString("unit");
                BigDecimal unitPrice = resultSet.getBigDecimal("price");
                String type = resultSet.getString("item_type");
                String itemNumber = resultSet.getString("item_number");

                suplierStm.setString(1, quatationNumber);
                ResultSet resultSet1 = suplierStm.executeQuery();
                resultSet1.next();
                String suplierid = resultSet1.getString(1);

                suplierStm2.setString(1, suplierid);
                ResultSet resultSet2 = suplierStm2.executeQuery();
                resultSet2.next();
                String supplierName = resultSet2.getString(1);

                stockStm.setString(1, itemNumber);
                ResultSet resultSet3 = stockStm.executeQuery();
                resultSet3.next();
                String balanced_quantity = resultSet3.getString("name");

                Approved approvedItem = new Approved(quatationNumber, supplierName, type, Unit.valueOf(unit), unitPrice, balanced_quantity);
                tblApproved.getItems().add(approvedItem);


            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load approved items").showAndWait();
            throw new RuntimeException(e);
        }
    }

    private void loadDataForTableSupplier() {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            Statement statement = connection.createStatement();
            PreparedStatement preparedStatement = connection.prepareStatement("Select contact from contact where supplier_id=?");
            ResultSet resultSet = statement.executeQuery("Select id,name,address from Supplier");
            ArrayList<String> contactList = new ArrayList<>();
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                preparedStatement.setString(1, id);
                ResultSet resultSet1 = preparedStatement.executeQuery();
                contactList.clear();

                while (resultSet1.next()) {
                    String contact = resultSet1.getString(1);
                    contactList.add(contact);
                }
                Supplier supplier = new Supplier(id, name, address, contactList);
                tblSupplier.getItems().add(supplier);

            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to connact with database tables").showAndWait();
            throw new RuntimeException(e);
        }
    }

    public void btnRejectedOnAction(ActionEvent actionEvent) {

    }

    public void btnSaveChangesOnAction(ActionEvent actionEvent) {
    }
}
