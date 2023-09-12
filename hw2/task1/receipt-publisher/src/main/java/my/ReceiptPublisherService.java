package my;

import my.model.Receipt;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReceiptPublisherService {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void publishToUsaBranch(Receipt receipt) {
        rabbitTemplate.convertAndSend("receipt-topic-exchange", "US.BACK.OFFICE", receipt);
    }

    public void publishToJapaneseBranch(Receipt receipt) {
        rabbitTemplate.convertAndSend("receipt-topic-exchange", "JP.BACK.OFFICE", receipt);
    }

}
