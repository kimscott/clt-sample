package com.example.template.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisConfig {

    @Autowired
    private Environment environment;

//    @Bean
//    public LettuceConnectionFactory redisConnectionFactory() {
//        RedisStandaloneConfiguration redisConf = new RedisStandaloneConfiguration();
//        redisConf.setHostName(environment.getProperty("spring.redis.host"));
//        redisConf.setPort(Integer.parseInt(environment.getProperty("spring.redis.port")));
////        redisConf.setPassword(RedisPassword.of(environment.getProperty("spring.redis.password")));
//
//        return new LettuceConnectionFactory(redisConf);
//    }


    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisConf = new RedisStandaloneConfiguration();
        redisConf.setHostName(environment.getProperty("spring.redis.host"));
        redisConf.setPort(Integer.parseInt(environment.getProperty("spring.redis.port")));
        redisConf.setPassword(RedisPassword.of(environment.getProperty("spring.redis.password")));

        JedisConnectionFactory connectionFactory = new JedisConnectionFactory(redisConf);

        return connectionFactory;
    }

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(600))  // 케쉬데이터 저장 시간
                .disableCachingNullValues();        // 케쉬는 null 을 허용 안함
        return cacheConfig;
    }
    @Bean
    public RedisCacheManager cacheManager() {
        RedisCacheManager rcm = RedisCacheManager.builder(redisConnectionFactory())
                .cacheDefaults(cacheConfiguration())
                .transactionAware()
                .build();
        return rcm;
    }

    /**
     * redisTemplate 을 사용하에 redis 데이터를 직접 컨트롤을 할 수가 있다.
     * redis pub/sub 을 사용할적에 메세지를 보내기 위하느 Template 으로 쓰인다.
     */
    @Bean
    public RedisTemplate redisTemplate() {
        RedisTemplate redisTemplate = new RedisTemplate<>();

        // Serializer 를 등록하지 않으면 Default Serializer로 JdkSerializationRedisSerializer 를 사용하며
        // 이때 모든 값은 UniCode로 되어버린다
        // 출처: https://yonguri.tistory.com/entry/스프링부트-SpringBoot-개발환경-구성-3-Redis-설정 [Yorath's 블로그]
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        return redisTemplate;
    }

}
