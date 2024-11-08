package com;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.time.Duration;
import java.util.Properties;

public class KafkaCustom extends KafkaConnect {
    private static final Logger log = LoggerFactory.getLogger(KafkaCustom.class);
    private KafkaConsumer<String, String> consumer;
    String groupID;

    public KafkaCustom(String groupID,String topic) {
        log.info("Connect to Kafka Consumer......");
        this.groupID = groupID;
        Properties props = new Properties();
        props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_BROKERS);
        props.setProperty(ConsumerConfig.GROUP_ID_CONFIG, this.groupID);
        props.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        this.consumer = new KafkaConsumer<>(props);
        this.consumer.subscribe(Arrays.asList(topic));
    }

    public void consumeMessage() {
        try {
            while (true) {
                ConsumerRecords<String,String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    log.info("Consumed message: Key: {} Value: {} Partition: {} Offset: {}",
                            record.key(),
                            record.value(),
                            record.partition(), 
                            record.offset()
                            );
                }
            }
        } catch (Exception e) {
            log.error("Error consuming message", e);
        }
    }

    public void closeConsumer() {
        if (consumer != null) {
            consumer.close();
            log.info("KafkaConsumer closed.");
        }
    }
}
