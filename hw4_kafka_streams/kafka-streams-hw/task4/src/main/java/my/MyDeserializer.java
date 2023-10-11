package my;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;

public class MyDeserializer implements Deserializer<Person> {
    @Override
    public Person deserialize(String topic, byte[] data) {
        try {
            return new ObjectMapper().readValue(data, Person.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
