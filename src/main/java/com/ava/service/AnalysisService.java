package com.ava.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class AnalysisService {

    private final ChatClient chatClient;

    // 从 classpath 加载提示词模板文件
    @Value("classpath:/prompts/key-points-prompt.st")
    private Resource keyPointsPromptResource;

    @Value("classpath:/prompts/analysis-report-prompt.st")
    private Resource analysisReportPromptResource;

    @Autowired
    public AnalysisService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    /**
     * 根据输入文本生成关键纪要
     * @param text 语音转录的原始文本
     * @return AI 生成的关键纪要
     */
    public String generateKeyPoints(String text) {
        PromptTemplate promptTemplate = new PromptTemplate(keyPointsPromptResource);
        // 将原始文本填充到提示词模板的 "text" 变量中
        var prompt = promptTemplate.create(Map.of("text", text));
        log.info("Generating key points for text: {}", text);

        return chatClient.prompt(prompt)
                .call()
                .content();
    }

    /**
     * 根据输入文本生成分析报告
     * @param text 语音转录的原始文本
     * @return AI 生成的分析报告
     */
    public String generateReport(String text) {
        PromptTemplate promptTemplate = new PromptTemplate(analysisReportPromptResource);
        // 将原始文本填充到提示词模板的 "text" 变量中
        var prompt = promptTemplate.create(Map.of("text", text));
        log.info("Generating report for text: {}", text);

        return chatClient.prompt(prompt)
                .call()
                .content();
    }
}
