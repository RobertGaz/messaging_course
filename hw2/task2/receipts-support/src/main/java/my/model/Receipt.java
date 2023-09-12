package my.model;

import java.util.UUID;

public class Receipt {
    private String receiptId;
    private String orderId;
    private String customerName;

    public Receipt() {
    }

    public Receipt(String orderId, String customerName) {
        this.receiptId = UUID.randomUUID().toString();
        this.orderId = orderId;
        this.customerName = customerName;
    }

    public String getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @Override
    public String toString() {
        return "Receipt{" +
                "receiptId='" + receiptId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", customerName='" + customerName + '\'' +
                '}';
    }
}
