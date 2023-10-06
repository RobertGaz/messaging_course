package my.taxiapp.config;

import my.taxiapp.model.VehicleCoordinates;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, VehicleCoordinates> consumerFactory() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("bootstrap.servers", "localhost:29092");
        properties.put("group.id", "taxi-destinations-service-group");
        properties.put("auto.offset.reset", "earliest");

        return new DefaultKafkaConsumerFactory<>(properties, new StringDeserializer(), new JsonDeserializer<>(VehicleCoordinates.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, VehicleCoordinates> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, VehicleCoordinates> containerFactory = new ConcurrentKafkaListenerContainerFactory<>();
        containerFactory.setConsumerFactory(consumerFactory());

        return containerFactory;
    }
}
