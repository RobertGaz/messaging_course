package my;

import my.model.Receipt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReceiptPublisherController {

    @Autowired
    private ReceiptPublisherService receiptPublisherService;

    @GetMapping("/publishReceipt")
    public String publishReceipt() {
        Receipt receipt = new Receipt("order_123", "Myself");
        receiptPublisherService.publishToJapaneseBranch(receipt);
        receiptPublisherService.publishToUsaBranch(receipt);

        return "OK";
    }

}