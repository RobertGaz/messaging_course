package my;

import my.model.Receipt;
import my.model.Region;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FailedReceiptSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void dispatchFailedReceipt(Receipt receipt, Region region) {
        rabbitTemplate.convertAndSend("failed-receipt-exchange", "failed.receipts", receipt, m -> {
            m.getMessageProperties().getHeaders().put("region", region.name());
            return m;
        });
    }

}
