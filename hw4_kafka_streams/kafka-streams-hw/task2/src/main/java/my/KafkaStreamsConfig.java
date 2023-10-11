package my;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Branched;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Named;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;

import java.util.Arrays;
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


    // as I understood, task implies to use Map<String, KStream<Integer, String>> beans
    @Bean
    public Map<String, KStream<Integer, String>> streamsOfWords(StreamsBuilder streamsBuilder) {

        //transform sentences to words
        KStream<Integer, String> stream = streamsBuilder
                .<String, String> stream("task2")
                .peek((key, value) -> LOGGER.info("record [key: {}, value: {}] is being processed through task2 stream", key, value))
                .filter((key, value) -> value != null)
                .flatMapValues(value -> Arrays.asList(value.split("\\W+")))
                .selectKey((key, value) -> value.length())
                .peek((key, value) -> LOGGER.info("word [length: {}, value: {}] was extracted", key, value));

        Map<String, KStream<Integer, String>> streamsSplitted = stream.split(Named.as("words-"))
                .branch((key, value) -> key < 10, Branched.as("short"))
                .defaultBranch(Branched.as("long"));

        return streamsSplitted;
    }

    @Bean
    public Map<String, KStream<Integer, String>> streamsOfWordsFiltered(@Qualifier("streamsOfWords") Map<String, KStream<Integer, String>> streams) {
        KStream<Integer, String> streamOfShortWordsFiltered = streams.get("words-short").filter(((key, value) -> !value.contains("a")));
        KStream<Integer, String> streamOfLongWordsFiltered = streams.get("words-long").filter(((key, value) -> !value.contains("a")));

        return Map.of("words-short", streamOfShortWordsFiltered,
                "words-long", streamOfLongWordsFiltered);
    }

    @Bean
    public KStream<Integer, String> finalMergedStream(@Qualifier("streamsOfWordsFiltered") Map<String, KStream<Integer, String>> streams) {
        KStream<Integer, String> streamOfShortWordsFiltered = streams.get("words-short");
        KStream<Integer, String> streamOfLongWordsFiltered = streams.get("words-long");

        return streamOfShortWordsFiltered.merge(streamOfLongWordsFiltered).peek((key, value) ->
                LOGGER.info("[final merged stream] key: {}, value: {}", key, value)
        );
    }

}
