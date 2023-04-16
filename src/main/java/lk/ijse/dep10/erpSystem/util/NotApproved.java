package lk.ijse.dep10.erpSystem.util;

import lk.ijse.dep10.erpSystem.model.Unit;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

public class NotApproved {
    private String quotation_number;
    private String SupplierId;
    private String name;
    private String type;
    private Unit unit;
    private BigDecimal unitPrice;

    public String getApproval(){
        return "PENDING";
    }

    public NotApproved(String quotation_number, String supplierId, String name, String type, Unit unit, BigDecimal unitPrice) {
        this.quotation_number = quotation_number;
        SupplierId = supplierId;
        this.name = name;
        this.type = type;
        this.unit = unit;
        this.unitPrice = unitPrice;
    }

    public String getQuotation_number() {
        return quotation_number;
    }

    public void setQuotation_number(String quotation_number) {
        this.quotation_number = quotation_number;
    }

    public String getSupplierId() {
        return SupplierId;
    }

    public void setSupplierId(String supplierId) {
        SupplierId = supplierId;
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
}
