package com.ava.controller;

import com.ava.service.ChatService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.nio.charset.StandardCharsets;

@RequestMapping("/assistant")
@RestController
public class ChatController {

    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/chat-text")
    public String chatWithText(@RequestParam("content") String content) {
        return chatService.chat(content);
    }

    @GetMapping("/chat-stream")
    public Flux<ChatResponse> chatWithStream(@RequestParam(value = "content", defaultValue = "Tell me a joke") String content) {
        return chatService.chatStream(content);
    }

    @GetMapping("/summarize-sync")
    public String summarizeSync(@RequestParam("content") String content) {
        return chatService.summarize(content);
    }

    @GetMapping("/summarize-sse")
    public Flux<ChatResponse> summarizeStream(@RequestParam("content") String content) {
        return chatService.summarizeStream(content);
    }

    @GetMapping("/chat-bot")
    public String chatBotApp(@RequestParam("content") String content) {
        return chatService.chatBotApp(content);
    }
}
