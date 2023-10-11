import my.KafkaStreamsConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.apache.kafka.streams.StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG;

public class StreamsTest {
    private KafkaStreamsConfig config;
    private StreamsBuilder streamsBuilder;

    private static Properties properties;
    @BeforeAll
    public static void setupProperties() {
        properties = new Properties();
        properties.setProperty(DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        properties.setProperty(DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
    }

    @BeforeEach
    public void setup() {
        config = new KafkaStreamsConfig();
        streamsBuilder = new StreamsBuilder();
        Map<String, KStream<Integer, String>> streamsOfWords = config.streamsOfWords(streamsBuilder);
        Map<String, KStream<Integer, String>> streamsOfWordsFiltered = config.streamsOfWordsFiltered(streamsOfWords);
        KStream<Integer, String> finalMergedStream = config.finalMergedStream(streamsOfWordsFiltered);
        finalMergedStream.to("output_test_topic", Produced.with(Serdes.Integer(), Serdes.String()));
    }

    @Test
    public void test() {

        List<KeyValue<Integer, String>> output;

        try (TopologyTestDriver driver = new TopologyTestDriver(streamsBuilder.build(), properties)) {
            TestInputTopic<String, String> inputTopic = driver.createInputTopic("task2",
                    Serdes.String().serializer(), Serdes.String().serializer());
            inputTopic.pipeInput("this is a random set of words which makes a sentence");

            TestOutputTopic<Integer, String> outputTopic = driver.createOutputTopic("output_test_topic",
                    Serdes.Integer().deserializer(), Serdes.String().deserializer());
            output = outputTopic.readKeyValuesToList();
        }

        Assertions.assertEquals(7, output.size());

        Assertions.assertTrue(output.contains(KeyValue.pair("this".length(), "this")));
        Assertions.assertTrue(output.contains(KeyValue.pair("is".length(), "is")));
        Assertions.assertTrue(output.contains(KeyValue.pair("set".length(), "set")));
        Assertions.assertTrue(output.contains(KeyValue.pair("of".length(), "of")));
        Assertions.assertTrue(output.contains(KeyValue.pair("words".length(), "words")));
        Assertions.assertTrue(output.contains(KeyValue.pair("which".length(), "which")));
        Assertions.assertTrue(output.contains(KeyValue.pair("sentence".length(), "sentence")));

        output.forEach(keyValue -> Assertions.assertFalse(keyValue.value.contains("a")));
    }
}
