package my;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;

public class MySerializer implements Serializer<Person> {
    @Override
    public byte[] serialize(String topic, Person person) {
        try {
            return new ObjectMapper().writeValueAsBytes(person);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
