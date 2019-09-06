package com.example.template;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class KafkaSender {

    public static void main(String[] args) {

        String topicName = "topic";

        send(topicName);
        sendSync(topicName);
        sendAsync(topicName);
    }

    public static void send(String topicName){
        // producer 생성
        KafkaProducer<String, String> producer = createKafkaProducer();

        // message 전달
        for (int i = 0; i < 5; i++) {
            String v = "hello"+i;
            producer.send(new ProducerRecord<String, String>(topicName, v));
        }

        // 종료
        producer.flush();
        producer.close();
    }

    /**
     * 리턴 값으로 Future를 반환하는데, get()을 호출하여 결과를 받아 메시지가 성공적으로 전송됬는지 체크한다.
     * 메인 스레드가 block되는 상황이 있지만, 신뢰성있는 메시지 전송을 보장한다.
     */
    public static void sendSync(String topicName) {
        long start = System.currentTimeMillis();

        // producer 생성
        KafkaProducer<String, String> producer = createKafkaProducer();

        try{
            RecordMetadata meta = producer.send(new ProducerRecord<String, String>(topicName, "Apache Kafka sendSync()")).get();
            System.out.println("Partition='"+meta.partition()+"' Offset='"+meta.offset()+"' ");
        }catch (Exception e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("sendSync - during time : "+ (end-start));
    }

    /**
     * 콜백방식으로 비동기 전송을 한다. 메시지 전송이 완료 혹은 실패되면,
     * 브로커쪽에서 콜백으로 onCompletion을 호출한다. 만약 실패하면 Exception 객체가 담긴다.
     * org.apache.kafka.clients.producer.Callback
     *
     * 로그를 보면 main이 아닌 별도의 카프카 스레드에서 콜백을 호출한다.
     *
     * 콜백을 받는 방식인데... 이상하게 정상 작동을 안한다.
     */
    public static void sendAsync(String topicName) {
        long start = System.currentTimeMillis();

        // producer 생성
        KafkaProducer<String, String> producer = createKafkaProducer();

        try{
            producer.send(new ProducerRecord<String, String>(topicName, "Apache Kafka sendAsync()"),new KafkaCallback());
        }catch (Exception e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("sendAsync() - during time : "+ (end-start));
    }


    public static KafkaProducer<String, String> createKafkaProducer() {

        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//        props.put(ProducerConfig.ACKS_CONFIG, "all"); // 자신이 보낸 메시지에 대해 카프카로부터 확인을 기다리지 않습니다.
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);
        return producer;

    }

    static class KafkaCallback implements Callback {

        @Override
        public void onCompletion(RecordMetadata metadata, Exception exception) {
            if(ObjectUtils.allNotNull(metadata)) {
                System.out.println("Partition='"+metadata.partition()+"' Offset='"+metadata.offset()+"' ");
            }else {
                System.err.println("KafkaCallback - Exception");
            }
        }

    }

}
