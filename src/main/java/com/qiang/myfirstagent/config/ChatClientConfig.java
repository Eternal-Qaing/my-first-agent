package com.qiang.myfirstagent.config;

import com.qiang.myfirstagent.tools.WeatherTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author qiang
 * @since 2026/10/10 19:32
 */
@Configuration
public class ChatClientConfig {
    @Bean
    public ChatClient chatClient(ChatClient.Builder builder, WeatherTools weatherTools) {
        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .maxMessages(10)
                .build();
        return builder
                .defaultSystem("你是一个严谨的助手。当用户询问任何天气相关的问题时，**绝对不能自己编造数据**，必须调用 getWeather 工具获取真实结果。如果工具没有返回数据，就如实告诉用户不知道。")
                .defaultTools(weatherTools)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }
}
