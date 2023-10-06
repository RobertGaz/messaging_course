package my.taxiapp.config;

import org.apache.kafka.common.serialization.DoubleSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {
    @Bean
    public ProducerFactory<String, Double> producerFactory() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("bootstrap.servers", "localhost:29092");
        properties.put("key.serializer", StringSerializer.class);
        properties.put("value.serializer", DoubleSerializer.class);

        return new DefaultKafkaProducerFactory<>(properties);
    }

    @Bean
    public KafkaTemplate<String, Double> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
