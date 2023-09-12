package my;

import my.model.Receipt;
import my.model.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class ReceiptReceiver {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReceiptReceiver.class);

    @Autowired
    private ReceiptProcessor receiptProcessor;

    @RabbitListener(queues = "japan_office_queue")
    public void receiveReceiptInJapan(Receipt receipt,
                                      @Header(name = "processing_attempts") Long processingAttempts) {
        LOGGER.info("(japan back office) We received receipt: {} with processing_attempts {}", receipt, processingAttempts);
        receiptProcessor.processReceipt(receipt, Region.JAPAN, processingAttempts);
    }

    @RabbitListener(queues = "us_office_queue")
    public void receiveReceiptInUsa(Receipt receipt,
                                    @Header(name = "processing_attempts") Long processingAttempts) {
        LOGGER.info("(us back office) We received receipt: {} with processing_attempts {}", receipt, processingAttempts);
        receiptProcessor.processReceipt(receipt, Region.USA, processingAttempts);
    }

}
