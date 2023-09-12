package my;

import my.model.Receipt;
import my.model.Region;
import my.repository.FailedReceiptsRepositoryOld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class FailedReceiptReceiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(FailedReceiptReceiver.class);

    @Autowired
    private FailedReceiptsRepositoryOld failedReceiptsRepositoryOld;

    @RabbitListener(queues = "failed-receipt-queue")
    public void receiveFailedReceipt(Receipt receipt,
                                     @Header(name = "region") Region region) {
        LOGGER.info("I received failed receipt {}", receipt);
        failedReceiptsRepositoryOld.persist(receipt, region);
    }




}
