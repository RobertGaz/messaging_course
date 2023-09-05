package my.subscriber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class SubscriberTwo {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubscriberTwo.class);
    @JmsListener(destination = "Consumer.subscriber_2.VirtualTopic.task3_virtual_topic" )
    public void receiveMessage(String message) {
        LOGGER.info("I received a message: " + message);
    }
}
