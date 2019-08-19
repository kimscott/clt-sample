package com.example.template.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EventListener {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Value("${topic.topicName}")
    String topicName;

    @KafkaListener(topics = "${topic.topicName}")
    public void onMessage(@Payload String message, ConsumerRecord<?, ?> consumerRecord) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        SlotOffered slotOffered = null;
        try {
            slotOffered = objectMapper.readValue(message, SlotOffered.class);

            if( slotOffered.getEventType().equals(SlotOffered.class.getSimpleName())){
                // slotOffered 라는 이벤트가 왔을때, offerTaken ,offerSimulated 이벤트 발송

                OfferTaken offerTaken = new OfferTaken();
                offerTaken.setEventType(OfferTaken.class.getSimpleName());
                offerTaken.setName("OfferTaken");
                this.sendTopic(topicName, offerTaken);

                OfferSimulated offerSimulated = new OfferSimulated();
                offerSimulated.setEventType(OfferSimulated.class.getSimpleName());
                offerSimulated.setName("OfferSimulated");
                this.sendTopic(topicName, offerSimulated);
            }
        } catch (
        IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 이벤트 발송
     */
    public void sendTopic(String topicName, Object object){
        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;
        try {
            json = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON format exception", e);
        }

        System.out.println("===== pub Event ====");
        System.out.println(json);
        ProducerRecord producerRecord = new ProducerRecord<>(topicName, json);
        kafkaTemplate.send(producerRecord);
    }
}
