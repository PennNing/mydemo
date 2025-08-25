package com.ava.service.impl;

import com.alibaba.dashscope.app.Application;
import com.alibaba.dashscope.app.ApplicationParam;
import com.alibaba.dashscope.app.ApplicationResult;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.ava.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class ChatServiceImpl implements ChatService {

    @Value("classpath:/summary-prompt.st")
    private Resource summaryPrompt;

    private final ChatClient chatClient;

    @Autowired
    public ChatServiceImpl(@Qualifier("openAiChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public String chat(String content) {
        return this.chatClient.prompt()
                .user(content)
                .call()
                .content();
    }

    @Override
    public String summarize(String content) {
        return this.chatClient.prompt()
                .system(summaryPrompt, StandardCharsets.UTF_8)
                .user(content)
                .call()
                .content();
    }

    @Override
    public Flux<ChatResponse> chatStream(String content) {
        var prompt = new Prompt(new UserMessage(content));
        return this.chatClient.prompt(prompt).stream().chatResponse();
    }

    @Override
    public Flux<ChatResponse> summarizeStream(String content) {
        var prompt = new Prompt(new UserMessage(content));
        return this.chatClient.prompt(prompt)
                .system(summaryPrompt, StandardCharsets.UTF_8)
                .stream()
                .chatResponse();
    }

    public String chatBotApp(String content) {
        ApplicationParam param = ApplicationParam.builder()
                // 百炼API Key将下行替换为：.apiKey("sk-xxx")。
                .apiKey("sk-662a6e34e54f425fa4bff64e610408d2")
                .appId("bccad62f828a46d8a7fab819faedffa1")
                .prompt(content)
                .build();

        Application application = new Application();
        try {
            ApplicationResult result = application.call(param);
            return result.getOutput().getText();
        } catch (NoApiKeyException | InputRequiredException e) {
            log.error("message：{}", e.getMessage());
        }
        return null;
    }
}
