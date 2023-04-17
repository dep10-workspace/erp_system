package lk.ijse.dep10.erpSystem.controlls;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import lk.ijse.dep10.erpSystem.db.DBConnection;
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
    public StackPane stkLoading;


    @FXML
    private Label lblDate;

    @FXML
    private Label lblTime;

    @FXML
    private StackPane stkApproved;

    @FXML
    private StackPane stkNotApproved;

    @FXML
    private TableView<NotApproved> tblNotApproved;

    @FXML
    private TableView<Supplier> tblSupplier;

    public void initialize() {
        stkLoading.setVisible(false);
        txtSearchAll.setDisable(true);
        btnAcceptReject.setDisable(true);
        btnAcceptReject.setDisable(true);

        toggleNew.selectedToggleProperty().addListener((value, previous, current) -> {
            txtSearchAll.setDisable(current == null);
            new Thread(() -> {
                if (current.equals(rdoApproved)) {
                    stkApproved.setDisable(true);
                    txtSearchAll.clear();
                    btnAcceptApprove.setDisable(true);
                    btnAcceptReject.setDisable(false);
                    stkImage.setVisible(false);
                    stkApproved.setVisible(true);
                    stkNotApproved.setVisible(false);
                    tblApproved.getItems().clear();
                    loadDataForTable("APPROVE");
                    stkApproved.setDisable(false);

                } else if (current.equals(rdoPending)) {
                    stkNotApproved.setDisable(true);
                    txtSearchAll.clear();
                    btnAcceptApprove.setDisable(false);
                    btnAcceptReject.setDisable(false);
                    stkImage.setVisible(false);
                    stkApproved.setVisible(false);
                    stkNotApproved.setVisible(true);
                    tblNotApproved.getItems().clear();
                    loadDataForTable("PENDING");
                    stkNotApproved.setDisable(false);

                } else if (current.equals(rdoRejected)) {
                    stkApproved.setDisable(true);
                    txtSearchAll.clear();
                    btnAcceptReject.setDisable(true);
                    btnAcceptApprove.setDisable(false);
                    stkImage.setVisible(false);
                    stkApproved.setVisible(true);
                    stkNotApproved.setVisible(false);
                    tblApproved.getItems().clear();
                    loadDataForTable("NOT APPROVED");
                    stkApproved.setDisable(false);
                }
            }).start();
            stkLoading.setVisible(false);

        });
        txtSearchAll.textProperty().addListener((value, previous, current) -> {

            if (current.isEmpty()) {

                new Thread(() -> {
                    if (rdoPending.isSelected()) {
                        stkNotApproved.setDisable(true);
                        stkImage.setVisible(false);
                        stkApproved.setVisible(false);
                        stkNotApproved.setVisible(true);
                        tblNotApproved.getItems().clear();
                        loadDataForTable("PENDING");
                        stkNotApproved.setDisable(false);


                    } else if (rdoRejected.isSelected()) {
                        stkApproved.setDisable(true);
                        stkImage.setVisible(false);
                        stkApproved.setVisible(true);
                        stkNotApproved.setVisible(false);
                        tblApproved.getItems().clear();
                        loadDataForTable("NOT APPROVED");
                        stkApproved.setDisable(false);

                    } else if (rdoApproved.isSelected()) {
                        stkApproved.setDisable(true);
                        stkImage.setVisible(false);
                        stkApproved.setVisible(true);
                        stkNotApproved.setVisible(false);
                        tblApproved.getItems().clear();
                        loadDataForTable("APPROVE");
                        stkApproved.setDisable(false);
                    }
                }).start();

            }

        });


        txtSearchSupplier.textProperty().addListener((value, previous, current) -> {
            if (current.isEmpty()) {
                new Thread(()->{
                    tblSupplier.setDisable(true);
                    tblSupplier.getItems().clear();
                    loadDataForTableSupplier();
                    tblSupplier.setDisable(false);
                }).start();


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

        stkNotApproved.setVisible(false);
        stkApproved.setVisible(false);
        stkImage.setVisible(true);

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
            new Alert(Alert.AlertType.ERROR, "Fail to load data from database").showAndWait();
            throw new RuntimeException(e);
        }
    }

    private void loadDataForTableSupplier() {
        try {
            ObservableList<Supplier>supplyTable =tblSupplier.getItems();
            Connection connection = DBConnection.getInstance().getConnection();
            Statement supplierStm = connection.createStatement();
            ResultSet resultSet = supplierStm.executeQuery("Select * from Supplier");
            PreparedStatement contactStm = connection.prepareStatement("Select * from contact where supplier_id=?");

            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                contactStm.setString(1, id);
                ResultSet resultSet1 = contactStm.executeQuery();
                ArrayList<String> contactList = new ArrayList<>();

                while (resultSet1.next()) {
                    String contact = resultSet1.getString("contact");
                    contactList.add(contact);

                }
                Supplier supplier = new Supplier(id, name, address, contactList);
                supplyTable.add(supplier);




            }

        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to connect with database").showAndWait();
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
                Connection connection = DBConnection.getInstance().getConnection();
                PreparedStatement supplierpreStm = connection.prepareStatement("Select * from Supplier where id like ? or name like ? or address like ?");
                String sqlQuarry = "%" + txtSearchSupplier.getText() + "%";
                supplierpreStm.setString(1, sqlQuarry);
                supplierpreStm.setString(2, sqlQuarry);
                supplierpreStm.setString(3, sqlQuarry);
                ResultSet resultSet = supplierpreStm.executeQuery();

                while (resultSet.next()) {
                    String id = resultSet.getString("id");
                    String name = resultSet.getString("name");
                    String address = resultSet.getString("address");
                    Statement stm = connection.createStatement();
                    ArrayList<String> contactList = new ArrayList<>();
                    ResultSet resultSet1 = stm.executeQuery(String.format("SELECT * from contact where supplier_id ='%s'", id));
                    while (resultSet1.next()) {
                        String contact = resultSet1.getString("contact");
                        contactList.add(contact);
                    }
//                    System.out.println(contactList);
                    Supplier supplier = new Supplier(id, name, address, contactList);
                    tblSupplier.getItems().add(supplier);
                }


            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load supplier details from data base ").showAndWait();
            throw new RuntimeException(e);
        }


    }

    public void searchList(String condition)

    {

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
            PreparedStatement quotationStm = connection.prepareStatement("Select supplier_id from Quotation where quotation_number=?");
            PreparedStatement suplierStm = connection.prepareStatement("select * from Supplier where id=?");
            PreparedStatement stockStm = connection.prepareStatement("select * from Stock where item_id=?");

            while (resultSet.next()) {
                String quotationNumber = resultSet.getString("quotation_number");
                String unit = resultSet.getString("unit");
                BigDecimal unitPrice = resultSet.getBigDecimal("price");
                String type = resultSet.getString("item_type");
                int itemNumber = resultSet.getInt("id");

                quotationStm.setString(1, quotationNumber);
                ResultSet resultSet1 = quotationStm.executeQuery();
                resultSet1.next();
                String supplierid = resultSet1.getString(1);


                suplierStm.setString(1, supplierid);
                ResultSet resultSet2 = suplierStm.executeQuery();
                resultSet2.next();
                String supplierName = resultSet2.getString("name");

                if(rdoApproved.isSelected() || rdoRejected.isSelected()) {
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

                    Approved approvedItem = new Approved(itemNumber, quotationNumber, supplierid, supplierName, type, unit, unitPrice, balanced_quantity);
                    tblApproved.getItems().add(approvedItem);
                } else if (rdoPending.isSelected()) {
                    NotApproved notApprovedItem = new NotApproved(itemNumber, quotationNumber, supplierid, supplierName, type, unit, unitPrice);
                    tblNotApproved.getItems().add(notApprovedItem);

                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load required data from database").showAndWait();
            throw new RuntimeException(e);
        }

    }


    public void btnSearchAllOnAction(ActionEvent actionEvent) {

        if (rdoApproved.isSelected()) {
            stkApproved.setDisable(true);
            tblApproved.getItems().clear();
            searchList("APPROVE");
            stkApproved.setDisable(false);


        } else if (rdoRejected.isSelected()) {
            stkApproved.setDisable(true);
            tblApproved.getItems().clear();
            searchList("NOT APPROVED");
            stkApproved.setDisable(false);

        } else if (rdoPending.isSelected()) {
            stkNotApproved.setDisable(true);
            tblNotApproved.getItems().clear();
            searchList("PENDING");
            stkNotApproved.setDisable(false);
        }

    }


}
