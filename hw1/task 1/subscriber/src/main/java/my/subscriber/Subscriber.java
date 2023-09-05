package my.subscriber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Subscriber {
    private static final Logger LOGGER = LoggerFactory.getLogger(Subscriber.class);
    @JmsListener(destination = "task1_topic" )
    public void receiveMessage(String message) {
        LOGGER.info("I received a message: " + message);
    }
}
