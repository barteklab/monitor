package messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

@Slf4j
public class KafkaSerializer implements Serializer {
    @Override
    public void configure(Map configs, boolean isKey) {

    }

    @Override
    public byte[] serialize(String s, Object object) {
        byte[] bytes = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            bytes = objectMapper.writeValueAsString(object).getBytes();
        } catch (Exception e) {
            log.error("Issue with serialization of {}", object);
            e.printStackTrace();
        }
        return bytes;
    }

    @Override
    public void close() {

    }
}
