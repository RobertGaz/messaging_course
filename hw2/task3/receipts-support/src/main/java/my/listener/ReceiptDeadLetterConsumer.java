package my.listener;

import my.model.Receipt;
import my.model.Region;
import my.repository.FailedReceiptsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class ReceiptDeadLetterConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReceiptDeadLetterConsumer.class);

    @Autowired
    private FailedReceiptsRepository failedReceiptsRepository;

    @RabbitListener(queues = "receipt-dead-letter-queue")
    public void receiveFromDLQ(Receipt receipt, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey) {
        LOGGER.info("(DLQ) new receipt in DLQ with routing key {}, persisting in DB...", routingKey);
        Region region = routingKey.startsWith("JP") ? Region.JAPAN : Region.USA;
        failedReceiptsRepository.persist(receipt, region);
    }
}
