package lk.ijse.dep10.erpSystem.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SupplierListModel {
    private final ObservableList<Supplier> supplierList;

    public SupplierListModel() {
        this.supplierList = FXCollections.observableArrayList();
    }

    public ObservableList<Supplier> getSupplierList() {
        return supplierList;
    }
}
