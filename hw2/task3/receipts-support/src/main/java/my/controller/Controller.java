package my.controller;

import my.model.Region;
import my.repository.FailedReceiptsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private FailedReceiptsRepository failedReceiptsRepository;

    @GetMapping("/resendAll")
    public String resendAllFailedReceipts() {
        failedReceiptsRepository.getAllRecords().forEach(record -> {
            LOGGER.info("Resending Receipt {} back...", record.getReceipt().getReceiptId());
            String routingKey = record.getRegion() == Region.JAPAN ? "JP.BACK.OFFICE" : "US.BACK.OFFICE";
            rabbitTemplate.convertAndSend("receipt-topic-exchange", routingKey, record.getReceipt(),
                    message -> {
                        message.getMessageProperties().setHeader("processing_attempts", 0);
                        return message;
                    });
        });

        return "OK";
    }

    @GetMapping("/showAllFailed")
    public String showAllFailedReceipts() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Failed Receipts Ids:\n");
        failedReceiptsRepository.getAllRecords().forEach(record -> {
            stringBuilder.append(record.getReceipt().getReceiptId());
            stringBuilder.append(".   ");
        });

        return stringBuilder.toString();
    }

}
