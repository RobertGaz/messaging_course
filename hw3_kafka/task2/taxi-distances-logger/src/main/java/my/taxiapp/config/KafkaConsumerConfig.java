//package my.taxiapp.config;
//
//import org.apache.kafka.common.serialization.DoubleDeserializer;
//import org.apache.kafka.common.serialization.StringDeserializer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
//import org.springframework.kafka.core.ConsumerFactory;
//import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//public class KafkaConsumerConfig {
//
//    @Bean
//    public ConsumerFactory<String, Double> consumerFactory() {
//        Map<String, Object> properties = new HashMap<>();
//        properties.put("bootstrap.servers", "localhost:29092");
//        properties.put("group.id", "taxi-destinations-logger-group");
//        properties.put("auto.offset.reset", "earliest");
//        properties.put("key.deserializer", StringDeserializer.class);
//        properties.put("value.deserializer", DoubleDeserializer.class);
//
//        return new DefaultKafkaConsumerFactory<>(properties);
//    }
//
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, Double> kafkaListenerContainerFactory() {
//        ConcurrentKafkaListenerContainerFactory<String, Double> containerFactory = new ConcurrentKafkaListenerContainerFactory<>();
//        containerFactory.setConsumerFactory(consumerFactory());
//
//        return containerFactory;
//    }
//}
