package lk.ijse.dep10.erpSystem.util;

import lk.ijse.dep10.erpSystem.model.Unit;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class Approved {
    private String QuotationNumber;
    private String supplierId;
    private String name;
    private String type;
    private Unit unit;
    private BigDecimal unitPrice;
    private String balanceQuantity;
}
