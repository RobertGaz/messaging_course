package my.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class InfrastructureConfig {

    @Bean
    public Queue japanOfficeQueue() {
        return new Queue("japan_office_queue");
    }

    @Bean
    public Queue usOfficeQueue() {
        return new Queue("us_office_queue");
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

}
