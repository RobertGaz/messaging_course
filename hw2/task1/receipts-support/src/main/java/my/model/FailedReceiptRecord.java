package my.model;

import java.util.Date;
import java.util.UUID;

public class FailedReceiptRecord {
    private Receipt receipt;
    private Date receivedDate;
    private Status status;
    private Region region;

    public FailedReceiptRecord(Receipt receipt, Region region, Status status) {
        this.receipt = receipt;
        this.receivedDate = new Date();
        this.status = status;
        this.region = region;
    }

    public Receipt getReceipt() {
        return receipt;
    }

    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    @Override
    public String toString() {
        return "FailedReceiptRecord{" +
                ", receipt=" + receipt +
                ", receivedDate=" + receivedDate +
                ", status=" + status +
                ", region=" + region +
                '}';
    }
}
