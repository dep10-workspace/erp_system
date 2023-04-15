package lk.ijse.dep10.erpSystem.util;

import lk.ijse.dep10.erpSystem.model.Unit;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
public class NotApproved {
    private String quotation_number;
    private String name;
    private String type;
    private Unit unit;
    private BigDecimal unitPrice;

    public String getApproval(){
        return "PENDING";
    }

}
