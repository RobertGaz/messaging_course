package my.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class Receiver {
    public static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class);

    @JmsListener(destination = "task2_queue")
    public String receiveMessage(@Payload String messageText) {
        LOGGER.info("I received message: " + messageText + ", responding...");
        return "Hi from app B!"; // this will go to destination specified in ReplyTo header
    }
}
