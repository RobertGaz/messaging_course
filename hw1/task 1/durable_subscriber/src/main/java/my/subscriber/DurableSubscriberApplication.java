package my.subscriber;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DurableSubscriberApplication {

	public static void main(String[] args) {
		SpringApplication.run(DurableSubscriberApplication.class, args);
	}

}
