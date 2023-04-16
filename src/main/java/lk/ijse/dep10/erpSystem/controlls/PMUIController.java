package lk.ijse.dep10.erpSystem.controlls;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
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

    public Button btnAcceptApprove;
    public Button btnAcceptReject;
    public StackPane stkImage;
    public TextField txtSearchSupplier;
    public Button btnSearch;
    public TextField txtSearchAll;
    public RadioButton rdoApproved;
    public ToggleGroup toggleNew;
    public RadioButton rdoPending;
    public RadioButton RdoReject;
    public RadioButton rdoRejected;
    public Button btnSearchAll;

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

    public void initialize() {
        txtSearchAll.setDisable(true);
        btnAcceptReject.setDisable(true);
        btnAcceptReject.setDisable(true);
        toggleNew.selectedToggleProperty().addListener((value, previous, current) -> {
            txtSearchAll.setDisable(current == null);
            new Thread(()->{
                if (current.equals(rdoApproved)) {
                    txtSearchAll.clear();
                    btnAcceptApprove.setDisable(true);
                    btnAcceptReject.setDisable(false);
                    stkImage.setVisible(false);
                    stkApproved.setVisible(true);
                    stkNotApproved.setVisible(false);
                    tblApproved.getItems().clear();
                    loadDataForTable("APPROVE");

                } else if (current.equals(rdoPending)) {
                    txtSearchAll.clear();
                    btnAcceptApprove.setDisable(false);
                    btnAcceptReject.setDisable(false);
                    stkImage.setVisible(false);
                    stkApproved.setVisible(false);
                    stkNotApproved.setVisible(true);
                    tblNotApproved.getItems().clear();
                    loadDataForTablePending();

                } else if (current.equals(rdoRejected)) {
                    txtSearchAll.clear();
                    btnAcceptReject.setDisable(true);
                    btnAcceptApprove.setDisable(false);
                    stkImage.setVisible(false);
                    stkApproved.setVisible(true);
                    stkNotApproved.setVisible(false);
                    tblApproved.getItems().clear();
                    loadDataForTable("NOT APPROVED");
                }
            }).start();

        });
        txtSearchAll.textProperty().addListener((value,previous,current)->{
            if(current.isEmpty()) {
                new Thread(()->{
                    if (rdoPending.isSelected()) {
                    stkImage.setVisible(false);
                    stkApproved.setVisible(false);
                    stkNotApproved.setVisible(true);
                    tblNotApproved.getItems().clear();
                    loadDataForTablePending();


                } else if (rdoRejected.isSelected()) {
                    stkImage.setVisible(false);
                    stkApproved.setVisible(true);
                    stkNotApproved.setVisible(false);
                    tblApproved.getItems().clear();
                    loadDataForTable("NOT APPROVED");

                } else if (rdoApproved.isSelected()) {
                    stkImage.setVisible(false);
                    stkApproved.setVisible(true);
                    stkNotApproved.setVisible(false);
                    tblApproved.getItems().clear();
                    loadDataForTable("APPROVE");
                }}).start();

            }

        });




        txtSearchSupplier.textProperty().addListener((value, previous, current) -> {
            if (current.isEmpty()) {
                tblSupplier.getItems().clear();
                loadDataForTableSupplier();

            }

        });

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
        tblApproved.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("SupplierId"));
        tblApproved.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblApproved.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("type"));
        tblApproved.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("unit"));
        tblApproved.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        tblApproved.getColumns().get(6).setCellValueFactory(new PropertyValueFactory<>("balanceQuantity"));

        tblNotApproved.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("quotation_number"));
        tblNotApproved.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("SupplierId"));
        tblNotApproved.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblNotApproved.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("type"));
        tblNotApproved.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("unit"));
        tblNotApproved.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        tblNotApproved.getColumns().get(6).setCellValueFactory(new PropertyValueFactory<>("Approval"));

        loadDataForTableSupplier();
