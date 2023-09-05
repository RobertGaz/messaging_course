package my.publisher;

import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class Publisher {
    @Autowired
    JmsTemplate jmsTemplate;

    public void publish(String message) {
        jmsTemplate.convertAndSend(new ActiveMQTopic("VirtualTopic.task3_virtual_topic"), message);
    }
}
