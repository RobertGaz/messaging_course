package my.app;

import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

@Component
public class Sender {
    public static final Logger LOGGER = LoggerFactory.getLogger(Sender.class);
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private MessageConverter messageConverter;
    @Autowired
    private ConnectionFactory connectionFactory;

    public void send() {
        Message response = jmsTemplate.sendAndReceive(
                "task2_queue",
                session -> messageConverter.toMessage("Hello from app A!", session) // MessageCreator
        );
        try {
            LOGGER.info("I sent message and received response: " + messageConverter.fromMessage(response));
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

}
