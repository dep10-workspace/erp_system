package lk.ijse.dep10.erpSystem.models;

import javafx.scene.control.Button;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;

@NoArgsConstructor
@Data
public class Supplier implements Serializable {
    private String id;
    private String name;
    private String address;
    private String bank;
    private String accountNumber;
    private ArrayList<String> contactNumbers;
    private Button addQuotation;

    public Supplier(String id, String name, String address, String bank, String accountNumber, ArrayList<String> contactNumbers) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.bank = bank;
        this.accountNumber = accountNumber;
        this.contactNumbers = contactNumbers;
        this.addQuotation = new Button("+Quotation");
    }
}
