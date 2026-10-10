package com.qiang.myfirstagent.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

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

    @GetMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatStream(
            @RequestParam String message,
            @RequestParam(defaultValue = "default_user") String conversationId) {

        return chatClient.prompt()
                .user(message)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .stream() // 关键：把 call() 换成 stream()
                .content()
                .doOnNext(token -> {
                    // 关键：每个 token 到达时，立刻打印，不换行
                    System.out.print(token);
                })
                .doOnComplete(() -> {
                    // 流结束时换行
                    System.out.println("\n========== 流式响应结束 ==========");
                }); // 返回一个 Flux<String> 流
    }
}
