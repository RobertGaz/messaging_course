package my.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class InfrastructureConfig {

    @Bean
    public Queue japanOfficeQueue() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-message-ttl", 200);
        arguments.put("x-max-length", 3);
        arguments.put("x-dead-letter-exchange", "receipt-dead-letter-exchange");
        return new Queue("japan_office_queue", true, false, false, arguments);
    }

    @Bean
    public Queue usOfficeQueue() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-message-ttl", 1000);
        arguments.put("x-max-length", 3);
        arguments.put("x-dead-letter-exchange", "receipt-dead-letter-exchange");
        return new Queue("us_office_queue", true, false, false, arguments);
    }

    @Bean
    public Queue receiptRetryQueue() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-message-ttl", 4000);
        arguments.put("x-dead-letter-exchange", "receipt-topic-exchange");
        return new Queue("receipt-retry-queue", true, false, false, arguments);
    }

    @Bean
    public Queue failedReceiptQueue() {
        return new Queue("failed-receipt-queue");
    }

    @Bean
    public Queue receiptDeadLetterQueue() {
        return new Queue("receipt-dead-letter-queue");
    }

    @Bean
    public TopicExchange receiptTopicExchange() {
        return new TopicExchange("receipt-topic-exchange");
    }

    @Bean
    public FanoutExchange receiptRetryExchange() {
        return new FanoutExchange("receipt-retry-exchange");
    }

    @Bean
    public DirectExchange failedReceiptExchange() {
        return new DirectExchange("failed-receipt-exchange");
    }

    @Bean
    public FanoutExchange receiptDeadLetterExchange() {
        return new FanoutExchange("receipt-dead-letter-exchange");
    }

    @Bean
    public Binding binding1() {
        return BindingBuilder.bind(japanOfficeQueue()).to(receiptTopicExchange()).with("JP.#");
    }

    @Bean
    public Binding binding2() {
        return BindingBuilder.bind(japanOfficeQueue()).to(receiptTopicExchange()).with("jp.#");
    }

    @Bean
    public Binding binding3() {
        return BindingBuilder.bind(usOfficeQueue()).to(receiptTopicExchange()).with("US.#");
    }

    @Bean
    public Binding binding4() {
        return BindingBuilder.bind(usOfficeQueue()).to(receiptTopicExchange()).with("us.#");
    }

    @Bean
    public Binding binding5() {
        return BindingBuilder.bind(failedReceiptQueue()).to(failedReceiptExchange()).with("failed.receipts");
    }

    @Bean
    public Binding binding6() {
        return BindingBuilder.bind(receiptRetryQueue()).to(receiptRetryExchange());
    }

    @Bean
    public Binding binding7() {
        return BindingBuilder.bind(receiptDeadLetterQueue()).to(receiptDeadLetterExchange());
    }

}
