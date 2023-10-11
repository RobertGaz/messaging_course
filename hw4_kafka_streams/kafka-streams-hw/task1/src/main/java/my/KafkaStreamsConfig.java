package my;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;

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
                DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName(),
                DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, WallclockTimestampExtractor.class.getName()
        ));
    }

    @Bean
    public KStream<String, String> stream(StreamsBuilder streamsBuilder) {
        KStream<String, String> stream = streamsBuilder
                .<String, String> stream("task1-1")
                .peek((key, value) -> LOGGER.info("record [key: {}, value: {}] is being processed through task1 stream", key, value));
        stream.to("task1-2");

        return stream;
    }
}