//        loadDataForTableApproved();
//        loadDataForTableRejected();
//        loadDataForTablePending();

        stkNotApproved.setVisible(false);
        stkApproved.setVisible(false);
        stkImage.setVisible(true);

    }

    private void loadDataForTablePending() {
        try {


            Connection connection = DBConnection.getInstance().getConnection();
            Statement statement = connection.createStatement();
            String sql = "Select * from Item where approval='PENDING'";
            ResultSet resultSet = statement.executeQuery(sql);
            selectQuarriesAndUpdateTablePending(resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load approved items").showAndWait();
            throw new RuntimeException(e);
        }
    }


    private void loadDataForTable(String condition) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            Statement statement = connection.createStatement();
            String sql = String.format("Select * from Item where approval='%s'", condition);
            ResultSet resultSet = statement.executeQuery(sql);
            selectQuarriesAndUpdateTable(resultSet);

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


    public void btnAcceptApproveOnAction(ActionEvent actionEvent) {
        try {
            if (tblApproved.getSelectionModel().getSelectedItem() != null) {
                Approved selectedItem = tblApproved.getSelectionModel().getSelectedItem();
                Connection connection = DBConnection.getInstance().getConnection();
                Statement stm = connection.createStatement();
                ResultSet resultSet = stm.executeQuery(String.format("SELECT * from Item where id='%s'", selectedItem.getId()));
                resultSet.next();
                String approval = resultSet.getString("approval");
                if (approval.equals("NOT APPROVED")) {
                    decisionApplication("APPROVE", selectedItem.getId());
                    tblApproved.getItems().remove(selectedItem);

                }

            } else if (tblNotApproved.getSelectionModel().getSelectedItem() != null) {
                decisionApplication("APPROVE", tblNotApproved.getSelectionModel().getSelectedItem().getId());
                tblNotApproved.getItems().remove(tblNotApproved.getSelectionModel().getSelectedItem());

            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to edit data base").showAndWait();
            throw new RuntimeException(e);
        }


    }

    public void btnAcceptRejectOnAction(ActionEvent actionEvent) {
        try {
            if (tblApproved.getSelectionModel().getSelectedItem() != null) {
                Approved selectedItem = tblApproved.getSelectionModel().getSelectedItem();
                Connection connection = DBConnection.getInstance().getConnection();
                Statement stm = connection.createStatement();
                ResultSet resultSet = stm.executeQuery(String.format("SELECT * from Item where id='%s'", selectedItem.getId()));
                resultSet.next();
                String approval = resultSet.getString("approval");

                Statement statement = connection.createStatement();
                ResultSet resultSet1 = statement.executeQuery(String.format("select * from Stock where item_id='%s' ", selectedItem.getId()));
                if (resultSet1.next()) {
                    new Alert(Alert.AlertType.ERROR, "Already perched this item").showAndWait();
                    return;
                }
                if (approval.equals("APPROVE")) {
                    decisionApplication("NOT APPROVED", selectedItem.getId());
                    tblApproved.getItems().remove(selectedItem);

                }

            } else if (tblNotApproved.getSelectionModel().getSelectedItem() != null) {
                decisionApplication("NOT APPROVED", tblNotApproved.getSelectionModel().getSelectedItem().getId());
                tblNotApproved.getItems().remove(tblNotApproved.getSelectionModel().getSelectedItem());

            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to edit database").showAndWait();
            throw new RuntimeException(e);
        }


    }

    public void decisionApplication(String condition, int id) {
        try {
            String sql = String.format("Update Item set approval='%s' where id =?", condition);
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement preStm = connection.prepareStatement(sql);
            preStm.setInt(1, id);
            preStm.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to update decision");
            throw new RuntimeException(e);
        }

    }

    public void btnSearchOnAction(ActionEvent actionEvent) {
        try {
            if (!txtSearchSupplier.getText().isEmpty()) {
                tblSupplier.getItems().clear();
                String searchSupplierText = txtSearchSupplier.getText();
                Connection connection = DBConnection.getInstance().getConnection();
                PreparedStatement preStm = connection.prepareStatement("Select * from Supplier where id like ? or name like ?");
                String sqlQuarry = "%" + searchSupplierText + "%";
                preStm.setString(1, sqlQuarry);
                preStm.setString(2, sqlQuarry);
                ResultSet resultSet = preStm.executeQuery();
                ArrayList<String> contactList = new ArrayList<>();
                while (resultSet.next()) {
                    String id = resultSet.getString("id");
                    String name = resultSet.getString("name");
                    String address = resultSet.getString("address");
                    Statement stm = connection.createStatement();
                    contactList.clear();
                    ResultSet resultSet1 = stm.executeQuery(String.format("SELECT * from contact where supplier_id ='%s'", id));
                    while (resultSet1.next()) {
                        contactList.add(resultSet1.getString("contact"));
                    }
                    Supplier supplier = new Supplier(id, name, address, contactList);
                    tblSupplier.getItems().add(supplier);
                }


            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to search such item due to connection loss of data base").showAndWait();
            throw new RuntimeException(e);
        }


    }

    public void searchList(String condition) {

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String searchText = "%" + txtSearchAll.getText().strip() + "%";
            PreparedStatement preStm = connection.prepareStatement(String.format("Select * from Item where (item_type like ? or quotation_number like ?) and approval = '%s'", condition));
            preStm.setString(1, searchText);
            preStm.setString(2, searchText);
            ResultSet resultSet = preStm.executeQuery();
            selectQuarriesAndUpdateTable(resultSet);

            PreparedStatement preStm2 = connection.prepareStatement("Select * from Supplier where name like ?");
            preStm2.setString(1, searchText);
            ResultSet resultSet1 = preStm2.executeQuery();

            while (resultSet1.next()) {
                String id = resultSet1.getString("id");

                Statement stm = connection.createStatement();
                ResultSet resultSet2 = stm.executeQuery(String.format("Select * from Quotation where supplier_id ='%s'", id));
                while (resultSet2.next()) {
                    String quotationNo = resultSet2.getString("quotation_number");
                    Statement stm2 = connection.createStatement();
                    ResultSet resultSet3 = stm2.executeQuery(String.format("Select * from Item where( quotation_number='%s' and approval='%s')", quotationNo, condition));
                    selectQuarriesAndUpdateTable(resultSet3);

                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to search ").showAndWait();
            throw new RuntimeException(e);
        }

    }

    public void selectQuarriesAndUpdateTable(ResultSet resultSet) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement suplierStm = connection.prepareStatement("Select supplier_id from Quotation where quotation_number=?");
            PreparedStatement suplierStm2 = connection.prepareStatement("select * from Supplier where id=?");
            PreparedStatement stockStm = connection.prepareStatement("select * from Stock where item_id=?");

            while (resultSet.next()) {
                String quotationNumber = resultSet.getString("quotation_number");
                String unit = resultSet.getString("unit");
                BigDecimal unitPrice = resultSet.getBigDecimal("price");
                String type = resultSet.getString("item_type");
                int itemNumber = resultSet.getInt("id");

                suplierStm.setString(1, quotationNumber);
                ResultSet resultSet1 = suplierStm.executeQuery();
                resultSet1.next();
                String supplierid = resultSet1.getString(1);


                suplierStm2.setString(1, supplierid);
                ResultSet resultSet2 = suplierStm2.executeQuery();
                resultSet2.next();
                String supplierName = resultSet2.getString("name");


                stockStm.setInt(1, itemNumber);
                ResultSet resultSet3 = stockStm.executeQuery();
                String balanced_quantity = "";
                if (resultSet3.next()) {
                    balanced_quantity = resultSet3.getString("balanced_quantity");
                } else {
                    if (rdoRejected.isSelected()) {
                        balanced_quantity = "-";

                    } else {
                        balanced_quantity = "Not ordered";
                    }
                }

                Approved approvedItem = new Approved(itemNumber,quotationNumber, supplierid, supplierName, type, unit, unitPrice, balanced_quantity);
                tblApproved.getItems().add(approvedItem);

            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to execute SQL Quarries").showAndWait();
            throw new RuntimeException(e);
        }

    }

    public void selectQuarriesAndUpdateTablePending(ResultSet resultSet) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement suplierStm = connection.prepareStatement("Select supplier_id from Quotation where quotation_number=?");
            PreparedStatement suplierStm2 = connection.prepareStatement("select name from Supplier where id=?");
            while (resultSet.next()) {
                String quatationNumber = resultSet.getString("quotation_number");
                String unit = resultSet.getString("unit");
                BigDecimal unitPrice = resultSet.getBigDecimal("price");
                String type = resultSet.getString("item_type");
                int itemNumber =resultSet.getInt("id");


                suplierStm.setString(1, quatationNumber);
                ResultSet resultSet1 = suplierStm.executeQuery();
                resultSet1.next();
                String suplierid = resultSet1.getString(1);

                suplierStm2.setString(1, suplierid);
                ResultSet resultSet2 = suplierStm2.executeQuery();
                resultSet2.next();
                String supplierName = resultSet2.getString(1);


                NotApproved notApprovedItem = new NotApproved(itemNumber,quatationNumber, suplierid, supplierName, type, unit, unitPrice);
                tblNotApproved.getItems().add(notApprovedItem);


            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to execute SQL Quarries").showAndWait();
            throw new RuntimeException(e);
        }
    }

    public void searchListPending() {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String searchText = "%" + txtSearchAll.getText().strip() + "%";
            PreparedStatement preStm = connection.prepareStatement(String.format("Select * from Item where (item_type like ? or quotation_number like ?) and approval = '%s'", "PENDING"));
            preStm.setString(1, searchText);
            preStm.setString(2, searchText);
            ResultSet resultSet = preStm.executeQuery();
            selectQuarriesAndUpdateTablePending(resultSet);

            PreparedStatement preStm2 = connection.prepareStatement("Select * from Supplier where name like ?");
            preStm2.setString(1, searchText);
            ResultSet resultSet1 = preStm2.executeQuery();

            while (resultSet1.next()) {
                String id = resultSet1.getString("id");

                Statement stm = connection.createStatement();
                ResultSet resultSet2 = stm.executeQuery(String.format("Select * from Quotation where supplier_id ='%s'", id));
                while (resultSet2.next()) {
                    String quotationNo = resultSet2.getString("quotation_number");
                    Statement stm2 = connection.createStatement();
                    System.out.println("Hello lady girl");
                    ResultSet resultSet3 = stm2.executeQuery(String.format("Select * from Item where( quotation_number='%s' and approval='%s')", quotationNo, "PENDING"));
                    selectQuarriesAndUpdateTablePending(resultSet3);

                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to Search").showAndWait();
            throw new RuntimeException(e);
        }


    }

    public void btnSearchAllOnAction(ActionEvent actionEvent) {

                if (rdoApproved.isSelected()) {
                    tblApproved.getItems().clear();
                    searchList("APPROVE");


                } else if (rdoRejected.isSelected()) {
                    tblApproved.getItems().clear();
                    searchList("NOT APPROVED");

                } else if (rdoPending.isSelected()) {
                    tblNotApproved.getItems().clear();
                    searchListPending();
                }

            }






}
