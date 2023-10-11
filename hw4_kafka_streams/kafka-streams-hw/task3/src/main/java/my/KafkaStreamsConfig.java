package my;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;

import java.time.Duration;
import java.util.Map;

import static org.apache.kafka.streams.StreamsConfig.*;

@Configuration
@EnableKafka
@EnableKafkaStreams
public class KafkaStreamsConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaStreamsConfig.class);

    @Bean(name= KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration kafkaStreamsConfiguration() {
        return new KafkaStreamsConfiguration(Map.of(
                APPLICATION_ID_CONFIG, "kafka-streams-hw",
                BOOTSTRAP_SERVERS_CONFIG, "localhost:9092",
                DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName(),
                DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName()
        ));
    }

    @Bean
    public KStream<String, String> transformedStream1(StreamsBuilder streamsBuilder) {
        return defineStreamWithTransformedRecords("task3-1", streamsBuilder);
    }

    @Bean
    public KStream<String, String> transformedStream2(StreamsBuilder streamsBuilder) {
        return defineStreamWithTransformedRecords("task3-2", streamsBuilder);
    }

    private KStream<String, String> defineStreamWithTransformedRecords(String topicName, StreamsBuilder streamsBuilder) {
        return streamsBuilder
                .<String, String> stream(topicName)
                .filter((key, value) -> value != null && value.contains(":"))
                .peek((key, value) -> LOGGER.info("[income record] key: {}, value: {}", key, value))
                .map((key, value) -> KeyValue.pair(value.split(":")[0], value.split(":")[1]))
                .peek((key, value) -> LOGGER.info("[transformed record] key: {}, value: {}", key, value));
    }

    @Bean
    public KStream<String, String> joinedStream(KStream<String, String> transformedStream1,
                                                KStream<String, String> transformedStream2) {
        return transformedStream1.join(transformedStream2, (value1, value2) -> value1 + " # " + value2,
                JoinWindows.ofTimeDifferenceAndGrace(Duration.ofMinutes(1), Duration.ofSeconds(30)))
                .peek((key, value) -> LOGGER.info("[joined record] key: {}, value: {}", key, value));
    }
}
