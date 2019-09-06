package com.example.template;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;

import static java.time.Duration.ofSeconds;
import static java.util.Collections.singleton;

public class KafkaReceiver {

    public static void main(String[] args) {
        consume();
    }


    public static void consume(){

        String topicName = "topic";
        KafkaConsumer<String, String> consumer = createKafkaConsumer(topicName);

        while (true) {
            int second = 5;
            ConsumerRecords<String, String> records = consumer.poll(ofSeconds(second));

            Map<TopicPartition, OffsetAndMetadata> offsetsToCommit = new HashMap<>();

            for (TopicPartition partition : records.partitions()) {
                List<ConsumerRecord<String, String>> partitionedRecords = records.records(partition);

                for(ConsumerRecord<String, String> record : partitionedRecords){
                    System.out.println("offset='"+record.offset()+"' value='"+record.value()+"' ");
                }
            }

            System.out.println("===================== {"+second+"} 초 여백 ==================== ");
        }
    }
    private static KafkaConsumer<String, String> createKafkaConsumer(String topicName) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "foo");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(singleton(topicName));
        return consumer;
    }


}
