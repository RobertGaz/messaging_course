package my;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.errors.LogAndContinueExceptionHandler;
import org.apache.kafka.streams.kstream.Consumed;
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

import java.util.ArrayList;
import java.util.List;
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
                DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, WallclockTimestampExtractor.class.getName(),
                DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG, LogAndContinueExceptionHandler.class.getName()
        ));
    }

    @Bean
    public List<Person> personRepository() {
        return new ArrayList<>();
    }

    @Bean
    public KStream<String, Person> stream(StreamsBuilder streamsBuilder, List<Person> personRepository) {

        Serde<Person> mySerde = Serdes.serdeFrom(new MySerializer(), new MyDeserializer());

        KStream<String, Person> stream = streamsBuilder
                .stream("task4", Consumed.with(Serdes.String(), mySerde))
                .filter((key, value) -> value != null)
                .peek((key, value) -> LOGGER.info("received record [key: {}, value: {}] from task4 topic", key, value))
                .peek((key, value) -> personRepository.add(value)); // <- for unit test

        return stream;
    }
}
