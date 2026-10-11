package com.qiang.myfirstagent.config;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.redis.RedisChatMemoryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.JedisClientConfig;
import redis.clients.jedis.RedisClient;

/**
 *
 * @author qiang
 * @since 2026/10/10 21:55
 */
@Configuration
public class ChatMemoryConfig {
    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.password:}") // 默认为空，适配无密码场景
    private String redisPassword;

    @Bean
    public ChatMemory chatMemory() {
        JedisClientConfig clientConfig;
        if (redisPassword != null && !redisPassword.isEmpty()) {
            clientConfig = DefaultJedisClientConfig.builder()
                    .password(redisPassword).build();
        }else {
            clientConfig = DefaultJedisClientConfig.builder().build();
        }

        RedisClient jedisClient = RedisClient.builder()
                .hostAndPort(redisHost, redisPort)
                .clientConfig(clientConfig)
                .build();


        RedisChatMemoryRepository redisRepo = RedisChatMemoryRepository.builder()
                .jedisClient(jedisClient)
                .initializeSchema(true)
                .build();
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(redisRepo)
                .maxMessages(10)
                .build();

    }

}
