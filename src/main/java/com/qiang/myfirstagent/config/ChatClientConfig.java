package com.qiang.myfirstagent.config;

import com.qiang.myfirstagent.tools.WeatherTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author qiang
 * @since 2026/10/10 19:32
 */
@Configuration
public class ChatClientConfig {
    @Value("${agent.system-prompt}")
    private String systemPrompt;

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder, WeatherTools weatherTools, ChatMemory chatMemory) {
        return builder
                .defaultSystem(systemPrompt)
                .defaultTools(weatherTools)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }
}
