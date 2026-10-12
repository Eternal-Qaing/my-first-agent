package com.qiang.myfirstagent.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {
    private final ChatClient chatClient;

    @GetMapping("/chat")
    public String chat(
            @RequestParam
            @NotBlank(message = "消息内容不能为空")
            @Size(max = 2000, message = "消息内容过长，请控制在 2000 字以内")
            String message,
            @RequestParam(defaultValue = "default_user")
            @Size(max = 64, message = "绘画 ID 长度不能超过 64 位")
            String conversationId){
        log.info("收到聊天请求 | conversationId={} | messageLength={}", conversationId, message.length());
        return chatClient.prompt()
                .user(message)
                .advisors(a->a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .content();
    }

    @GetMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatStream(
            @RequestParam
            @NotBlank(message = "消息内容不能为空")
            @Size(max = 2000, message = "消息内容过长，请控制在 2000 字以内")
            String message,

            @RequestParam(defaultValue = "default_user")
            @Size(max = 64, message = "会话 ID 长度不能超过 64 位")
            String conversationId) {
        log.info("收到流式聊天请求 | conversationId={} | messageLength={}", conversationId, message.length());
        // 关键：每个 token 到达时，立刻打印，不换行
        return chatClient.prompt()
                .user(message)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .stream() // 关键：把 call() 换成 stream()
                .content()
                .doOnNext(System.out::print)
                .doOnComplete(() -> {
                    // 流结束时换行
                    System.out.println("\n========== 流式响应结束 ==========");
                }); // 返回一个 Flux<String> 流
    }
}
