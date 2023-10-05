package my;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class Consumer {
    private static final long POLL_DURATION = 1000;
    private final Properties properties;

    public Consumer(String bootstrapServers) {
        properties = new Properties();
        properties.setProperty("bootstrap.servers", bootstrapServers);
        properties.setProperty("key.deserializer", StringDeserializer.class.getName());
        properties.setProperty("value.deserializer", StringDeserializer.class.getName());
        properties.setProperty("group.id", "task1-consumer-group");
        properties.setProperty("auto.offset.reset", "earliest");

        properties.setProperty("enable.auto.commit", "false");
    }

    public List<String> consume(String topic) {
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer(properties);

        kafkaConsumer.subscribe(Collections.singletonList(topic));
        ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(POLL_DURATION));

        // at most once consumer, so we commit offset right after receiving the records
        kafkaConsumer.commitAsync();
        kafkaConsumer.close();

        ArrayList<String> result = new ArrayList<>();
        records.forEach(record -> result.add(record.value()));

        return result;
    }
}
