package stream;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import io.github.cdimascio.dotenv.Dotenv;
import java.util.Arrays;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;

public class LineSplit {
    public static void main(String[] args) {
        Dotenv env = Dotenv.load();
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "LineSpilt_app");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, env.get("KAFKA_BROKERS"));
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        final StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> source = builder.stream("first-topic");
        
        source.flatMapValues(value -> Arrays.asList(value.split("\\W+"))).to("testing3");
        
        final Topology topology = builder.build();
        final KafkaStreams streams = new KafkaStreams(topology, props);
        final CountDownLatch latch = new CountDownLatch(1);

        // attach shutdown handler to catch control-c
        Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
            @Override
            public void run() {
                streams.close();
                latch.countDown();
            }
        });
        try {
            streams.start();
            latch.await();
        } catch (final Throwable e) {
            System.exit(1);
        }
    }
}
