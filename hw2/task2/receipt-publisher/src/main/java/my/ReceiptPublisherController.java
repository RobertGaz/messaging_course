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
        receiptPublisherService.publishToJapaneseBranch(new Receipt("order_123", "Myself"));
        receiptPublisherService.publishToUsaBranch(new Receipt("order_456", "Myself2"));

        return "OK";
    }

}