package my.publisher;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class Publisher {
    @Autowired
    JmsTemplate jmsTemplate;

    public void publish(String message) {
        jmsTemplate.convertAndSend("task1_topic", message);
    }
}
