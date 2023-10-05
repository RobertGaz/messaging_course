
import my.Consumer;
import my.Producer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.util.List;

@Testcontainers
public class IntegrationTest {

    @ClassRule
    @Container
    public static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));

    private Producer producer;
    private Consumer consumer;

    @Before
    public void setup() {
        String bootstrapServers = kafka.getBootstrapServers();
        producer = new Producer(bootstrapServers);
        consumer = new Consumer(bootstrapServers);
    }

    @Test
    public void test() throws IOException, InterruptedException {
        final String topic = "task_1_topic";
        final String key = "some key";
        final String message = "some message";

        //create topic in kafka in container
        kafka.execInContainer("/bin/sh", "-c", "kafka-topics --bootstrap-server localhost:9092 --topic " + topic + " --create");

        producer.send(key, message, topic);
        List<String> messages = consumer.consume(topic);

        System.out.println("Received messages from consumer: " + messages);

        Assert.assertEquals(1, messages.size());
        Assert.assertEquals(message, messages.get(0));
    }
}
