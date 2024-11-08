package com;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import java.util.Properties;

public class KafkaProduce extends KafkaConnect {

    private static final Logger log = LoggerFactory.getLogger(KafkaProduce.class);
    private KafkaProducer<String, String> producer;

    public KafkaProduce() {
        log.info("Connect to Kafka Producer......");

        Properties prop = new Properties();
        prop.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_BROKERS);
        prop.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        prop.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        
        prop.setProperty(ProducerConfig.ACKS_CONFIG, "all");
        
        this.producer = new KafkaProducer<>(prop);
    }

    public void generateTopic(String topic, String key, String event) {
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, key, event);
        try {
            producer.send(producerRecord, (metadata, exception) -> {
                if (exception != null) {
                    log.error("Error sending message: key=>{} & event=>{}",key, event, exception);
                } else {
                    log.info("Message sent to topic: {} partition: {} offset: {} with key:{} ", 
                            metadata.topic(), 
                            metadata.partition(), 
                            metadata.offset(),
                            key
                            );
                }
            });
            producer.flush();
        } catch (Exception e) {
            log.error("Error sending message with key: {} and value: {}", key, event, e);
        }
    }

    public void closeProducer() {
        if (producer != null) {
            producer.close();
            log.info("KafkaProducer closed.");
        }
    }
}
