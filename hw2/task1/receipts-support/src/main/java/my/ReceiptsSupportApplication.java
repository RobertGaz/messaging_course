package my;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

//@EnableJpaRepositories
@EnableRabbit
@SpringBootApplication
public class ReceiptsSupportApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReceiptsSupportApplication.class, args);
	}

	@Bean
	public MessageConverter messageConverter() {
		Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
		return converter;
	}

}
