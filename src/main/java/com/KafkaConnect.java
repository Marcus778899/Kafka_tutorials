package com;
import io.github.cdimascio.dotenv.Dotenv;

public abstract class KafkaConnect {
    String KAFKA_BROKERS;

    public KafkaConnect(){
        Dotenv env = Dotenv.load();
        this.KAFKA_BROKERS = env.get("KAFKA_BROKERS");
    }
}
