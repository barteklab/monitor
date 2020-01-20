package messaging;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import trades.Trade;

import java.util.List;
import java.util.Properties;

@Slf4j
public class Publisher {
    private Producer<String, Trade> producer;
    private String topic;

    public Publisher(String servers, String topic) {
        this.topic = topic;
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaSerializer.class.getName());
        producer = new KafkaProducer<>(props);
    }

    public void publish(List<Trade> trades) {
        try {
            trades.stream()
                    .forEach(trade -> {
                        ProducerRecord producerRecord = new ProducerRecord(topic, trade.getTradeReference(), trade);
                        producer.send(producerRecord);
                        log.info("Message {} sent successfully", trade.getTradeReference());
                    });
        } finally {
            producer.close();
        }
    }
}
