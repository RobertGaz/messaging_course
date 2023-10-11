import my.KafkaStreamsConfig;
import my.Person;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TopologyTestDriver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class DeserializationTest {

    private KafkaStreamsConfig config;

    @BeforeEach
    public void setup() {
        config = new KafkaStreamsConfig();
    }

    @Test
    public void testDeserialization() {
        StreamsBuilder streamsBuilder = new StreamsBuilder();

        List<Person> personRepository = new ArrayList<>();
        config.stream(streamsBuilder, personRepository);

        try (TopologyTestDriver driver = new TopologyTestDriver(streamsBuilder.build())) {
            TestInputTopic<String, String> inputTopic = driver.createInputTopic("task4",
                    Serdes.String().serializer(), Serdes.String().serializer());
            inputTopic.pipeInput("test_key", "{ \"name\":\"John\", \"company\":\"EPAM\", \"position\":\"developer\", \"experience\":5 }");
        }

        Person person = new Person("John", "EPAM", "developer", 5);
        Assertions.assertEquals(1, personRepository.size());
        Assertions.assertEquals(person, personRepository.get(0));
    }
}
