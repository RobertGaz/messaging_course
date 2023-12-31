package my.publisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublisherController {
    @Autowired
    private Publisher publisher;

    @GetMapping("/publish")
    public String publish() {
        publisher.publish("some message");
        return "OK";
    }
}
