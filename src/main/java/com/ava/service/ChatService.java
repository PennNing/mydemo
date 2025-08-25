package com.ava.service;

import org.springframework.ai.chat.model.ChatResponse;
import reactor.core.publisher.Flux;

public interface ChatService {

    public String chat(String content);

    public String summarize(String content);

    public Flux<ChatResponse> chatStream(String content);

    public Flux<ChatResponse> summarizeStream(String content);

    public String chatBotApp(String content);
}
