package lk.ijse.dep10.erpSystem.controlls;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class StoresManagerUIController {

    public Button btnSuppliers;
    public VBox quotationPane;
    public VBox supplierPane;
    public Button btnQuotations;
    public Button btnPurchase;
    public Button btnStock;
    public Button btnOrders;
    public Button btnIssues;
    public VBox purchasePane;
    public VBox stockPane;
    public VBox ordersPane;
    public VBox issuesPane;
    @FXML
    private Button btnQuotation;

    @FXML
    private Button btnSupply;



    public void initialize() {

    }

    @FXML
    void btnQuotationOnAction(ActionEvent event) {
            quotationPane.toFront();

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
}
