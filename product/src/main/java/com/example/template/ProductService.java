package com.example.template;

import com.example.template.event.SlotOffered;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProductService {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${topic.topicName}")
    String topicName;

    // url 관리를 properties 파일에서 하지 않고, 별도의 api 관리서버가 있다면 해당 서버에서 조회하는 것도 좋은 방법입니다.
    @Value("${inventoryUrl}")
    String inventoryUrl;

    @Value("${financeUrl}")
    String financeUrl;


    public void slotOffer(String username, Long offerId){

        String inventoryUrl = "/inventories";
        String financeUrl = "/budgets";

        // 1. checkInventory
        ResponseEntity<String> inventoryEntity = restTemplate.getForEntity(inventoryUrl, String.class);
        System.out.println(inventoryEntity.getStatusCode());
        System.out.println(inventoryEntity.getBody());

        // 2. Check budget
        ResponseEntity<String> financeEntity = restTemplate.getForEntity(financeUrl, String.class);
        System.out.println(financeEntity.getStatusCode());
        System.out.println(financeEntity.getBody());

        // 3. send kafka

        SlotOffered slotOffered = new SlotOffered();
        slotOffered.setEventType(SlotOffered.class.getSimpleName());
        slotOffered.setName(username);
        slotOffered.setOfferId(offerId);
        this.sendTopic(topicName, slotOffered);
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

