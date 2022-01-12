package com.tobdev.qywxinner.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
//    @Bean
//    public RedisTemplate<String,String> redisTemplate(RedisConnectionFactory factory){
//        RedisTemplate<String ,String > redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(factory);
//        return  redisTemplate;
//    }
    @Bean
    public RedisTemplate<String,Object> RedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String ,Object > template = new RedisTemplate<>();
        // 配置连接工厂
        template.setConnectionFactory(factory);

        //key序列化
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // key采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // hash的key也采用String的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);

        //值序列化
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
    //        ObjectMapper om = new ObjectMapper();
    //        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    //        //om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
    //        jackson2JsonRedisSerializer.setObjectMapper(om);

        // value序列化方式采用jackson
        template.setValueSerializer(jackson2JsonRedisSerializer);
        // hash的value序列化方式采用jackson
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return  template;
    }



}
