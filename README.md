# Kafka

## install kafka in docker with 3 containers

docker-compose.yaml

```YAML
version: '3'
services:
  zookeeper:
    image: zookeeper:3.8
    # to survive the container restart
    tmpfs: "/zktmp"
    environment:
      ALLOW_ANONYMOUS_LOGIN: 'yes'
    ports:
      - "2181:2181"

  kafka1:
    image: bitnami/kafka:3.8
    depends_on:
      - zookeeper
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_CFG_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_CFG_LISTENERS: INTERNAL://:9092,EXTERNAL://0.0.0.0:29092
      KAFKA_CFG_ADVERTISED_LISTENERS: INTERNAL://kafka1:9092,EXTERNAL://localhost:29092
      KAFKA_CFG_LISTENER_SECURITY_PROTOCOL_MAP: INTERNAL:PLAINTEXT,EXTERNAL:PLAINTEXT
      KAFKA_CFG_INTER_BROKER_LISTENER_NAME: INTERNAL
      # optional - enable topic auto create
      KAFKA_CFG_AUTO_CREATE_TOPICS_ENABLE: 'true'
      ALLOW_PLAINTEXT_LISTENER: 'yes'
    ports:
      - "9092:9092"
      - "29092:29092"
    volumes:
      - kafka_data1:/bitnami/kafka

  kafka2:
    image: bitnami/kafka:3.8
    depends_on:
      - zookeeper
    environment:
      KAFKA_BROKER_ID: 2
      KAFKA_CFG_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_CFG_LISTENERS: INTERNAL://:9093,EXTERNAL://0.0.0.0:29093
      KAFKA_CFG_ADVERTISED_LISTENERS: INTERNAL://kafka2:9093,EXTERNAL://localhost:29093
      KAFKA_CFG_LISTENER_SECURITY_PROTOCOL_MAP: INTERNAL:PLAINTEXT,EXTERNAL:PLAINTEXT
      KAFKA_CFG_INTER_BROKER_LISTENER_NAME: INTERNAL
      # optional - enable topic auto create
      KAFKA_CFG_AUTO_CREATE_TOPICS_ENABLE: 'true'
      ALLOW_PLAINTEXT_LISTENER: 'yes'
    ports:
      - "9093:9093"
      - "29093:29093"
    volumes:
      - kafka_data2:/bitnami/kafka

  kafka3:
    image: bitnami/kafka:3.8
    depends_on:
      - zookeeper
    environment:
      KAFKA_BROKER_ID: 3
      KAFKA_CFG_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_CFG_LISTENERS: INTERNAL://:9094,EXTERNAL://0.0.0.0:29094
      KAFKA_CFG_ADVERTISED_LISTENERS: INTERNAL://kafka3:9094,EXTERNAL://localhost:29094
      KAFKA_CFG_LISTENER_SECURITY_PROTOCOL_MAP: INTERNAL:PLAINTEXT,EXTERNAL:PLAINTEXT
      KAFKA_CFG_INTER_BROKER_LISTENER_NAME: INTERNAL
      # optional - enable topic auto create
      KAFKA_CFG_AUTO_CREATE_TOPICS_ENABLE: 'true'
      ALLOW_PLAINTEXT_LISTENER: 'yes'
    ports:
      - "9094:9094"
      - "29094:29094"
    volumes:
      - kafka_data3:/bitnami/kafka

  kafka-ui:
    image: provectuslabs/kafka-ui:latest
    depends_on:
      - kafka1
      - kafka2
      - kafka3
    ports:
      - "8080:8080"
    environment:
      KAFKA_CLUSTERS_0_NAME: local
      KAFKA_CLUSTERS_0_BOOTSTRAPSERVERS: kafka1:9092,kafka2:9093,kafka3:9094
      KAFKA_CLUSTERS_0_ZOOKEEPER: zookeeper:2181

volumes:
  kafka_data1:
    driver: local
  kafka_data2:
    driver: local
  kafka_data3:
    driver: local
```

![alt text](./image/kafka-ui.png)

## Quick start

