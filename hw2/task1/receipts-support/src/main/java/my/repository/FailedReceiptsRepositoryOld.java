package my.repository;

import my.model.FailedReceiptRecord;
import my.model.Receipt;
import my.model.Region;
import my.model.Status;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Repository
public class FailedReceiptsRepositoryOld {

//    <receiptID: FailedReceiptRecord>
    private final Map<String, FailedReceiptRecord> storage = new HashMap<>();

    public void persist(Receipt receipt, Region region) {
        storage.put(receipt.getReceiptId(), new FailedReceiptRecord(receipt, region, Status.RECEIVED));
    }

    public List<FailedReceiptRecord> getAllRecords() {
        return storage.values().stream().toList();
    }

    public void updateRecord(FailedReceiptRecord record) {
        storage.put(record.getReceipt().getReceiptId(), record);
    }


}
