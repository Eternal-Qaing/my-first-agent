package com.qiang.myfirstagent.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author qiang
 * @since 2026/10/10 19:23
 */
@RestController
@RequiredArgsConstructor
public class ChatController {
    private final ChatClient chatClient;

    @GetMapping("/chat")
    public String chat(@RequestParam String message,
                       @RequestParam(defaultValue = "default_user") String conversationId){
        return chatClient.prompt()
                .user(message)
                .advisors(a->a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .content();
    }
}
