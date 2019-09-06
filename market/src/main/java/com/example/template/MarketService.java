package com.example.template;

import com.example.template.sse.SseBaseMessageHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Service
public class MarketService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MarketService.class);

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private SseBaseMessageHandler messageHandler;

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * @Cacheable 은 key 에 해당하는 cache 값이 있으면 cache 를 조회하고,
     * key 에 해당하는 cache 값이 없으면 메서드 안쪽의 내용을 실행하여 결과값을 key 에 저장하는 역할을 한다.
     */
    @Cacheable(value = "app", key = "#name")
    public Offers getOffers(String name){
        LOGGER.info("find name from jdbc {}", name);
        // TODO DB 조회

        Offers offers =  new Offers();
        offers.setId(1L);
        offers.setName(name);
        return offers;

    }

    /**
     * @CachePut cache 에 저장을 한다.
     * 아래 주석처럼 @Caching(put 으로도 사용이 가능하다.
     */
    @CachePut(value = "app", key = "#offers.name")
//    @Caching(put = {
//            @CachePut(value = "app", key="#offers.name")
//    })
    @Transactional
    public Offers saveCache(Offers offers) {
//        return repository.save(Offers);

        return offers;
    }

    /**
     * 관련된 캐쉬를 지운다,
     */
    @Caching(evict = {
            @CacheEvict(value = "app"),
            @CacheEvict(value = "app", key="#offers.name")
    })
    public String deleteCacheList(Offers offers) {
        return "";
    }

    /**
     * redisTemplate.opsForValue() 를 사용하여 key 에 대한 값을 조회할 수 있다.
     * 조회는 list, set, hash, value 등이 있다.
     */
    public Offers getOffersWithoutAnnotation(String name){
        String key = "app2::"+name;
        Offers value = (Offers)redisTemplate.opsForValue().get(key);
        return value;
    }

    /**
     * 레디스를 사용하는 방법중에 데이터 저장 공간으로 활용을 할 수가 있다.
     * 이렇게 저장된 값은 시간이 지나도 지워지지 않는다.
     * 스프링 어노테이션을 쓰지 않고, redisTemplate 을 사용하여 직접 사용하는 예쩨
     */
    public Offers saveCacheWithoutAnnotation(Offers offers){
        String key = "app2::"+offers.getName();
        redisTemplate.opsForValue().set(key, offers);
        return offers;
    }



    /**
     * redisTemplate.delete 를 사용하여 데이터를 지울수 있다
     * Set<String> keys = redisTemplate.keys(key); 를 통하여 여러 키를 조회하여 한번에 지울수도 있다.
     */
    public String deketeKeyByRedis(String name){
        String key = "app2::"+name;
        redisTemplate.delete(key);

        // delete multi keys
//        String key = "app2::*"+name+"*";
//        Set<String> keys = redisTemplate.keys(key);
//        redisTemplate.delete(keys);

        return "ok";
    }


    /**
     * 여러개의 key 를 조회하여 list 로 만들어 데이터를 조회하는 예제
     */
    public List<Offers> getListByRedis(String userName){

        // like 조회
        String key = "app2::*"+userName+"*";
        Set<String> keys = redisTemplate.keys(key);
        List<Offers> value = (List<Offers>)redisTemplate.opsForValue().multiGet(keys);

        return value;
    }

    /**
     *
     */
    public Offers marketOffers(Long offerId){
        Offers offers =  new Offers();
        offers.setId(offerId);
        offers.setName("offers 가 왔습니다.");
        return offers;
    }

    @KafkaListener(topics = "${topic.topicName}", groupId = "offerSimulated")
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

    @KafkaListener(topics = "${topic.topicName}", groupId = "offerTaken")
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
