package com.example.redis.config;

import com.alibaba.fastjson.parser.ParserConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();

        //序列化
        FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        redisTemplate.setKeySerializer(stringRedisSerializer);// 设置key采用String的序列化方式
        redisTemplate.setHashKeySerializer(stringRedisSerializer);// 设置hash的key也采用String的序列化方式

        redisTemplate.setValueSerializer(fastJsonRedisSerializer); // 设置value采用的fastjson的序列化方式
        redisTemplate.setHashValueSerializer(fastJsonRedisSerializer);// 设置hash的value采用的fastjson的序列化方式

        // 全局开启AutoType，这里方便开发，使用全局的方式
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        // 建议使用这种方式，小范围指定白名单
         ParserConfig.getGlobalInstance().addAccept("com.example.redis.pojo");


        redisTemplate.setDefaultSerializer(fastJsonRedisSerializer);// 设置其他默认的序列化方式为fastjson

        redisTemplate.setConnectionFactory(factory);

        return redisTemplate;
    }
}
