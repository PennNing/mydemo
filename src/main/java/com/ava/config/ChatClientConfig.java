package com.ava.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {
    /**
     * 创建并配置一个ChatClient实例
     * 该方法通过注入的OpenAiChatModel对象初始化一个ChatClient
     * 主要作用是将聊天模型与客户端进行绑定，以便进行后续的聊天操作
     *
     * @param chatModel 聊天模型，包含了聊天所需的配置和参数
     * @return 返回配置好的ChatClient实例
     */
    @Bean
    public ChatClient chatClient(DeepSeekChatModel chatModel) {
        return ChatClient.create(chatModel);
    }
}
