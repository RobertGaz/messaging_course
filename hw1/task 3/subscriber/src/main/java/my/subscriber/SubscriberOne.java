package my.subscriber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class SubscriberOne {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubscriberOne.class);
    @JmsListener(destination = "Consumer.subscriber_1.VirtualTopic.task3_virtual_topic" )
    public void receiveMessage(String message) {
        LOGGER.info("I received a message: " + message);
    }
}
