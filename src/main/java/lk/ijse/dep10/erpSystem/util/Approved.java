package lk.ijse.dep10.erpSystem.util;

import lk.ijse.dep10.erpSystem.model.Unit;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;



public class Approved {
    private String QuotationNumber;
    private String supplierId;
    private String name;

    public Approved(String quotationNumber, String supplierId, String name, String type, Unit unit, BigDecimal unitPrice, String balanceQuantity) {
        QuotationNumber = quotationNumber;
        this.supplierId = supplierId;
        this.name = name;
        this.type = type;
        this.unit = unit;
        this.unitPrice = unitPrice;
        this.balanceQuantity = balanceQuantity;
    }

    private String type;
    private Unit unit;
    private BigDecimal unitPrice;
    private String balanceQuantity;

    public String getQuotationNumber() {
        return QuotationNumber;
    }

    public void setQuotationNumber(String quotationNumber) {
        QuotationNumber = quotationNumber;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getBalanceQuantity() {
        return balanceQuantity;
    }

    public void setBalanceQuantity(String balanceQuantity) {
        this.balanceQuantity = balanceQuantity;
    }
}
