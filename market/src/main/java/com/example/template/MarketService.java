package com.example.template;

import com.example.template.sse.SseBaseMessageHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MarketService {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private SseBaseMessageHandler messageHandler;

    /**
     *
     */
    public Offers marketOffers(Long offerId){
        Offers offers =  new Offers();
        offers.setId(offerId);
        offers.setName("offers 가 왔습니다.");
        return offers;
    }

    @KafkaListener(topics = "${topic.topicName}", groupId = "OfferSimulated")
    public void onMessageSimulated(@Payload String message, ConsumerRecord<?, ?> consumerRecord) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OfferSimulated offerSimulated = null;
        try {
            offerSimulated = objectMapper.readValue(message, OfferSimulated.class);

            if( offerSimulated.getEventType().equals(OfferSimulated.class.getSimpleName())){

                System.out.println("OfferSimulated 이벤트 수신!!!!!!!!!");
                System.out.println(message);

                this.messageHandler.publish(offerSimulated.getName(), offerSimulated.getOfferId(), message);
            }
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "${topic.topicName}", groupId = "OfferSimulated")
    public void onMessageTaken(@Payload String message, ConsumerRecord<?, ?> consumerRecord) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OfferTaken offerTaken = null;
        try {
            offerTaken = objectMapper.readValue(message, OfferTaken.class);

            if( offerTaken.getEventType().equals(OfferTaken.class.getSimpleName())){

                System.out.println("OfferTaken 이벤트 수신!!!!!!!!!");
                System.out.println(message);
                this.messageHandler.publish(offerTaken.getName(), offerTaken.getOfferId(), message);
            }
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }
}
